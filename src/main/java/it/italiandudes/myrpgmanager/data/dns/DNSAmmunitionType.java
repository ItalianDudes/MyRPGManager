package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSAmmunitionType {
    OTHER(0, "Altro"),
    ARROW(1, "Freccia"),
    BOLT(2, "Dardo");

    // Attributes
    @NotNull
    public static final ArrayList<DNSAmmunitionType> ammo_types = new ArrayList<>();
    static {
        ammo_types.addAll(Arrays.asList(DNSAmmunitionType.values()));
    }
    private final int databaseValue;
    @NotNull private final String name;

    // Constructors
    DNSAmmunitionType(final int databaseValue, @NotNull final String name) {
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
