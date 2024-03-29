package com.hh.data.repo.local_db.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.hh.data.model.BaseCountry

@DatabaseView("SELECT member, name FROM eunions "+
                        "WHERE schengen>1 AND eu>1 ORDER BY name",
        viewName = "other_countries")

data class OtherCountry (
        @ColumnInfo(name = "member") override val iso: String,
        @ColumnInfo(name = "name")   override val name:   String
) : BaseCountry()
