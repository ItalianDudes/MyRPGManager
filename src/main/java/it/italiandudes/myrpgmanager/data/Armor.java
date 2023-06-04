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
    private int AC;
    private int strengthRequired;
    private int stealth;

    // Constructors
    public Armor() {
        super();
        AC = 0;
        strengthRequired = 0;
        stealth = 0;
    }
    public Armor(@NotNull final Item baseItem, @Nullable final Integer armorID,
                 @Nullable final String category, final int AC,
                 final int strengthRequired, final int stealth) {
        super(baseItem);
        this.armorID = armorID;
        this.category = category;
        this.AC = Math.max(AC, 0);
        this.strengthRequired = Math.max(strengthRequired, 0);
        if (stealth >= -1 && stealth <= 1) this.stealth = stealth;
        else this.stealth = 0;
    }
    public Armor(@NotNull final ResultSet resultSet) throws SQLException {
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
        if (!(o instanceof Armor)) return false;
        if (!super.equals(o)) return false;

        Armor armor = (Armor) o;

        if (getAC() != armor.getAC()) return false;
        if (getStrengthRequired() != armor.getStrengthRequired()) return false;
        if (getStealth() != armor.getStealth()) return false;
        if (getArmorID() != null ? !getArmorID().equals(armor.getArmorID()) : armor.getArmorID() != null) return false;
        return getCategory() != null ? getCategory().equals(armor.getCategory()) : armor.getCategory() == null;
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
