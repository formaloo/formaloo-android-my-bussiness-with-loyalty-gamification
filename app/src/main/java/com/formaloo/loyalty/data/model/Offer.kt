package com.formaloo.loyalty.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Offer(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: String,
    var title: String?,
    var description: String?,
    var link: String?,
    var imageUrl: String?,
) : Serializable