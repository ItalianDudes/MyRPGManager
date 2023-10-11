package it.italiandudes.myrpgmanager.data.dnd5e;

import it.italiandudes.myrpgmanager.data.common.Rarity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public final class DND5EElementPreview {

    // Attributes
    @NotNull private final String name;
    private final double costCopper;
    @Nullable private final Rarity rarity;
    @Nullable private final String rarityColor;
    private final double weight;
    private final int type;

    // Constructors
    public DND5EElementPreview(@NotNull final String name, final double costCopper, final int rarity, final double weight, final int type) {
        @Nullable Rarity finalRarity;
        this.name = name;
        this.costCopper = costCopper;
        try {
            finalRarity = Rarity.values()[rarity];
        } catch (IndexOutOfBoundsException e) {
            finalRarity = Rarity.COMMON;
        }
        this.rarity = finalRarity;
        if (this.rarity != null) {
            int red = (int)(this.rarity.getColor().getRed()*255);
            int green = (int)(this.rarity.getColor().getGreen()*255);
            int blue = (int)(this.rarity.getColor().getBlue()*255);
            rarityColor = String.format("#%02X%02X%02X", red, green, blue);
        } else {
            this.rarityColor = null;
        }
        this.weight = weight;
        this.type = type;
    }

    // Methods
    @NotNull
    public String getName() {
        return name;
    }
    public double getCostCopper() {
        return costCopper;
    }
    @Nullable
    public Rarity getRarity() {
        return rarity;
    }
    public double getWeight() {
        return weight;
    }
    @Nullable
    public String getRarityColor() {
        return rarityColor;
    }
    public int getType() {
        return type;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DND5EElementPreview)) return false;

        DND5EElementPreview that = (DND5EElementPreview) o;

        if (Double.compare(that.getCostCopper(), getCostCopper()) != 0) return false;
        if (Double.compare(that.getWeight(), getWeight()) != 0) return false;
        if (getType() != that.getType()) return false;
        if (!getName().equals(that.getName())) return false;
        if (getRarity() != that.getRarity()) return false;
        return getRarityColor() != null ? getRarityColor().equals(that.getRarityColor()) : that.getRarityColor() == null;
    }
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getName().hashCode();
        temp = Double.doubleToLongBits(getCostCopper());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getRarity() != null ? getRarity().hashCode() : 0);
        result = 31 * result + (getRarityColor() != null ? getRarityColor().hashCode() : 0);
        temp = Double.doubleToLongBits(getWeight());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getType();
        return result;
    }

    @Override
    public String toString() {
        return getName();
    }
}
