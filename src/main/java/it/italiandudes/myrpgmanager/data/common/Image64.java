package it.italiandudes.myrpgmanager.data.common;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class Image64 {

    // Attributes
    @NotNull private final String imageBase64;
    @NotNull private final String imageExtension;

    // Constructors
    public Image64(@NotNull final String imageBase64, @NotNull final String imageExtension) {
        this.imageBase64 = imageBase64;
        this.imageExtension = imageExtension;
    }

    // Methods
    @NotNull
    public String getImageBase64() {
        return imageBase64;
    }
    @NotNull
    public String getImageExtension() {
        return imageExtension;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image64)) return false;
        Image64 image64 = (Image64) o;
        return Objects.equals(getImageBase64(), image64.getImageBase64()) && Objects.equals(getImageExtension(), image64.getImageExtension());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getImageBase64(), getImageExtension());
    }
}
