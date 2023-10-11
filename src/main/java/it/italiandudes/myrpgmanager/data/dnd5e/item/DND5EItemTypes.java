package it.italiandudes.myrpgmanager.data.dnd5e.item;

@SuppressWarnings("unused")
public enum DND5EItemTypes {
    TYPE_ITEM(0),
    TYPE_ARMOR(1),
    TYPE_WEAPON(2),
    TYPE_SPELL(3),
    TYPE_EQUIPMENT_PACK(4);

    // Attributes
    private final int databaseValue;

    // Constructors
    DND5EItemTypes(final int databaseValue) {
        this.databaseValue = databaseValue;
    }

    // Methods
    public int getDatabaseValue() {
        return databaseValue;
    }
}
