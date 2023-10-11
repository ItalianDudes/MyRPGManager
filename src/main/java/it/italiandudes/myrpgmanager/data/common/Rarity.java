package it.italiandudes.myrpgmanager.data.common;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

@SuppressWarnings("unused")
public enum Rarity {
    COMMON("Comune", new Color(0.88,0.88,0.88,1)),
    UNCOMMON("Non Comune", new Color(0.13333333333333333333333333333333, 0.63921568627450980392156862745098,0.09411764705882352941176470588235,1)),
    RARE("Raro", new Color(0.03529411764705882352941176470588,0.54901960784313725490196078431373,0.87058823529411764705882352941176,1)),
    VERY_RARE("Molto Raro", new Color(0.15490196078431372549019607843137, 0.36862745098039215686274509803922, 0.92156862745098039215686274509804, 1)),
    LEGENDARY("Leggendario", new Color(0.7078431372549019607843137254902, 0.06666666666666666666666666666667, 0.67058823529411764705882352941176, 1)),
    EXOTIC("Esotico", new Color(0.90980392156862745098039215686275, 0.89411764705882352941176470588235, 0.03529411764705882352941176470588, 1));

    // Attributes
    @NotNull public static final ArrayList<String> colorNames = new ArrayList<>();
    static {
        for (Rarity rarity : Rarity.values()) {
            colorNames.add(rarity.textedRarity);
        }
    }
    @NotNull private final String textedRarity;
    @NotNull private final Color color;

    // Constructors
    Rarity(@NotNull final String textedRarity, @NotNull final Color color) {
        this.textedRarity = textedRarity;
        this.color = color;
    }

    // Methods
    @NotNull
    public String getTextedRarity() {
        return textedRarity;
    }
    @NotNull
    public Color getColor() {
        return color;
    }
}
