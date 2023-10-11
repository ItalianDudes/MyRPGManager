package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSCategory {
    ITEM(0, "Oggetto"),
    EQUIPMENT(1, "Equipaggiamento"),
    SPELL(2, "Incantesimo");

    // Attributes
    @NotNull public static final ArrayList<DNSCategory> categories = new ArrayList<>();
    static {
        categories.addAll(Arrays.asList(DNSCategory.values()));
    }
    private final int databaseValue;
    @NotNull private final String name;

    // Constructors
    DNSCategory(final int databaseValue, @NotNull final String name) {
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
