package views;

public enum RatingDisplay {
    ONE(1, "--"), TWO(2, "-"), THREE(3, "0"), FOUR(4, "+"), FIVE(5, "++");

    private int    number;
    private String representation;

    private RatingDisplay(int number, String representation) {
        this.number = number;
        this.representation = representation;
    }

    public int getNumber() {
        return number;
    }

    public String getRepresentation() {
        return representation;
    }

    public static String getRepresentationByNumber(int number) {
        for (RatingDisplay rating : RatingDisplay.values()) {
            if (rating.getNumber() == number) {
                return rating.getRepresentation();
            }
        }
        return ONE.getRepresentation();
    }
}
