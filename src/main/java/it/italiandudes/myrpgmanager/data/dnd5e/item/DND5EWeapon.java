package it.italiandudes.myrpgmanager.data.dnd5e.item;

import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class DND5EWeapon extends DND5EItem implements ISavable {

    // Attributes
    @Nullable private Integer weaponID;
    @Nullable private String category;
    @Nullable private String damage;
    @Nullable private String properties;
    private int strengthRequired;

    // Constructors
    public DND5EWeapon() {
        super();
        setItemType(DND5EItemTypes.TYPE_WEAPON.getDatabaseValue());
    }
    public DND5EWeapon(@NotNull final DND5EItem baseDND5EItem, @Nullable final Integer weaponID, @Nullable final String category,
                       @Nullable final String damage, @Nullable final String properties, @Nullable final Integer strengthRequired) {
        super(baseDND5EItem);
        this.weaponID = weaponID;
        this.category = category;
        this.damage = damage;
        this.properties = properties;
        if (strengthRequired == null || strengthRequired < 0) {
            this.strengthRequired = 0;
        } else {
            this.strengthRequired = strengthRequired;
        }
    }
    public DND5EWeapon(@NotNull final String weaponName) throws SQLException {
        super(weaponName);
        String query = "SELECT * FROM weapons WHERE item_id = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        Integer itemID = getItemID();
        assert itemID != null;
        ps.setInt(1, itemID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.category = result.getString("category");
            this.weaponID = result.getInt("id");
            this.strengthRequired = result.getInt("strength_required");
            this.properties = result.getString("properties");
            this.damage = result.getString("damage");
            ps.close();
        } else {
            ps.close();
            throw new SQLException("Exist the item, but not the weapon");
        }
    }
    public DND5EWeapon(@NotNull final ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("item_id"));
        this.weaponID = resultSet.getInt("id");
        try {
            this.category = resultSet.getString("category");
        } catch (SQLException e) {
            this.category = null;
        }
        try {
            this.damage = resultSet.getString("damage");
        } catch (SQLException e) {
            this.damage = null;
        }
        try {
            this.properties = resultSet.getString("properties");
        } catch (SQLException e) {
            this.properties = null;
        }
        try {
            this.strengthRequired = resultSet.getInt("strength_required");
        } catch (SQLException e) {
            this.strengthRequired = 0;
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        super.saveIntoDatabase(oldName);
        Integer itemID = getItemID();
        assert itemID != null;
        if (weaponID == null) { // Insert
            String query = "INSERT INTO weapons (item_id, category, damage, properties, strength_required) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setString(2, getCategory());
            ps.setString(3, getDamage());
            ps.setString(4, getProperties());
            ps.setInt(5, getStrengthRequired());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM weapons WHERE item_id = ?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setWeaponID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on weapon insert! Weapon insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE weapons SET item_id=?, category=?, damage=?, properties=?, strength_required=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setString(2, getCategory());
            ps.setString(3, getDamage());
            ps.setString(4, getProperties());
            ps.setInt(5, getStrengthRequired());
            ps.setInt(6, getWeaponID());
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getWeaponID() {
        return weaponID;
    }
    public void setWeaponID(final int weaponID) {
        if (this.weaponID == null) this.weaponID = weaponID;
    }
    @Nullable
    public String getCategory() {
        return category;
    }
    public void setCategory(@Nullable final String category) {
        this.category = category;
    }
    @Nullable
    public String getDamage() {
        return damage;
    }
    public void setDamage(@Nullable final String damage) {
        this.damage = damage;
    }
    @Nullable
    public String getProperties() {
        return properties;
    }
    public void setProperties(@Nullable final String properties) {
        this.properties = properties;
    }
    public int getStrengthRequired() {
        return strengthRequired;
    }
    public void setStrengthRequired(@Nullable final Integer strengthRequired) {
        if (strengthRequired == null || strengthRequired < 0) {
            this.strengthRequired = 0;
        } else {
            this.strengthRequired = strengthRequired;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DND5EWeapon)) return false;
        if (!super.equals(o)) return false;

        DND5EWeapon DND5EWeapon = (DND5EWeapon) o;

        if (getStrengthRequired() != DND5EWeapon.getStrengthRequired()) return false;
        if (getWeaponID() != null ? !getWeaponID().equals(DND5EWeapon.getWeaponID()) : DND5EWeapon.getWeaponID() != null)
            return false;
        if (getCategory() != null ? !getCategory().equals(DND5EWeapon.getCategory()) : DND5EWeapon.getCategory() != null)
            return false;
        if (getDamage() != null ? !getDamage().equals(DND5EWeapon.getDamage()) : DND5EWeapon.getDamage() != null) return false;
        return getProperties() != null ? getProperties().equals(DND5EWeapon.getProperties()) : DND5EWeapon.getProperties() == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getWeaponID() != null ? getWeaponID().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getDamage() != null ? getDamage().hashCode() : 0);
        result = 31 * result + (getProperties() != null ? getProperties().hashCode() : 0);
        result = 31 * result + getStrengthRequired();
        return result;
    }
    @Override
    public String toString() {
        return "Weapon{" +
                "weaponID=" + weaponID +
                ", category='" + category + '\'' +
                ", damage='" + damage + '\'' +
                ", properties='" + properties + '\'' +
                ", strengthRequired=" + strengthRequired +
                "} " + super.toString();
    }
}
