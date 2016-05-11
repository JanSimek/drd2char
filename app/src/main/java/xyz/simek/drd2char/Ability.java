package xyz.simek.drd2char;

/**
 * Created by jansimek on 09.05.16.
 */
public class Ability {
    private String name, description;

    public Ability(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }
}
