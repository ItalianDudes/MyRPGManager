package it.italiandudes.myrpgmanager.data;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Spell extends Item {

    // Attributes
    @Nullable private Integer spellID;
    private int level;
    @Nullable private String type;
    @Nullable private String castTime;
    @Nullable private String range;
    @Nullable private String components;
    @Nullable private String duration;
    @Nullable private String damage;

    // Constructors
    public Spell() {
        super();
        level = 0;
    }
    public Spell(@NotNull final Item baseItem, @Nullable final Integer spellID,
                 final int level, @Nullable final String type, @Nullable final String castTime,
                 @Nullable final String range, @Nullable final String components,
                 @Nullable final String duration, @Nullable final String damage) {
        super(baseItem);
        this.spellID = spellID;
        if (level >= 0 && level <= 9) this.level = level;
        else this.level = 0;
        this.type = type;
        this.castTime = castTime;
        this.range = range;
        this.components = components;
        this.duration = duration;
        this.damage = damage;
    }
    public Spell(@NotNull final ResultSet resultSet) throws SQLException {
        super(resultSet.getInt("item_id"));
        this.spellID = resultSet.getInt("id");
        try {
            this.level = resultSet.getInt("level");
            if (this.level < 0 || this.level > 9) this.level = 0;
        } catch (SQLException e) {
            this.level = 0;
        }
        try {
            this.type = resultSet.getString("type");
        } catch (SQLException e) {
            this.type = null;
        }
        try {
            this.castTime = resultSet.getString("cast_time");
        } catch (SQLException e) {
            this.castTime = null;
        }
        try {
            this.range = resultSet.getString("spell_range");
        } catch (SQLException e) {
            this.range = null;
        }
        try {
            this.components = resultSet.getString("components");
        } catch (SQLException e) {
            this.components = null;
        }
        try {
            this.duration = resultSet.getString("duration");
        } catch (SQLException e) {
            this.duration = null;
        }
        try {
            this.damage = resultSet.getString("damage");
        } catch (SQLException e) {
            this.damage = null;
        }
    }

    // Methods
    @Nullable
    public Integer getSpellID() {
        return spellID;
    }
    public void setSpellID(final int spellID) {
        if (this.spellID == null) this.spellID = spellID;
    }
    public int getLevel() {
        return level;
    }
    public void setLevel(final int level) {
        if (level >= 0 && level <= 9) this.level = level;
    }
    @Nullable
    public String getType() {
        return type;
    }
    public void setType(@Nullable final String type) {
        this.type = type;
    }
    @Nullable
    public String getCastTime() {
        return castTime;
    }
    public void setCastTime(@Nullable final String castTime) {
        this.castTime = castTime;
    }
    @Nullable
    public String getRange() {
        return range;
    }
    public void setRange(@Nullable final String range) {
        this.range = range;
    }
    @Nullable
    public String getComponents() {
        return components;
    }
    public void setComponents(@Nullable final String components) {
        this.components = components;
    }
    @Nullable
    public String getDuration() {
        return duration;
    }
    public void setDuration(@Nullable final String duration) {
        this.duration = duration;
    }
    @Nullable
    public String getDamage() {
        return damage;
    }
    public void setDamage(@Nullable final String damage) {
        this.damage = damage;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Spell)) return false;
        if (!super.equals(o)) return false;

        Spell spell = (Spell) o;

        if (getLevel() != spell.getLevel()) return false;
        if (getSpellID() != null ? !getSpellID().equals(spell.getSpellID()) : spell.getSpellID() != null) return false;
        if (getType() != null ? !getType().equals(spell.getType()) : spell.getType() != null) return false;
        if (getCastTime() != null ? !getCastTime().equals(spell.getCastTime()) : spell.getCastTime() != null)
            return false;
        if (getRange() != null ? !getRange().equals(spell.getRange()) : spell.getRange() != null) return false;
        if (getComponents() != null ? !getComponents().equals(spell.getComponents()) : spell.getComponents() != null)
            return false;
        if (getDuration() != null ? !getDuration().equals(spell.getDuration()) : spell.getDuration() != null)
            return false;
        return getDamage() != null ? getDamage().equals(spell.getDamage()) : spell.getDamage() == null;
    }
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getSpellID() != null ? getSpellID().hashCode() : 0);
        result = 31 * result + getLevel();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getCastTime() != null ? getCastTime().hashCode() : 0);
        result = 31 * result + (getRange() != null ? getRange().hashCode() : 0);
        result = 31 * result + (getComponents() != null ? getComponents().hashCode() : 0);
        result = 31 * result + (getDuration() != null ? getDuration().hashCode() : 0);
        result = 31 * result + (getDamage() != null ? getDamage().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Spell{" +
                "spellID=" + spellID +
                ", level=" + level +
                ", type='" + type + '\'' +
                ", castTime='" + castTime + '\'' +
                ", range='" + range + '\'' +
                ", components='" + components + '\'' +
                ", duration='" + duration + '\'' +
                ", damage='" + damage + '\'' +
                "} " + super.toString();
    }
}