package com.ravi.exploremovie.screenRoutes

sealed class ScreenRoutes(val route: String) {
    object SplashScreen : ScreenRoutes("splashScreen")
    object OnboardingScreen : ScreenRoutes("onboardingScreen")
    object LoginScreen : ScreenRoutes("loginScreen")
    object OtpScreen : ScreenRoutes("otpScreen")
    object HomeScreen : ScreenRoutes("homeScreen")
    object DetailsScreen : ScreenRoutes("detailsScreen")
    object SearchScreen : ScreenRoutes("searchScreen")
    object SeeMoreScreen : ScreenRoutes("seeMoreScreen")
}
