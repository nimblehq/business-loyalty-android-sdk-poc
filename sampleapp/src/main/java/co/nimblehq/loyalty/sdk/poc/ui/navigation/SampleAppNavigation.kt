package co.nimblehq.loyalty.sdk.poc.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import co.nimblehq.loyalty.sdk.poc.ui.screen.detail.RewardDetailScreen
import co.nimblehq.loyalty.sdk.poc.ui.screen.history.RewardHistoryScreen
import co.nimblehq.loyalty.sdk.poc.ui.screen.home.HomeScreen
import co.nimblehq.loyalty.sdk.poc.ui.screen.products.ProductListScreen
import co.nimblehq.loyalty.sdk.poc.ui.screen.products.detail.ProductDetailScreen
import co.nimblehq.loyalty.sdk.poc.ui.screen.rewards.RewardListScreen

private const val NAVIGATION_ARGUMENT_REWARD_ID = "reward_id"
private const val NAVIGATION_ARGUMENT_PRODUCT_ID = "product_id"

private const val NAVIGATION_ROUTE_HOME = "home"
private const val NAVIGATION_ROUTE_REWARD_LIST = "reward_list"
private const val NAVIGATION_ROUTE_REWARD_HISTORY = "reward_history"
private const val NAVIGATION_ROUTE_REWARD_DETAILS = "reward_details"
private const val NAVIGATION_ROUTE_PRODUCT_LIST = "product_list"
private const val NAVIGATION_ROUTE_PRODUCT_DETAILS = "product_details"

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
                onNavigateToProductList = {
                    navController.navigate(NAVIGATION_ROUTE_PRODUCT_LIST)
                }
            )
        }
        composable(NAVIGATION_ROUTE_REWARD_LIST) {
            RewardListScreen(
                onNavigateRewardDetail = { rewardId ->
                    navController.navigate(
                        "$NAVIGATION_ROUTE_REWARD_DETAILS/$rewardId",
                    )
                },
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
        composable(
            route = "$NAVIGATION_ROUTE_REWARD_DETAILS/{$NAVIGATION_ARGUMENT_REWARD_ID}",
            arguments = listOf(
                navArgument(NAVIGATION_ARGUMENT_REWARD_ID) { type = NavType.StringType }
            )) { backStackEntry ->
            RewardDetailScreen(
                rewardId = backStackEntry.arguments?.getString(NAVIGATION_ARGUMENT_REWARD_ID)
                    .orEmpty(),
                onNavigateBack = {
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(NAVIGATION_ROUTE_PRODUCT_LIST) {
            ProductListScreen(
                onNavigateProductDetail = { productId ->
                    navController.navigate(
                        "$NAVIGATION_ROUTE_PRODUCT_DETAILS/$productId",
                    )
                },
                onNavigateBack = {
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        composable(
            route = "$NAVIGATION_ROUTE_PRODUCT_DETAILS/{$NAVIGATION_ARGUMENT_PRODUCT_ID}",
            arguments = listOf(
                navArgument(NAVIGATION_ARGUMENT_PRODUCT_ID) { type = NavType.StringType }
            )) { backStackEntry ->
            ProductDetailScreen(
                productId = backStackEntry.arguments?.getString(NAVIGATION_ARGUMENT_PRODUCT_ID)
                    .orEmpty(),
                onNavigateBack = {
                    navController.navigateUp()
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        // TODO: Add more destinations
    }
}