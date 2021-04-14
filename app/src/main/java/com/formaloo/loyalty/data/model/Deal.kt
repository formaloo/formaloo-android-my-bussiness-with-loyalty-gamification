package com.formaloo.loyalty.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Deal(
    @PrimaryKey @ColumnInfo(name = "id")
    var id: String,
    var title: String?,
    var description: String?,
    var score_needed: Int?,
    var link: String?,
    var imageUrl: String?,
) : Serializable