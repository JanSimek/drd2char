package xyz.simek.drd2char;

enum RaceEnum { ELF, HOBBIT, KROLL, HUMAN, DWARF; }

public class Character {
    private String name, description;
    private RaceEnum race;

    public Character(String name, RaceEnum race, String description) {
        this.name = name;
        this.race = race;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public RaceEnum getRace() {
        return this.race;
    }

    public String getDescription() {
        return this.description;
    }
}
