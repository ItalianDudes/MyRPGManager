package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSAddonSlot {
    NECKLACE(0, "Collana"),
    MANTLE(1, "Mantello"),
    BRACELET(3, "Bracciale"),
    EARRING(5, "Orecchino"),
    RING(6, "Anello")
    ;

    // Attributes
    @NotNull
    public static final ArrayList<DNSAddonSlot> addon_slots = new ArrayList<>();
    static {
        addon_slots.addAll(Arrays.asList(DNSAddonSlot.values()));
    }
    private final int databaseValue;
    private final String name;

    // Constructors
    DNSAddonSlot(final int databaseValue, final String name) {
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
