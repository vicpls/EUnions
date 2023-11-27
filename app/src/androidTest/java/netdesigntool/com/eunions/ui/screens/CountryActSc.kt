package netdesigntool.com.eunions.ui.screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.country.CountryAct

object CountryActSc : KScreen<CountryActSc>() {
    override val layoutId: Int?
        get() = R.layout.act_country
    override val viewClass: Class<*>?
        get() = CountryAct::class.java

    val flag = KImageView { withId(R.id.ivFlag)}
    val countryName = KView { withId(R.id.tvCountryName)}
}