package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSEquipmentType {
    WEAPON(0, "Arma"),
    SHIELD(1, "Scudo"),
    ARMOR(2, "Armatura"),
    COSMETICS(3, "Cosmetico");

    // Attributes
    @NotNull
    public static final ArrayList<DNSEquipmentType> equipment_types = new ArrayList<>();
    static {
        equipment_types.addAll(Arrays.asList(DNSEquipmentType.values()));
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
