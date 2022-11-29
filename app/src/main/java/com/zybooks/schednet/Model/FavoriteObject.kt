package com.zybooks.schednet.Model

import androidx.annotation.Nullable

class FavoriteObject(favoriteId: Int, @Nullable favoriteName: String?, @Nullable isPinned: Boolean?, type: Boolean) {

    var favoriteId: Int
    var favoriteName: String
    var isPinned: Boolean
    var favoriteType: Boolean

    init {
        this.favoriteId = favoriteId
        this.favoriteName = favoriteName ?: "NAN"
        this.isPinned = isPinned ?: false
        this.favoriteType = type
    }

    override fun toString(): String {
        return "$favoriteId:$favoriteName:$isPinned:$favoriteType"
    }
}