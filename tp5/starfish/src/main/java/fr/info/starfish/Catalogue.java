package fr.info.starfish;
import lombok.Data;

@Data
public class Catalogue {
  private String titre = "";
  private String auteur = "";
  private String categorie = "";

  public void init(String titre, String auteur, String categorie) {
    this.titre = titre;
    this.auteur = auteur;
    this.categorie = categorie;
  }

  @Override
  public String toString() {
    return "catalogue";
  }
}