package PokeSmart;



public class PhysicalEntity {
    private String nom;
    private String pseudo;
    /**
     * à voir s'il faut garder refBag
     */
    private double refBag; //reference of the entity's bag
    // lien avec la position définie dans Tile/Position
    private double HealthPoints;

    /**
     * constructeur d'une entité physique
     * permet d'associer à l'entité un nom, pseudo et des points de vie
     *
     * @param nom
     * @param pseudo
     * @param refBag
     * @param HealthPoints
     */
    public PhysicalEntity(String nom, String pseudo, double refBag, double HealthPoints) { //ajouter les coordonnées géographiques
        this.nom = nom;
        this.pseudo = pseudo;
        this.refBag = refBag;
        this.HealthPoints = HealthPoints;
        //this.position = position;
    }

    /**
     * récupère le nom de l'entité, permet de différencier 2 entités ayant le même pseudo
     *
     * @return nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * associer un nouveau nom à l'entité
     *
     * @param nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * récupère le pseudo de l'entité, utilisé dans le jeu
     *
     * @return
     */
    public String getPseudo() {
        return pseudo;
    }

    /**
     * associer un nouveau pseudo à l'entité
     *
     * @param pseudo
     */
    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    /**
     * retourne la référence du sac de l'inventaire
     *
     * @return
     */
    public double getRefBag() {
        return refBag;
    }

    /**
     * associer un nouveau sac d'objets à un joueur
     *
     * @param refBag
     */
    public void setRefBag(double refBag) {
        this.refBag = refBag;
    }

    /**
     * retourner le nombre de points de vie de l'entité
     *
     * @return HealthPoints
     */
    public double getHealthPoints() {
        return HealthPoints;
    }

    /**
     * associer de nouveaux points de vie à l'entité
     *
     * @param healthPoints
     */
    public void setHealthPoints(double healthPoints) {
        HealthPoints = healthPoints;
    }
}
