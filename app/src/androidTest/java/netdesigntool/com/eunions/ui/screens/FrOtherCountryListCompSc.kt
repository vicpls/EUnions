package netdesigntool.com.eunions.ui.screens

import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import io.github.kakaocup.compose.node.builder.NodeMatcher
import io.github.kakaocup.compose.node.core.BaseNode
import io.github.kakaocup.compose.node.element.KNode
import netdesigntool.com.eunions.ui.othcountries.ComposeTestTag_CountryList

class FrOtherCountryListCompSc(
    semanticsProvider: SemanticsNodeInteractionsProvider,
    nodeMatcher: NodeMatcher = NodeMatcher(
        matcher = SemanticsMatcher(
            description = "Empty matcher",
            matcher = { true }
        )
    )
) : BaseNode<DescFragComposeSc>(semanticsProvider, nodeMatcher) {

    val countryList : KNode = child { hasTestTag(ComposeTestTag_CountryList) }
}