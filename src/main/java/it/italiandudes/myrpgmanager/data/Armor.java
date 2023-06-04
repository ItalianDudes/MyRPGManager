package it.italiandudes.myrpgmanager.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Armor extends Item {

    // Attributes
    @Nullable private Integer armorID;
    @Nullable private String category;
    @Nullable private Integer AC;
    @Nullable private Integer strengthRequired;
    @Nullable private Integer stealth;

    // Constructors
    public Armor() {
        super();
    }
    public Armor(@NotNull final Item item, @Nullable final Integer armorID,
                 @Nullable final String category, @Nullable final Integer AC,
                 @Nullable final Integer strengthRequired, @Nullable final Integer stealth) {
        super(item);
        this.armorID = armorID;
        this.category = category;
        this.AC = AC;
        this.strengthRequired = strengthRequired;
        this.stealth = stealth;
    }
    public Armor(@NotNull final ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("item_id"));
        this.armorID = resultSet.getInt("id");
        this.category = resultSet.getString("category");
        this.AC = resultSet.getInt("ac");
        this.strengthRequired = resultSet.getInt("strength_required");
        this.stealth = resultSet.getInt("stealth");
    }

    // Methods
    @Nullable
    public Integer getArmorID() {
        return armorID;
    }
    public void setArmorID(@NotNull Integer armorID) {
        if (this.armorID == null) this.armorID = armorID;
    }
    @Nullable
    public String getCategory() {
        return category;
    }
    public void setCategory(@Nullable final String category) {
        this.category = category;
    }
    @Nullable
    public Integer getAC() {
        return AC;
    }
    public void setAC(@Nullable final Integer AC) {
        this.AC = AC;
    }
    @Nullable
    public Integer getStrengthRequired() {
        return strengthRequired;
    }
    public void setStrengthRequired(@Nullable final Integer strengthRequired) {
        this.strengthRequired = strengthRequired;
    }
    @Nullable
    public Integer getStealth() {
        return stealth;
    }
    public void setStealth(@Nullable final Integer stealth) {
        this.stealth = stealth;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Armor)) return false;
        if (!super.equals(o)) return false;

        Armor armor = (Armor) o;

        if (getArmorID() != null ? !getArmorID().equals(armor.getArmorID()) : armor.getArmorID() != null) return false;
        if (getCategory() != null ? !getCategory().equals(armor.getCategory()) : armor.getCategory() != null)
            return false;
        if (getAC() != null ? !getAC().equals(armor.getAC()) : armor.getAC() != null) return false;
        if (getStrengthRequired() != null ? !getStrengthRequired().equals(armor.getStrengthRequired()) : armor.getStrengthRequired() != null)
            return false;
        return getStealth() != null ? getStealth().equals(armor.getStealth()) : armor.getStealth() == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getArmorID() != null ? getArmorID().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getAC() != null ? getAC().hashCode() : 0);
        result = 31 * result + (getStrengthRequired() != null ? getStrengthRequired().hashCode() : 0);
        result = 31 * result + (getStealth() != null ? getStealth().hashCode() : 0);
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
