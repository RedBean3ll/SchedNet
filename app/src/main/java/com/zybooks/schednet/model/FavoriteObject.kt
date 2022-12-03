package com.zybooks.schednet.model

import androidx.annotation.NonNull
import androidx.annotation.Nullable

/**
 *  File: FavoriteObject.kt
 *  @author Matthew Clark
 */

class FavoriteObject(@NonNull favoriteId: Int, @Nullable favoriteName: String?, @Nullable isPinned: Boolean?, @NonNull type: Boolean) {

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