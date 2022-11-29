package com.zybooks.schednet.Model

import androidx.annotation.Nullable

class FavoriteDataObject(favoriteId: Int, @Nullable favoriteName: String?, @Nullable eventName: String?, @Nullable eventDescription: String?, @Nullable isPinned: Boolean?) {

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