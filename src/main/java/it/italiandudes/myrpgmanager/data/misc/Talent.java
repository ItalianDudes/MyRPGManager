package it.italiandudes.myrpgmanager.data.misc;

import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Talent implements ISavable {

    // Attributes
    @Nullable private Integer talentID;
    @NotNull private String name;
    @Nullable private String requirements;
    @Nullable private String description;
    @Nullable private String privileges;

    // Constructors
    public Talent(@Nullable final Integer talentID, @NotNull final String name, @Nullable final String requirements,
                  @Nullable final String description, @Nullable final String privileges) {
        this.talentID = talentID;
        this.name = name;
        this.requirements = requirements;
        this.description = description;
        this.privileges = privileges;
    }
    public Talent(@NotNull final String talentName) throws SQLException {
        String query = "SELECT * FROM talents WHERE name = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        ps.setString(1, talentName);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.name = talentName;
            this.talentID = result.getInt("id");
            this.requirements = result.getString("requirements");
            this.description = result.getString("description");
            this.privileges = result.getString("privileges");
            ps.close();
        } else {
            ps.close();
            throw new SQLException("This talent doesn't exist");
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        Integer talentID = getTalentID();
        if (talentID == null) { // Insert
            String query = "INSERT INTO talents (name, requirements, description, privileges) VALUES (?, ?, ?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ps.setString(2, getRequirements());
            ps.setString(3, getDescription());
            ps.setString(4, getPrivileges());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM talents WHERE name=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setTalentID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on talent insert! Talent insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE talents SET name=?, requirements=?, description=?, privileges=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ps.setString(2, getRequirements());
            ps.setString(3, getDescription());
            ps.setString(4, getPrivileges());
            ps.setInt(5, talentID);
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getTalentID() {
        return talentID;
    }
    public void setTalentID(final int talentID) {
        if (this.talentID == null) this.talentID = talentID;
    }
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(@NotNull final String name) {
        this.name = name;
    }
    @Nullable
    public String getRequirements() {
        return requirements;
    }
    public void setRequirements(@Nullable final String requirements) {
        this.requirements = requirements;
    }
    @Nullable
    public String getDescription() {
        return description;
    }
    public void setDescription(@Nullable final String description) {
        this.description = description;
    }
    @Nullable
    public String getPrivileges() {
        return privileges;
    }
    public void setPrivileges(@Nullable final String privileges) {
        this.privileges = privileges;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Talent)) return false;

        Talent talent = (Talent) o;

        if (getTalentID() != null ? !getTalentID().equals(talent.getTalentID()) : talent.getTalentID() != null)
            return false;
        if (!getName().equals(talent.getName())) return false;
        if (getRequirements() != null ? !getRequirements().equals(talent.getRequirements()) : talent.getRequirements() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(talent.getDescription()) : talent.getDescription() != null)
            return false;
        return getPrivileges() != null ? getPrivileges().equals(talent.getPrivileges()) : talent.getPrivileges() == null;
    }
    @Override
    public int hashCode() {
        int result = getTalentID() != null ? getTalentID().hashCode() : 0;
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getRequirements() != null ? getRequirements().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getPrivileges() != null ? getPrivileges().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Talent{" +
                "talentID=" + talentID +
                ", name='" + name + '\'' +
                ", requirements='" + requirements + '\'' +
                ", description='" + description + '\'' +
                ", privileges='" + privileges + '\'' +
                '}';
    }
}
