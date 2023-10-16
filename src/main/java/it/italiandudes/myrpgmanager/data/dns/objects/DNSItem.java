package it.italiandudes.myrpgmanager.data.dns.objects;

import it.italiandudes.idl.common.Logger;
import it.italiandudes.myrpgmanager.data.common.Image64;
import it.italiandudes.myrpgmanager.data.common.Rarity;
import it.italiandudes.myrpgmanager.data.dns.DNSCategory;
import it.italiandudes.myrpgmanager.data.dns.DNSItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.sql.SQLException;

public class DNSItem extends DNSElement {

    // Attributes
    @Nullable private Integer itemID;
    @NotNull private DNSItemType itemType;

    // Constructors
    public DNSItem(
            @Nullable final Integer itemID, @NotNull final DNSItemType itemType, @Nullable final Integer elementID,
            @NotNull final String name, @NotNull final Rarity rarity, @Nullable final Image64 image,
            @Nullable final Integer costCopper, @Nullable final Double weight, @Nullable final String description
    ) {
        super(elementID, name, rarity, DNSCategory.ITEM, image, costCopper, weight, description);
        this.itemID = itemID;
        this.itemType = itemType;
    }
    public DNSItem(@NotNull final String name) throws SQLException {
        super(name, DNSCategory.ITEM);
        // TODO: DB Call
    }

    // Overridden Methods
    @Override
    public void saveIntoDatabase(@Nullable String oldName) throws SQLException {
        // TODO: implement saveIntoDatabase
    }
    @Override
    public JSONObject exportElementJSON() {
        // TODO: implement exportElementJSON
        return null;
    }
    @Override
    public String exportElement() {
        // TODO: implement exportElement
        return null;
    }
}
