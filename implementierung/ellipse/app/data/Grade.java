package data;

import com.avaje.ebean.annotation.DbEnumValue;

public enum Grade {

    ONE_ZERO("1.0", 100), ONE_THREE("1.3", 130), ONE_SEVEN("1.7",
            170), TWO_ZERO("2.0", 200), TWO_THREE("2.3", 230), TWO_SEVEN("2.7",
                    270), THREE_ZERO("3.0", 300), THREE_THREE("3.3",
                            330), THREE_SEVEN("3.7", 330), FOUR_ZERO("4.0",
                                    400), FIVE_ZERO("5.0",
                                            500), UNKNOWN("unknown", 0);

    /**
     * String der Note.
     */
    private String name;
    /**
     * Note als 3-stellige Zahl.
     */
    private int    number;

    private Grade(String name, int number) {
        this.name = name;
        this.number = number;
    }

    /**
     * Gibt den String der Note zurück.
     * 
     * @return String der Note.
     */
    public String getName() {
        return name;
    }

    /**
     * Gibt die 3-stellige Zahl der Note zurück.
     * 
     * @return 3-stellige Zahl der Note.
     */
    @DbEnumValue
    public int getNumber() {
        return number;
    }

    /**
     * Gibt die Note zurück, die der 3-stelligen Zahl entspricht.
     * 
     * @param number 3-stellige Zahl.
     * @return Note.
     */
    public static Grade getGradeByNumber(int number) {
        for (Grade grade : Grade.values()) {
            if (grade.getNumber() == number) {
                return grade;
            }
        }
        return Grade.UNKNOWN;
    }
}
