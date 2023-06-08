package it.italiandudes.myrpgmanager.data.misc;

import it.italiandudes.myrpgmanager.db.DBManager;
import it.italiandudes.myrpgmanager.interfaces.ISavable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
public final class Language implements ISavable {

    // Attributes
    @Nullable private Integer languageID;
    @NotNull private String name;
    @Nullable private String alphabet;
    @Nullable private String lore;

    // Constructors
    public Language(@Nullable final Integer languageID, @NotNull final String name, @Nullable final String alphabet,
                    @Nullable final String lore) {
        this.languageID = languageID;
        this.name = name;
        this.alphabet = alphabet;
        this.lore = lore;
    }

    // Methods
    @Override
    public void saveIntoDatabase(@Nullable final String oldName) throws SQLException {
        Integer languageID = getLanguageID();
        if (languageID == null) { // Insert
            String query = "INSERT INTO languages (name, alphabet, lore) VALUES (?, ?, ?);";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ps.setString(2, getAlphabet());
            ps.setString(3, getLore());
            ps.executeUpdate();
            ps.close();
            query = "SELECT id FROM languages WHERE name=?;";
            ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                setLanguageID(resultSet.getInt("id"));
                ps.close();
            } else {
                ps.close();
                throw new SQLException("Something strange happened on language insert! Language insert but doesn't result on select");
            }
        } else { // Update
            String query = "UPDATE languages SET name=?, alphabet=?, lore=? WHERE id=?;";
            PreparedStatement ps = DBManager.preparedStatement(query);
            if (ps == null) throw new SQLException("The database is not connected");
            ps.setString(1, getName());
            ps.setString(2, getAlphabet());
            ps.setString(3, getLore());
            ps.setInt(4, languageID);
            ps.executeUpdate();
            ps.close();
        }
    }
    @Nullable
    public Integer getLanguageID() {
        return languageID;
    }
    public void setLanguageID(final int languageID) {
        if (this.languageID == null) this.languageID = languageID;
    }
    @NotNull
    public String getName() {
        return name;
    }
    public void setName(@NotNull final String name) {
        this.name = name;
    }
    @Nullable
    public String getAlphabet() {
        return alphabet;
    }
    public void setAlphabet(@Nullable final String alphabet) {
        this.alphabet = alphabet;
    }
    @Nullable
    public String getLore() {
        return lore;
    }
    public void setLore(@Nullable final String lore) {
        this.lore = lore;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Language)) return false;

        Language language = (Language) o;

        if (getLanguageID() != null ? !getLanguageID().equals(language.getLanguageID()) : language.getLanguageID() != null)
            return false;
        if (!getName().equals(language.getName())) return false;
        if (getAlphabet() != null ? !getAlphabet().equals(language.getAlphabet()) : language.getAlphabet() != null)
            return false;
        return getLore() != null ? getLore().equals(language.getLore()) : language.getLore() == null;
    }
    @Override
    public int hashCode() {
        int result = getLanguageID() != null ? getLanguageID().hashCode() : 0;
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getAlphabet() != null ? getAlphabet().hashCode() : 0);
        result = 31 * result + (getLore() != null ? getLore().hashCode() : 0);
        return result;
    }
    @Override
    public String toString() {
        return "Language{" +
                "languageID=" + languageID +
                ", name='" + name + '\'' +
                ", alphabet='" + alphabet + '\'' +
                ", lore='" + lore + '\'' +
                '}';
    }
}
