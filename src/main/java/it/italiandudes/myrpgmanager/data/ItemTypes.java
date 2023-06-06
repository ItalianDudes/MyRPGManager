package it.italiandudes.myrpgmanager.data;

public enum ItemTypes {
    TYPE_ITEM(0),
    TYPE_ARMOR(1);

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
