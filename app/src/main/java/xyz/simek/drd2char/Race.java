package xyz.simek.drd2char;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by jansimek on 09.05.16.
 */
public class Race {
    private String name, description;
    ArrayList<Ability> abilities = new ArrayList<>();
    ArrayList<Ability> cultures = new ArrayList<>(); // FIXME: use Culture object

    public Race(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Race() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public ArrayList<Ability> getAbilities() {
        return this.abilities;
    }

    public void addAbility(Ability ability) {
        abilities.add(ability);
    }

    public ArrayList<Ability> getCultures() {
        return this.cultures;
    }
    public void addCulture(Ability culture) {
        cultures.add(culture);
    }

}
