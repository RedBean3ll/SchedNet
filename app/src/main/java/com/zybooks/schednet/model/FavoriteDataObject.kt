package com.zybooks.schednet.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 *  File: FavoriteDataObject.kt
 *  @author Matthew Clark
 */

class FavoriteDataObject(@NonNull favoriteId: Int, @Nullable favoriteName: String?, @Nullable eventName: String?, @Nullable eventDescription: String?, @Nullable isPinned: Boolean?) {

    var favoriteId: Int
    var favoriteName: String
    var eventName: String
    var eventDescription: String
    var isPinned: Boolean

    init {
        this.favoriteId = favoriteId
        this.favoriteName = favoriteName ?: "NAN"
        this.eventName = eventName ?: "NAN"
        this.eventDescription = eventDescription ?: ""
        this.isPinned = isPinned ?: false
    }
}