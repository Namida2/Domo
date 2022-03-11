package com.example.core.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "employee")
data class Employee (
    @PrimaryKey var email: String,
    var name: String,
    var post: String,
    var password: String,
    var permission: Boolean = false
) {
    constructor() : this("", "", "", "")
}