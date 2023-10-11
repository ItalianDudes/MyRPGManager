package it.italiandudes.myrpgmanager.data.dnd5e.item;

import it.italiandudes.myrpgmanager.data.common.Rarity;
import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.interfaces.ISavable;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

@SuppressWarnings("unused")
public class DND5EItem implements ISavable {

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
    private int itemType;

    // Constructors
    public DND5EItem() {
        name = "";
        rarity = Rarity.COMMON;
        costCopper = 0;
        weight = 0;
        this.itemType = DND5EItemTypes.TYPE_ITEM.getDatabaseValue();
    }
    public DND5EItem(@NotNull final DND5EItem DND5EItem) {
        this.itemID = DND5EItem.itemID;
        this.base64image = DND5EItem.base64image;
        this.imageExtension = DND5EItem.imageExtension;
        this.name = DND5EItem.name;
        this.costCopper = DND5EItem.costCopper;
        if (this.costCopper < 0) this.costCopper = 0;
        this.description = DND5EItem.description;
        this.rarity = DND5EItem.rarity;
        this.weight = DND5EItem.weight;
        if (this.weight < 0) this.weight = 0;
        this.itemType = DND5EItem.itemType;
    }
    public DND5EItem(@Nullable final Integer itemID, @Nullable final String base64image, @Nullable final String imageExtension, @NotNull final String name,
                     final int costCopper, @Nullable final String description, @NotNull final Rarity rarity,
                     final double weight, final int itemType) {
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
        this.itemType = itemType;
    }
    public DND5EItem(@Nullable final Integer itemID, @Nullable final Image image, @Nullable final String imageExtension, @NotNull final String name,
                     final int cc, final int cs, final int ce, final int cg, final int cp, @Nullable final String description, @NotNull final String rarity, final int itemType, final double weight) {
        this.itemID = itemID;
        this.name = name;
        this.costCopper = cc + cs*10 + ce*50 + cg*100 + cp*1000;
        this.description = description;
        this.rarity = Rarity.values()[Rarity.colorNames.indexOf(rarity)];
        ByteArrayOutputStream imageByteStream = new ByteArrayOutputStream();
        if (imageExtension != null && image != null) {
            try {
                this.imageExtension = imageExtension;
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), imageExtension, imageByteStream);
                this.base64image = Base64.getEncoder().encodeToString(imageByteStream.toByteArray());
            } catch (IOException e) {
                this.imageExtension = null;
                this.base64image = null;
            }
        } else {
            this.imageExtension = null;
            this.base64image = null;
        }
        this.itemType = itemType;
        this.weight = weight;
    }
    public DND5EItem(@NotNull final String name) throws SQLException {
        String query = "SELECT * FROM items WHERE name = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection is not initialized");
        ps.setString(1, name);
        DND5EItem retrievedDND5EItem = new DND5EItem(ps.executeQuery());
        ps.close();
        this.itemID = retrievedDND5EItem.itemID;
        this.base64image = retrievedDND5EItem.base64image;
        this.imageExtension = retrievedDND5EItem.imageExtension;
        this.name = retrievedDND5EItem.name;
        this.costCopper = retrievedDND5EItem.costCopper;
        if (this.costCopper < 0) this.costCopper = 0;
        this.description = retrievedDND5EItem.description;
        this.rarity = retrievedDND5EItem.rarity;
        this.weight = retrievedDND5EItem.weight;
        if (this.weight < 0) this.weight = 0;
        this.itemType = retrievedDND5EItem.itemType;
    }
    public DND5EItem(int itemID) throws SQLException {
        String query = "SELECT * FROM items WHERE id = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database connection is not initialized");
        ps.setInt(1, itemID);
        DND5EItem retrievedDND5EItem = new DND5EItem(ps.executeQuery());
        ps.close();
        this.itemID = retrievedDND5EItem.itemID;
        this.base64image = retrievedDND5EItem.base64image;
        this.imageExtension = retrievedDND5EItem.imageExtension;
        this.name = retrievedDND5EItem.name;
        this.costCopper = retrievedDND5EItem.costCopper;
        if (this.costCopper < 0) this.costCopper = 0;
        this.description = retrievedDND5EItem.description;
        this.rarity = retrievedDND5EItem.rarity;
        this.weight = retrievedDND5EItem.weight;
        if (this.weight < 0) this.weight = 0;
        this.itemType = retrievedDND5EItem.itemType;
    }
    public DND5EItem(@NotNull final ResultSet resultSet) throws SQLException {
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
        this.itemType = resultSet.getInt("item_type");
    }

    // Methods
    public static boolean checkIfExist(@NotNull final String itemName) throws SQLException {
        String query = "SELECT id FROM items WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("There's no connection with the database");
        ps.setString(1, itemName);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            int id = result.getInt("id");
            ps.close();
            return true;
        } else {
            ps.close();
            return false;
        }
    }
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        String itemCheckerQuery = "SELECT id FROM items WHERE name=?;";
        PreparedStatement ps = DBManager.preparedStatement(itemCheckerQuery);
        if (ps == null) throw new SQLException("The database connection doesn't exist");
        ps.setString(1, oldName);
        ResultSet result = ps.executeQuery();
        String query;
        int itemID;
        if (result.next()) { // Update
            itemID = result.getInt("id");
            ps.close();
            query = "UPDATE items SET name=?, base64image=?, image_extension=?, cost_copper=?, description=?, rarity=?, weight=?, item_type=? WHERE id=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, getName());
            ps.setString(2, getBase64image());
            ps.setString(3, getImageExtension());
            ps.setDouble(4, getCostCopper());
            ps.setString(5, getDescription());
            ps.setInt(6, Rarity.colorNames.indexOf(getRarity().getTextedRarity()));
            ps.setDouble(7, getWeight());
            ps.setInt(8, getItemType());
            ps.setInt(9, itemID);
            ps.executeUpdate();
            ps.close();
        } else { // Insert
            ps.close();
            query = "INSERT INTO items (name, base64image, image_extension, cost_copper, description, rarity, weight, item_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, getName());
            ps.setString(2, getBase64image());
            ps.setString(3, getImageExtension());
            ps.setDouble(4, getCostCopper());
            ps.setString(5, getDescription());
            ps.setInt(6, Rarity.colorNames.indexOf(getRarity().getTextedRarity()));
            ps.setDouble(7, getWeight());
            ps.setInt(8, getItemType());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM items WHERE name=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database connection doesn't exist");
            ps.setString(1, getName());
            result = ps.executeQuery();
            if (result.next()) {
                setItemID(result.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on item insert! Item insert but doesn't result on select");
            }
        }
    }
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
    public int getItemType() {
        return itemType;
    }
    public void setItemType(int itemType) {
        if (itemType >= 0)
            this.itemType = itemType;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DND5EItem)) return false;

        DND5EItem DND5EItem = (DND5EItem) o;

        if (getCostCopper() != DND5EItem.getCostCopper()) return false;
        if (Double.compare(DND5EItem.getWeight(), getWeight()) != 0) return false;
        if (getItemType() != DND5EItem.getItemType()) return false;
        if (getItemID() != null ? !getItemID().equals(DND5EItem.getItemID()) : DND5EItem.getItemID() != null) return false;
        if (getBase64image() != null ? !getBase64image().equals(DND5EItem.getBase64image()) : DND5EItem.getBase64image() != null)
            return false;
        if (getImageExtension() != null ? !getImageExtension().equals(DND5EItem.getImageExtension()) : DND5EItem.getImageExtension() != null)
            return false;
        if (!getName().equals(DND5EItem.getName())) return false;
        if (getDescription() != null ? !getDescription().equals(DND5EItem.getDescription()) : DND5EItem.getDescription() != null)
            return false;
        return getRarity() == DND5EItem.getRarity();
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
        result = 31 * result + getItemType();
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
                ", itemType=" + itemType +
                '}';
    }
}
