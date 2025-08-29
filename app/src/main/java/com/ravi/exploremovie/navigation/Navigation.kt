package com.ravi.exploremovie.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ravi.exploremovie.screenRoutes.ScreenRoutes
import com.ravi.exploremovie.screens.SplashScreen
import com.ravi.exploremovie.screens.auth.LoginScreen
import com.ravi.exploremovie.screens.auth.OtpVerifyScreen
import com.ravi.exploremovie.screens.details.MovieDetailsScreen
import com.ravi.exploremovie.screens.details.SeeMoreScreen
import com.ravi.exploremovie.screens.downloads.DownloadsScreen
import com.ravi.exploremovie.screens.home.HomeScreen
import com.ravi.exploremovie.screens.onboarding.OnboardingScreen
import com.ravi.exploremovie.screens.player.youtube.YoutubePlayerScreen
import com.ravi.exploremovie.screens.search.SearchScreen
import com.ravi.exploremovie.screens.player.exoplayer.PlayerScreen


@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ScreenRoutes.SplashScreen.route) {

        composable(route = ScreenRoutes.SplashScreen.route) {
            SplashScreen(navController)
        }
        composable(route = ScreenRoutes.OnboardingScreen.route) {
            OnboardingScreen(navController)
        }
        composable(route = ScreenRoutes.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(route = ScreenRoutes.LoginScreen.route) {
            LoginScreen(navController)
        }

        composable(route = ScreenRoutes.OtpScreen.route) {
            OtpVerifyScreen(navController)
        }
        
        composable(route = ScreenRoutes.SearchScreen.route) {
            SearchScreen(navController)
        }

        composable (route = ScreenRoutes.DownloadScreen.route) {
            DownloadsScreen(navController)
        }
        
        composable(
            route = "${ScreenRoutes.DetailsScreen.route}/{movieId}",
            arguments = listOf(
                navArgument("movieId") { 
                    type = NavType.IntType 
                }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: -1
            MovieDetailsScreen(navController = navController, movieId = movieId)
        }

        composable(
            route = "${ScreenRoutes.SeeMoreScreen.route}/{contentType}/{title}",
            arguments = listOf(
                navArgument("contentType") { 
                    type = NavType.StringType 
                },
                navArgument("title") { 
                    type = NavType.StringType 
                }
            )
        ) { backStackEntry ->
            val contentType = backStackEntry.arguments?.getString("contentType") ?: "popular"
            val title = backStackEntry.arguments?.getString("title") ?: "See More"
            SeeMoreScreen(
                navController = navController, 
                contentType = contentType,
                title = title
            )
        }

        composable(
            route = "${ScreenRoutes.YoutubePlayerScreen.route}/{videoId}",
            arguments = listOf(
                navArgument("videoId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
            YoutubePlayerScreen(
                videoId = videoId,
                onBackPressed = { navController.popBackStack() }
            )
        }
        composable(
            route = "${ScreenRoutes.PlayerScreen}player/{videoUri}",
            arguments = listOf(navArgument("videoUri") { type = NavType.StringType })
        ) { backStackEntry ->
            val uriString = backStackEntry.arguments?.getString("videoUri") ?: return@composable
            val videoUri = Uri.parse(uriString)

            PlayerScreen(
                videoUri = videoUri,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
