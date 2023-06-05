package it.italiandudes.myrpgmanager.data;

import it.italiandudes.myrpgmanager.db.DBManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public class Item {

    // Attributes
    @Nullable private Integer itemID;
    @Nullable private String base64image;
    @Nullable private String imageExtension;
    @NotNull private String name;
    private int costCopper;
    @Nullable private String description;
    @NotNull
    private Rarity rarity;
    private double weight;

    // Constructors
    public Item() {
        name = "";
        rarity = Rarity.COMMON;
        costCopper = 0;
        weight = 0;
    }
    public Item(@NotNull final Item item) {
        this.itemID = item.itemID;
        this.base64image = item.base64image;
        this.imageExtension = item.imageExtension;
        this.name = item.name;
        this.costCopper = item.costCopper;
        if (this.costCopper < 0) this.costCopper = 0;
        this.description = item.description;
        this.rarity = item.rarity;
        this.weight = item.weight;
        if (this.weight < 0) this.weight = 0;
    }
    public Item(@Nullable final Integer itemID, @Nullable final String base64image, @Nullable final String imageExtension, @NotNull final String name,
                final int costCopper, @Nullable final String description, @NotNull final Rarity rarity,
                final double weight) {
        this.itemID = itemID;
        this.base64image = base64image;
        this.imageExtension = imageExtension;
        this.name = name;
        this.costCopper = costCopper;
        if (this.costCopper < 0) this.costCopper = 0;
        this.description = description;
        this.rarity = rarity;
        this.weight = weight;
        if (this.weight < 0) this.weight = 0;
    }
    public Item(@NotNull final String name) throws SQLException {
        String query = "SELECT * FROM items WHERE name = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection is not initialized");
        ps.setString(1, name);
        Item retrievedItem = new Item(ps.executeQuery());
        ps.close();
        this.itemID = retrievedItem.itemID;
        this.base64image = retrievedItem.base64image;
        this.imageExtension = retrievedItem.imageExtension;
        this.name = retrievedItem.name;
        this.costCopper = retrievedItem.costCopper;
        if (this.costCopper < 0) this.costCopper = 0;
        this.description = retrievedItem.description;
        this.rarity = retrievedItem.rarity;
        this.weight = retrievedItem.weight;
        if (this.weight < 0) this.weight = 0;
    }
    public Item(int itemID) throws SQLException {
        String query = "SELECT * FROM items WHERE id = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection is not initialized");
        ps.setInt(1, itemID);
        Item retrievedItem = new Item(ps.executeQuery());
        ps.close();
        this.itemID = retrievedItem.itemID;
        this.base64image = retrievedItem.base64image;
        this.imageExtension = retrievedItem.imageExtension;
        this.name = retrievedItem.name;
        this.costCopper = retrievedItem.costCopper;
        if (this.costCopper < 0) this.costCopper = 0;
        this.description = retrievedItem.description;
        this.rarity = retrievedItem.rarity;
        this.weight = retrievedItem.weight;
        if (this.weight < 0) this.weight = 0;
    }
    public Item(@NotNull final ResultSet resultSet) throws SQLException {
        this.itemID = resultSet.getInt("id");
        try {
            this.base64image = resultSet.getString("base64image");
            this.imageExtension = resultSet.getString("image_extension");
        } catch (SQLException e) {
            this.base64image = null;
            this.imageExtension = null;
        }
        this.name = resultSet.getString("name");
        try {
            this.costCopper = resultSet.getInt("cost_copper");
            if (this.costCopper < 0) this.costCopper = 0;
        } catch (SQLException e) {
            this.costCopper = 0;
        }
        try {
            this.description = resultSet.getString("description");
        } catch (SQLException e) {
            this.description = null;
        }
        this.rarity = Rarity.values()[resultSet.getInt("rarity")];
        this.weight = resultSet.getDouble("weight");
        if (this.weight < 0) this.weight = 0;
    }

    // Methods
    @Nullable
    public Integer getItemID() {
        return itemID;
    }
    public void setItemID(final int itemID) {
        if (this.itemID == null) this.itemID = itemID;
    }
    @Nullable
    public String getBase64image() {
        return base64image;
    }
    public void setBase64image(@Nullable final String base64image) {
        this.base64image = base64image;
    }
    @Nullable
    public String getImageExtension() {
        return imageExtension;
    }
    public void setImageExtension(@Nullable final String imageExtension) {
        this.imageExtension = imageExtension;
    }
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(@NotNull final String name) {
        this.name = name;
    }
    public int getCostCopper() {
        return costCopper;
    }
    public void setCostCopper(final int costCopper) {
        if (costCopper >= 0) this.costCopper = costCopper;
    }
    @Nullable
    public String getDescription() {
        return description;
    }
    public void setDescription(@Nullable final String description) {
        this.description = description;
    }
    @NotNull
    public Rarity getRarity() {
        return rarity;
    }
    public void setRarity(@NotNull final Rarity rarity) {
        this.rarity = rarity;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(final double weight) {
        if (weight >= 0) this.weight = weight;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (getCostCopper() != item.getCostCopper()) return false;
        if (Double.compare(item.getWeight(), getWeight()) != 0) return false;
        if (getItemID() != null ? !getItemID().equals(item.getItemID()) : item.getItemID() != null) return false;
        if (getBase64image() != null ? !getBase64image().equals(item.getBase64image()) : item.getBase64image() != null)
            return false;
        if (getImageExtension() != null ? !getImageExtension().equals(item.getImageExtension()) : item.getImageExtension() != null)
            return false;
        if (!getName().equals(item.getName())) return false;
        if (getDescription() != null ? !getDescription().equals(item.getDescription()) : item.getDescription() != null)
            return false;
        return getRarity() == item.getRarity();
    }
    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getItemID() != null ? getItemID().hashCode() : 0;
        result = 31 * result + (getBase64image() != null ? getBase64image().hashCode() : 0);
        result = 31 * result + (getImageExtension() != null ? getImageExtension().hashCode() : 0);
        result = 31 * result + getName().hashCode();
        result = 31 * result + getCostCopper();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + getRarity().hashCode();
        temp = Double.doubleToLongBits(getWeight());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    @Override
    public String toString() {
        return "Item{" +
                "itemID=" + itemID +
                ", base64image='" + base64image + '\'' +
                ", imageExtension='" + imageExtension + '\'' +
                ", name='" + name + '\'' +
                ", costCopper=" + costCopper +
                ", description='" + description + '\'' +
                ", rarity=" + rarity +
                ", weight=" + weight +
                '}';
    }
}
