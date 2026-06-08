package fr.info.starfish;

import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.json.JSONObject;
import java.io.IOException;
import fr.info.starfish.Catalogue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@ComponentScan("fr.info")
public class Controleur {
  private final Services services;
  private String menu = "catalogue";
  private static final Logger logger = LoggerFactory.getLogger(Controleur.class);

  @Autowired
  public Controleur(Services services) {
    this.services = services;
  }

  @ModelAttribute
  public void addAttributes(Model model) {
    model.addAttribute("menu", menu);
  }

  @GetMapping("/index")
  public String index(Model model) { return "index"; }

  @GetMapping("/accueil")
  public String accueil(Model model) {
    model.addAttribute("latitude", 48.8566);
    model.addAttribute("longitude", 2.3522);
    return "accueil";
  }

  @GetMapping("/catalogue")
  public String liste(Model model) {
    menu = "catalogue";
    model.addAttribute("menu", menu);
    model.addAttribute("liste", services.liste());
    return "catalogue";
  }

  @GetMapping("/selection")
  public String selection(Model model) {
    menu = "selection";
    model.addAttribute("menu", menu);
    model.addAttribute("catalogue", services.selection());
    return "catalogue";
  }

  @GetMapping("/ajouter")
  public String ajouter(@ModelAttribute("catalogue") Catalogue catalogue, Model model) {
    services.ajouterCatalogue(catalogue);
    return "redirect:/catalogue";
  }

  @PostMapping("/search")
  public String search(@RequestParam("query") String query, Model model) {
      logger.info("Recherche demandée : '{}'", query);
      try {
          JSONObject location = services.search(query);
          if (location != null) {
              double dlat = Double.parseDouble(location.getString("lat"));
              double dlon = Double.parseDouble(location.getString("lon"));
              logger.info("Résultat : lat={}, lon={}", dlat, dlon);
              model.addAttribute("latitude", dlat);
              model.addAttribute("longitude", dlon);
          } else {
              logger.warn("Aucun résultat pour '{}'", query);
              model.addAttribute("message", "Lieu introuvable : « " + query + " »");
          }
      } catch (IOException e) {
          logger.error("Erreur réseau pour '{}' : {}", query, e.getMessage());
          model.addAttribute("message", "Erreur de connexion : " + e.getMessage());
      }
      return "accueil";
  }
}