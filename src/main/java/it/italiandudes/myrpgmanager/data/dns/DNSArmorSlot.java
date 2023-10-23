package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSArmorSlot {
    FULL_SET(0, "Set Completo"),
    HEAD(1, "Testa"),
    LEFT_SHOULDER(2, "Spalla SX"),
    RIGHT_SHOULDER(3, "Spalla DX"),
    LEFT_FOREARM(4, "Avambraccio SX"),
    RIGHT_FOREARM(5, "Avambraccio DX"),
    LEFT_HAND(6, "Mano SX"),
    RIGHT_HAND(7, "Mano DX"),
    TRUNK(8, "Tronco"),
    LEFT_LEG(9, "Gamba SX"),
    RIGHT_LEG(10, "Gamba DX"),
    LEFT_FOOT(11, "Piede SX"),
    RIGHT_FOOT(12, "Piede DX")
    ;

    // Attributes
    @NotNull
    public static final ArrayList<DNSArmorSlot> armor_slots = new ArrayList<>();
    static {
        armor_slots.addAll(Arrays.asList(DNSArmorSlot.values()));
    }
    private final int databaseValue;
    private final String name;

    // Constructors
    DNSArmorSlot(final int databaseValue, final String name) {
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
