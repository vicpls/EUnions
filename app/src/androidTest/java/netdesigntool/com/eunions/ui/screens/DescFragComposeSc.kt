package netdesigntool.com.eunions.ui.screens

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.builder.NodeMatcher
import io.github.kakaocup.compose.node.core.BaseNode
import io.github.kakaocup.compose.node.element.KNode
import io.github.kakaocup.kakao.common.utilities.getResourceString
import netdesigntool.com.eunions.R

class DescFragComposeSc (
    semanticsProvider: SemanticsNodeInteractionsProvider,
    nodeMatcher: NodeMatcher = NodeMatcher(
        matcher = SemanticsMatcher(
            description = "Empty matcher",
            matcher = { true }
        )
    )
) : BaseNode<DescFragComposeSc>(semanticsProvider, nodeMatcher) {

    val euDesc: KNode = child {
        //hasTestTag(ComposeTestTags.banner.title)
        hasText(getResourceString(R.string.eu_desc))
    }

    val schenDesc: KNode = child {
        hasText(getResourceString(R.string.schengen_desc))
    }
}