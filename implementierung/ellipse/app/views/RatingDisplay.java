package views;

/**
 * Enum um ein Rating darzustellen.
 */
public enum RatingDisplay {
    ONE(1, "--"), TWO(2, "-"), THREE(3, "0"), FOUR(4, "+"), FIVE(5, "++");

    private int    number;
    private String representation;

    private RatingDisplay(int number, String representation) {
        this.number = number;
        this.representation = representation;
    }

    /**
     * Gibt den Wert des Ratings zurück.
     * 
     * @return Wert des Ratings.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Gibt die Darstellung des Ratings zurück.
     * 
     * @return Darstellung des Ratings.
     */
    public String getRepresentation() {
        return representation;
    }

    /**
     * Gibt die Darstellung zu einem Wert zurück.
     * 
     * @param number
     *            Wert, zu dem die Darstellung zurückgegeben wird.
     * @return Darstellung des Ratings.
     */
    public static String getRepresentationByNumber(int number) {
        for (RatingDisplay rating : RatingDisplay.values()) {
            if (rating.getNumber() == number) {
                return rating.getRepresentation();
            }
        }
        return ONE.getRepresentation();
    }
}
