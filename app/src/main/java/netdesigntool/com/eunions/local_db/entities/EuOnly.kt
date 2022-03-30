package netdesigntool.com.eunions.local_db.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView(
    "SELECT schengen,member,eu FROM eunions WHERE schengen>1 AND eu<=1 ORDER BY name"
    , "eu_only")
data class EuOnly (
    @ColumnInfo(name = "schengen") val schengen :Int,
    @ColumnInfo(name = "member")   val member: Int,
    @ColumnInfo(name = "eu")       val eu: Int
)