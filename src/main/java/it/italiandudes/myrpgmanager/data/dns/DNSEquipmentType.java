package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSEquipmentType {
    ARMOR(0, "Armatura"),
    WEAPON(1, "Arma"),
    ADDON(2, "Addon")
    ;

    // Attributes
    @NotNull
    public static final ArrayList<DNSEquipmentType> types = new ArrayList<>();
    static {
        types.addAll(Arrays.asList(DNSEquipmentType.values()));
    }
    private final int databaseValue;
    private final String name;

    // Constructors
    DNSEquipmentType(final int databaseValue, final String name) {
        this.databaseValue = databaseValue;
        this.name = name;
    }

    // Methods
    public int getDatabaseValue() {
        return databaseValue;
    }
    public String getName() {
        return name;
    }
    @Override
    public String toString() {
        return getName();
    }
}
