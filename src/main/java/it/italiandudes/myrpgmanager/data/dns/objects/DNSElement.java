package it.italiandudes.myrpgmanager.data.dns.objects;

import it.italiandudes.myrpgmanager.data.common.Image64;
import it.italiandudes.myrpgmanager.data.common.Rarity;
import it.italiandudes.myrpgmanager.data.dns.DNSCategory;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.interfaces.ISavable;
import it.italiandudes.myrpgmanager.interfaces.ISerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public abstract class DNSElement implements ISavable, ISerializable {

    // Attributes
    @Nullable private Integer elementID;
    @NotNull private String name;
    @NotNull private DNSCategory category;
    @NotNull private Rarity rarity;
    @Nullable private Image64 image;
    @Nullable private Integer costCopper;
    @Nullable private Double weight;
    @Nullable private String description;

    // Constructors
    public DNSElement(@NotNull final String name, @NotNull final DNSCategory category) {
        this.name = name;
        this.category = category;
        this.rarity = Rarity.COMMON;
    }
    public DNSElement(
            @Nullable final Integer elementID, @NotNull final String name, @NotNull final Rarity rarity,
            @NotNull final DNSCategory category, @Nullable final Image64 image, @Nullable Integer costCopper,
            @Nullable final Double weight, @Nullable final String description
    ) {
        this(name, category);
        this.elementID = elementID;
        this.rarity = rarity;
        this.image = image;
        this.costCopper = costCopper;
        this.weight = weight;
        this.description = description;
    }

    // Exist
    public static boolean getElement(@NotNull final String name) throws SQLException {
        String query = "SELECT * FROM elements WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection is not initialized");
        ps.setString(1, name);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            result.close();
            return true;
        } else {
            result.close();
            return false;
        }
    }

    // Methods
    @Nullable
    public Integer getElementID() {
        return elementID;
    }
    public void setElementID(final int elementID) {
        if (this.elementID == null) this.elementID = elementID;
    }
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(@NotNull final String name) {
        this.name = name;
    }
    @NotNull
    public DNSCategory getCategory() {
        return category;
    }
    public void setCategory(@NotNull final DNSCategory category) {
        this.category = category;
    }
    @NotNull
    public Rarity getRarity() {
        return rarity;
    }
    public void setRarity(@NotNull final Rarity rarity) {
        this.rarity = rarity;
    }
    @Nullable
    public Image64 getImage() {
        return image;
    }
    public void setImage(@Nullable final Image64 image) {
        this.image = image;
    }
    @Nullable
    public Integer getCostCopper() {
        return costCopper;
    }
    public void setCostCopper(@Nullable final Integer costCopper) {
        this.costCopper = costCopper;
    }
    @Nullable
    public Double getWeight() {
        return weight;
    }
    public void setWeight(@Nullable final Double weight) {
        this.weight = weight;
    }
    @Nullable
    public String getDescription() {
        return description;
    }
    public void setDescription(@Nullable final String description) {
        this.description = description;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DNSElement)) return false;
        DNSElement that = (DNSElement) o;
        return Objects.equals(getElementID(), that.getElementID()) && Objects.equals(getName(), that.getName()) && getCategory() == that.getCategory() && getRarity() == that.getRarity() && Objects.equals(getImage(), that.getImage()) && Objects.equals(getCostCopper(), that.getCostCopper()) && Objects.equals(getWeight(), that.getWeight()) && Objects.equals(getDescription(), that.getDescription());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getElementID(), getName(), getCategory(), getRarity(), getImage(), getCostCopper(), getWeight(), getDescription());
    }
    @Override
    public String toString() {
        return getName();
    }
}
