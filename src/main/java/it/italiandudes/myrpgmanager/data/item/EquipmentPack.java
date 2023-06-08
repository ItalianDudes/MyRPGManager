package it.italiandudes.myrpgmanager.data.item;

import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class EquipmentPack extends Item implements ISavable {

    // Attributes
    @Nullable private Integer equipmentPackID;
    @Nullable private String content;

    // Constructors
    public EquipmentPack() {
        super();
        setItemType(ItemTypes.TYPE_EQUIPMENT_PACK.getDatabaseValue());
    }
    public EquipmentPack(@NotNull final Item baseItem, @Nullable final Integer equipmentPackID,
                         @Nullable final String content) {
        super(baseItem);
        this.equipmentPackID = equipmentPackID;
        this.content = content;
    }
    public EquipmentPack(@NotNull final ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("item_id"));
        this.equipmentPackID = resultSet.getInt("id");
        try {
            this.content = resultSet.getString("content");
        } catch (SQLException e) {
            this.content = null;
        }
    }
    public EquipmentPack(@NotNull final String equipmentPackName) throws SQLException {
        super(equipmentPackName);
        String query = "SELECT * FROM equipment_packs WHERE item_id = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        Integer itemID = getItemID();
        assert itemID != null;
        ps.setInt(1, itemID);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.equipmentPackID = result.getInt("id");
            this.content = result.getString("content");
            ps.close();
        } else {
            ps.close();
            throw new SQLException("Exist the item, but not the equipment pack");
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        super.saveIntoDatabase(oldName);
        Integer itemID = getItemID();
        assert itemID != null;
        if (equipmentPackID == null) { // Insert
            String query = "INSERT INTO equipment_packs (item_id, content) VALUES (?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setString(2, getContent());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM equipment_packs WHERE item_id = ?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setEquipmentPackID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on equipment pack insert! Equipment pack insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE equipment_packs SET item_id=?, content=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setInt(1, itemID);
            ps.setString(2, getContent());
            ps.setInt(3, getEquipmentPackID());
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getEquipmentPackID() {
        return equipmentPackID;
    }
    public void setEquipmentPackID(final int equipmentPackID) {
        this.equipmentPackID = equipmentPackID;
    }
    @Nullable
    public String getContent() {
        return content;
    }
    public void setContent(@Nullable final String content) {
        this.content = content;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EquipmentPack)) return false;
        if (!super.equals(o)) return false;

        EquipmentPack that = (EquipmentPack) o;

        if (getEquipmentPackID() != null ? !getEquipmentPackID().equals(that.getEquipmentPackID()) : that.getEquipmentPackID() != null)
            return false;
        return getContent() != null ? getContent().equals(that.getContent()) : that.getContent() == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getEquipmentPackID() != null ? getEquipmentPackID().hashCode() : 0);
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "EquipmentPack{" +
                "equipmentPackID=" + equipmentPackID +
                ", content='" + content + '\'' +
                "} " + super.toString();
    }
}
