package co.nimblehq.loyalty.sdk.poc.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.nimblehq.loyalty.sdk.poc.ui.screen.history.RewardHistoryScreen
import co.nimblehq.loyalty.sdk.poc.ui.screen.home.HomeScreen
import co.nimblehq.loyalty.sdk.poc.ui.screen.rewards.RewardListScreen

private const val NAVIGATION_ROUTE_HOME = "home"
private const val NAVIGATION_ROUTE_REWARD_LIST = "reward_list"
private const val NAVIGATION_ROUTE_REWARD_HISTORY = "reward_history"

@Composable
fun SampleAppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NAVIGATION_ROUTE_HOME) {
        composable(NAVIGATION_ROUTE_HOME) {
            HomeScreen(
                modifier = Modifier.fillMaxSize(),
                onNavigateToRewardList = {
                    navController.navigate(NAVIGATION_ROUTE_REWARD_LIST)
                },
                onNavigateToRewardHistory = {
                    navController.navigate(NAVIGATION_ROUTE_REWARD_HISTORY)
                },
            )
        }
        composable(NAVIGATION_ROUTE_REWARD_LIST) {
            RewardListScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(NAVIGATION_ROUTE_REWARD_HISTORY) {
            RewardHistoryScreen(
                onNavigateBack = {
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        // TODO: Add more destinations
    }
}