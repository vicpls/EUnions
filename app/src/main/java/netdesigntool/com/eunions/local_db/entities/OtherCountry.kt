package netdesigntool.com.eunions.local_db.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("""SELECT member, name FROM eunions 
                        WHERE schengen>1 AND eu>1 ORDER BY name""",
        "other_countries")

data class OtherCountry (
        @ColumnInfo(name = "member") val member: String,
        @ColumnInfo(name = "name")   val name:   String
)
