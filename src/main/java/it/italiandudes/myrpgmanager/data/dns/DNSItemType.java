package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSItemType {
    ITEM(0, "Oggetto"),
    MATERIAL(1, "Materiale"),
    KEY_ITEM(2,"Oggetto Chiave"),
    CONSUMABLE(3, "Consumabile"),
    AMMUNITION(4, "Munizione");

    // Attributes
    @NotNull
    public static final ArrayList<DNSItemType> item_types = new ArrayList<>();
    static {
        item_types.addAll(Arrays.asList(DNSItemType.values()));
    }
    private final int databaseValue;
    @NotNull private final String name;

    // Constructors
    DNSItemType(final int databaseValue, @NotNull final String name) {
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
