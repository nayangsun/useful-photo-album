package com.example.useful_photo_album.data.temp.firebase.auth;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public interface UserInfo {
    @NonNull
    String getUid();

    @NonNull
    String getProviderId();

    @Nullable
    String getDisplayName();

    @Nullable
    Uri getPhotoUrl();

    @Nullable
    String getEmail();

    @Nullable
    String getPhoneNumber();

    boolean isEmailVerified();
}
