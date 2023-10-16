package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSSpellType {
    MIRACLE(0, "Miracolo"),
    SORCERY(1, "Stregoneria"),
    PIROMANCY(2,"Piromanzia"),
    HEX(3, "Malocchio");

    // Attributes
    @NotNull
    public static final ArrayList<DNSSpellType> spell_types = new ArrayList<>();
    static {
        spell_types.addAll(Arrays.asList(DNSSpellType.values()));
    }
    private final int databaseValue;
    @NotNull private final String name;

    // Constructors
    DNSSpellType(final int databaseValue, @NotNull final String name) {
        this.databaseValue = databaseValue;
        this.name = name;
    }

    // Methods
    public int getDatabaseValue() {
        return databaseValue;
    }
    @NotNull
    public String getName() {
        return name;
    }
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
