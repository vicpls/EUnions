package com.hh.data.model

import com.hh.data.repo.local_db.entities.OtherCountry
import com.hh.data.repo.local_db.entities.ParticipialCountries

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
