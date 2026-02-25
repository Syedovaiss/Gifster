package com.ovais.gifster.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.retain.retain
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.ovais.gifster.features.home.presentation.HomeScreen
import com.ovais.gifster.features.search.presentation.SearchScreen

@Composable
fun Gifster() {
    val backStack = retain { mutableStateListOf<Routes>(Routes.Home) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<Routes.Home> { HomeScreen(
                onDetails = {},
                onRandom = {},
                onSearch = {
                    backStack.add(Routes.Search)
                }
            ) }
            entry<Routes.Search> { SearchScreen {  } }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
    )
}