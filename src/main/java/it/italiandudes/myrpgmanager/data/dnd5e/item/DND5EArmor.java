package it.italiandudes.myrpgmanager.data.dnd5e.item;

import it.italiandudes.myrpgmanager.db.DBManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class DND5EArmor extends DND5EItem {

    // Attributes
    @Nullable private Integer armorID;
    @Nullable private String category;
    private int AC;
    private int strengthRequired;
    private int stealth;

    // Constructors
    public DND5EArmor() {
        super();
        setItemType(DND5EItemTypes.TYPE_ARMOR.getDatabaseValue());
        AC = 0;
        strengthRequired = 0;
        stealth = 0;
    }
    public DND5EArmor(@NotNull final DND5EItem baseDND5EItem, @Nullable final Integer armorID,
                      @Nullable final String category, final int AC,
                      final int strengthRequired, final int stealth) {
        super(baseDND5EItem);
        this.armorID = armorID;
        this.category = category;
        this.AC = Math.max(AC, 0);
        this.strengthRequired = Math.max(strengthRequired, 0);
        if (stealth >= -1 && stealth <= 1) this.stealth = stealth;
        else this.stealth = 0;
    }
    public DND5EArmor(@NotNull final String armorName) throws SQLException {
        super(armorName);
        String query = "SELECT * FROM armors WHERE item_id = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        Integer itemID = getItemID();
        assert itemID != null;
        ps.setInt(1, itemID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.category = result.getString("category");
            this.armorID = result.getInt("id");
            this.strengthRequired = result.getInt("strength_required");
            this.AC = result.getInt("ac");
            this.stealth = result.getInt("stealth");
            ps.close();
        } else {
            ps.close();
            throw new SQLException("Exist the item, but not the armor");
        }
    }
    public DND5EArmor(@NotNull final ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("item_id"));
        this.armorID = resultSet.getInt("id");
        try {
            this.category = resultSet.getString("category");
        } catch (SQLException e) {
            this.category = null;
        }
        try {
            this.AC = resultSet.getInt("ac");
            if (this.AC < 0) this.AC = 0;
        } catch (SQLException e) {
            this.AC = 0;
        }
        try {
            this.strengthRequired = resultSet.getInt("strength_required");
            if (this.strengthRequired < 0) this.strengthRequired = 0;
        } catch (SQLException e) {
            this.strengthRequired = 0;
        }
        try {
            this.stealth = resultSet.getInt("stealth");
            if (this.stealth < -1 || this.stealth > 1) this.stealth = 0;
        } catch (SQLException e) {
            this.stealth = 0;
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        super.saveIntoDatabase(oldName);
        Integer itemID = getItemID();
        assert itemID != null;
        if (armorID == null) { // Insert
            String query = "INSERT INTO armors (item_id, category, ac, strength_required, stealth) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setString(2, getCategory());
            ps.setInt(3, getAC());
            ps.setInt(4, getStrengthRequired());
            ps.setInt(5, getStealth());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM armors WHERE item_id = ?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setArmorID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on armor insert! Armor insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE armors SET item_id=?, category=?, ac=?, strength_required=?, stealth=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setString(2, getCategory());
            ps.setInt(3, getAC());
            ps.setInt(4, getStrengthRequired());
            ps.setInt(5, getStealth());
            ps.setInt(6, getArmorID());
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getArmorID() {
        return armorID;
    }
    public void setArmorID(final int armorID) {
        if (this.armorID == null) this.armorID = armorID;
    }
    @Nullable
    public String getCategory() {
        return category;
    }
    public void setCategory(@Nullable final String category) {
        this.category = category;
    }
    public int getAC() {
        return AC;
    }
    public void setAC(int AC) {
        if (AC >= 0) this.AC = AC;
    }
    public int getStrengthRequired() {
        return strengthRequired;
    }
    public void setStrengthRequired(int strengthRequired) {
        if (strengthRequired >= 0) this.strengthRequired = strengthRequired;
    }
    public int getStealth() {
        return stealth;
    }
    public void setStealth(int stealth) {
        if (stealth >= -1 && stealth <= 1) this.stealth = stealth;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DND5EArmor)) return false;
        if (!super.equals(o)) return false;

        DND5EArmor DND5EArmor = (DND5EArmor) o;

        if (getAC() != DND5EArmor.getAC()) return false;
        if (getStrengthRequired() != DND5EArmor.getStrengthRequired()) return false;
        if (getStealth() != DND5EArmor.getStealth()) return false;
        if (getArmorID() != null ? !getArmorID().equals(DND5EArmor.getArmorID()) : DND5EArmor.getArmorID() != null) return false;
        return getCategory() != null ? getCategory().equals(DND5EArmor.getCategory()) : DND5EArmor.getCategory() == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getArmorID() != null ? getArmorID().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + getAC();
        result = 31 * result + getStrengthRequired();
        result = 31 * result + getStealth();
        return result;
    }
    @Override
    public String toString() {
        return "Armor{" +
                "armorID=" + armorID +
                ", category='" + category + '\'' +
                ", AC=" + AC +
                ", strengthRequired=" + strengthRequired +
                ", stealth=" + stealth +
                "} " + super.toString();
    }
}
