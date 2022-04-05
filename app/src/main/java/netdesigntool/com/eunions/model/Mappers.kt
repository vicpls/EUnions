package netdesigntool.com.eunions.model

import netdesigntool.com.eunions.local_db.entities.OtherCountry
import netdesigntool.com.eunions.local_db.entities.ParticipialCountries

fun ParticipialCountries.toCountry() = Country(
    iso = member,
    euni = eu,
    schen = schengen,
    name = name
)

fun OtherCountry.toCountry() = Country(
    iso = iso,
    euni = 1,
    schen = 1,
    name = name
)
