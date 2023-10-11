package it.italiandudes.myrpgmanager.data.dnd5e.misc;

import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class DND5EBackground implements ISavable {

    // Attributes
    @Nullable private Integer backgroundID;
    @NotNull private String name;
    @Nullable private String description;

    // Constructors
    public DND5EBackground(@Nullable final Integer backgroundID, @NotNull final String name, @Nullable final String description) {
        this.backgroundID = backgroundID;
        this.name = name;
        this.description = description;
    }
    public DND5EBackground(@NotNull final String backgroundName) throws SQLException {
        String query = "SELECT * FROM backgrounds WHERE name = ?;";
        PreparedStatement ps = DBManager.preparedStatement(query);
        if (ps == null) throw new SQLException("The database is not connected");
        ps.setString(1, backgroundName);
        ResultSet result = ps.executeQuery();
        if (result.next()) {
            this.name = backgroundName;
            this.backgroundID = result.getInt("id");
            this.description = result.getString("description");
            ps.close();
        } else {
            ps.close();
            throw new SQLException("This background doesn't exist");
        }
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        Integer backgroundID = getBackgroundID();
        if (backgroundID == null) { // Insert
            String query = "INSERT INTO backgrounds (name, description) VALUES (?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ps.setString(2, getDescription());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM backgrounds WHERE name=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setBackgroundID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on background insert! Background insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE backgrounds SET name=?, description=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ps.setString(2, getDescription());
            ps.setInt(3, backgroundID);
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getBackgroundID() {
        return backgroundID;
    }
    public void setBackgroundID(final int backgroundID) {
        if (this.backgroundID == null) this.backgroundID = backgroundID;
    }
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(@NotNull final String name) {
        this.name = name;
    }
    @Nullable
    public String getDescription() {
        return description;
    }
    public void setDescription(@Nullable final String description) {
        this.description = description;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DND5EBackground)) return false;

        DND5EBackground that = (DND5EBackground) o;

        if (getBackgroundID() != null ? !getBackgroundID().equals(that.getBackgroundID()) : that.getBackgroundID() != null)
            return false;
        if (!getName().equals(that.getName())) return false;
        return getDescription() != null ? getDescription().equals(that.getDescription()) : that.getDescription() == null;
    }
    @Override
    public int hashCode() {
        int result = getBackgroundID() != null ? getBackgroundID().hashCode() : 0;
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Background{" +
                "backgroundID=" + backgroundID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
