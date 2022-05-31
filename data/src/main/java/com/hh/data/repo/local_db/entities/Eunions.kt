package com.hh.data.repo.local_db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Eunions(
    @PrimaryKey @ColumnInfo(name = "_id") val id :Int,
    @ColumnInfo(name = "member")   val member: String,
    @ColumnInfo(name = "eu")       val eu: Int,
    @ColumnInfo(name = "schengen") val schengen :Int,
    @ColumnInfo(name = "name")   val name:   String
)