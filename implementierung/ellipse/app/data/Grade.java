package data;

import com.avaje.ebean.annotation.DbEnumValue;

public enum Grade {

    ONE_ZERO("1.0", 100), ONE_THREE("1.3", 130), ONE_SEVEN("1.7", 170), TWO_ZERO("2.0", 200), TWO_THREE("2.3",
            230), TWO_SEVEN("2.7", 270), THREE_ZERO("3.0", 300), THREE_THREE("3.3",
                    330), THREE_SEVEN("3.7", 330), FOUR_ZERO("4.0", 400), FIVE_ZERO("5.0", 500), UNKNOWN("unknown", 0);

    private String name;
    private int    number;

    private Grade(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    @DbEnumValue
    public int getNumber() {
        return number;
    }
}
