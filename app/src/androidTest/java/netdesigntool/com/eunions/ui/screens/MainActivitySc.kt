package netdesigntool.com.eunions.ui.screens

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import io.github.kakaocup.kakao.toolbar.KToolbar
import netdesigntool.com.eunions.R
import netdesigntool.com.eunions.ui.main.MainActivity

object MainActivitySc : KScreen<MainActivitySc>()  {
    override val layoutId: Int?
        get() = R.layout.act_main
    override val viewClass: Class<*>?
        get() = MainActivity::class.java

    val menuAbout = KToolbar { withId(R.id.about) }

    val EUFlag = KTextView { withId(R.id.tvLeftCap) }
    val SchengenFlag = KTextView { withId(R.id.tvRightCap) }
    val fab = KButton { withId(R.id.fabOthers) }

    val middleFlexBox = KFlexBox { withId(R.id.flexboxMiddle) }
}

class KFlexBox(function: ViewBuilder.() -> Unit) : KBaseView<KFlexBox>(function) {
}