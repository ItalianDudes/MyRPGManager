package it.italiandudes.myrpgmanager.data.item;

@SuppressWarnings("unused")
public enum ItemTypes {
    TYPE_ITEM(0),
    TYPE_ARMOR(1),
    TYPE_WEAPON(2),
    TYPE_SPELL(3),
    TYPE_EQUIPMENT_PACK(4);

    // Attributes
    private final int databaseValue;

    // Constructors
    ItemTypes(final int databaseValue) {
        this.databaseValue = databaseValue;
    }

    // Methods
    public int getDatabaseValue() {
        return databaseValue;
    }
}
