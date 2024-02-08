package netdesigntool.com.eunions.ui.screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.rating.KRatingBar
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import netdesigntool.com.eunions.ui.AboutAct
import com.mcsoft.aboutactivity.R.id as aboutActRId

object AboutActSc : KScreen<AboutActSc>() {
    override val layoutId: Int?
        get() = null
    override val viewClass: Class<*>
        get() = AboutAct::class.java

    val appLogo = KImageView { withId(aboutActRId.imgLogoApp)}
    val shareFab = KButton { withId(aboutActRId.fabShare)}
    val version = KTextView { withId(aboutActRId.lblVersion)}
    val cardLicense = KView { withId(aboutActRId.cardLicense)}
    val ratingBar = KRatingBar { withId(aboutActRId.ratingBar)}
}