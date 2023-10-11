package it.italiandudes.myrpgmanager.data.dns;

import it.italiandudes.myrpgmanager.data.common.Rarity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class DNSElementPreview {

    // Attributes
    private final int id;
    @NotNull
    private final String name;
    private final double costCopper;
    @NotNull
    private final DNSCategory category;
    @NotNull
    private final Rarity rarity;
    private final double weight;

    // Constructors
    public DNSElementPreview(final int id, @NotNull final String name, @NotNull final DNSCategory category, @NotNull final Rarity rarity, final double weight, final int costCopper) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.costCopper = costCopper;
        this.rarity = rarity;
        this.weight = weight;
    }

    // Methods
    public int getId() {
        return id;
    }
    @NotNull
    public String getName() {
        return name;
    }
    public double getCostCopper() {
        return costCopper;
    }
    @NotNull
    public DNSCategory getCategory() {
        return category;
    }
    @NotNull
    public Rarity getRarity() {
        return rarity;
    }
    public double getWeight() {
        return weight;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DNSElementPreview)) return false;
        DNSElementPreview that = (DNSElementPreview) o;
        return getId() == that.getId() && Double.compare(getCostCopper(), that.getCostCopper()) == 0 && Double.compare(getWeight(), that.getWeight()) == 0 && Objects.equals(getName(), that.getName()) && getCategory() == that.getCategory() && getRarity() == that.getRarity();
    }
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCostCopper(), getCategory(), getRarity(), getWeight());
    }
    @Override @NotNull
    public String toString() {
        return getName();
    }
}
