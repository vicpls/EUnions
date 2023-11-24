package netdesigntool.com.eunions.ui.screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.rating.KRatingBar
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.AboutAct

object AboutActSc : KScreen<AboutActSc>() {
    override val layoutId: Int?
        get() = null
    override val viewClass: Class<*>?
        get() = AboutAct::class.java

    val appLogo = KImageView { withId(R.id.imgLogoApp)}
    val shareFab = KButton { withId(R.id.fabShare)}
    val version = KTextView { withId(R.id.lblVersion)}
    val cardLicense = KView { withId(R.id.cardLicense)}
    val ratingBar = KRatingBar { withId(R.id.ratingBar)}
}