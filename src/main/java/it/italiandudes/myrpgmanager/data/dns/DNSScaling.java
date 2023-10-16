package it.italiandudes.myrpgmanager.data.dns;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public enum DNSScaling {
    NA(0, "-"),
    F(1, "F"),
    E(2, "E"),
    D(3, "D"),
    C(4, "C"),
    B(5, "B"),
    A(6, "A"),
    S(7, "S"),
    SS(8, "SS"),
    SSS(9, "SSS");

    // Attributes
    @NotNull
    public static final ArrayList<DNSScaling> scaling = new ArrayList<>();
    static {
        scaling.addAll(Arrays.asList(DNSScaling.values()));
    }
    private final int databaseValue;
    @NotNull private final String name;

    // Constructors
    DNSScaling(final int databaseValue, @NotNull final String name) {
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
