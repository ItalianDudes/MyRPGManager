package it.italiandudes.myrpgmanager.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Weapon extends Item {

    // Attributes
    @Nullable private Integer weaponID;
    @Nullable private String category;
    @Nullable private String damage;
    @Nullable private String properties;

    // Constructors
    public Weapon() {
        super();
    }
    public Weapon(@NotNull final Item baseItem, @Nullable final Integer weaponID,@Nullable final String category,
                  @Nullable final String damage, @Nullable final String properties) {
        super(baseItem);
        this.weaponID = weaponID;
        this.category = category;
        this.damage = damage;
        this.properties = properties;
    }
    public Weapon(@NotNull final ResultSet resultSet) throws SQLException {
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
    }

    // Methods
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
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Weapon)) return false;
        if (!super.equals(o)) return false;

        Weapon weapon = (Weapon) o;

        if (getWeaponID() != null ? !getWeaponID().equals(weapon.getWeaponID()) : weapon.getWeaponID() != null)
            return false;
        if (getCategory() != null ? !getCategory().equals(weapon.getCategory()) : weapon.getCategory() != null)
            return false;
        if (getDamage() != null ? !getDamage().equals(weapon.getDamage()) : weapon.getDamage() != null) return false;
        return getProperties() != null ? getProperties().equals(weapon.getProperties()) : weapon.getProperties() == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getWeaponID() != null ? getWeaponID().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getDamage() != null ? getDamage().hashCode() : 0);
        result = 31 * result + (getProperties() != null ? getProperties().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Weapon{" +
                "weaponID=" + weaponID +
                ", category='" + category + '\'' +
                ", damage='" + damage + '\'' +
                ", properties='" + properties + '\'' +
                "} " + super.toString();
    }
}
