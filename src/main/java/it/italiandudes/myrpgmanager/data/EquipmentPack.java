package it.italiandudes.myrpgmanager.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class EquipmentPack extends Item {

    // Attributes
    @Nullable private Integer equipmentPackID;
    @Nullable private String content;

    // Constructors
    public EquipmentPack() {
        super();
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

    // Methods
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
