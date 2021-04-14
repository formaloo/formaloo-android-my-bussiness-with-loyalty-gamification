package com.formaloo.loyalty.data.model.customer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Customer(
    @PrimaryKey @ColumnInfo(name = "code")
    val code: String,
    var full_name: String? = null,
    var phone_number: String? = null,
    var email: String? = null,
    var score: Int? = null
) : Serializable
