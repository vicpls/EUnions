package netdesigntool.com.eunions.local_db.entities

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

@DatabaseView("""SELECT _id,schengen,member,eu, name FROM eunions 
                WHERE schengen<=1 OR eu<=1 ORDER BY case 
                    WHEN ScHENGEN<=1 AND eu>1 THEN 1 
                    WHEN schengen<=1 AND eu<=1 THEN 2 
                    WHEN schengen>1 AND eu<=1 THEN 3 END, name""",
    "participial_countries")
data class ParticipialCountries (
    @ColumnInfo(name = "_id")      val id :Int,
    @ColumnInfo(name = "schengen") val schengen :Int,
    @ColumnInfo(name = "member")   val member: String,
    @ColumnInfo(name = "eu")       val eu: Int,
    @ColumnInfo(name = "name")     val name:   String
)