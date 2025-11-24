package fr.but3.model;

public class JourStats {

    private int nbCreneauxDisponibles;
    private int nbTotalPersonnes;
    private boolean ouvert;
    private boolean ferie;
    private double tauxOccupation;
    private boolean limiteDepassee;


    public JourStats(int nbCreneauxDisponibles,
                     int nbTotalPersonnes,
                     boolean ouvert,
                     boolean ferie,
                     double tauxOccupation,
                     boolean limiteDepassee) {

        this.nbCreneauxDisponibles = nbCreneauxDisponibles;
        this.nbTotalPersonnes = nbTotalPersonnes;
        this.ouvert = ouvert;
        this.ferie = ferie;
        this.tauxOccupation = tauxOccupation;
        this.limiteDepassee = limiteDepassee;
    }

    public int getNbCreneauxDisponibles() {
        return nbCreneauxDisponibles;
    }

    public int getNbTotalPersonnes() {
        return nbTotalPersonnes;
    }

    public boolean isOuvert() {
        return ouvert;
    }

    public boolean isFerie() {
        return ferie;
    }

    public double getTauxOccupation() {
        return tauxOccupation;
    }

    public boolean isLimiteDepassee() { 
        return limiteDepassee;
    }
}
