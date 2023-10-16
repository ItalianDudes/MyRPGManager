package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSWeaponType {
    MELEE_WEAPON(0, "Arma da Mischia"),
    RANGED_WEAPON(1, "Arma a Distanza"),
    CASTER_WEAPON(2, "Catalizzatore")
    ;

    // Attributes
    @NotNull
    public static final ArrayList<DNSWeaponType> types = new ArrayList<>();
    static {
        types.addAll(Arrays.asList(DNSWeaponType.values()));
    }
    private final int databaseValue;
    private final String name;

    // Constructors
    DNSWeaponType(final int databaseValue, final String name) {
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
