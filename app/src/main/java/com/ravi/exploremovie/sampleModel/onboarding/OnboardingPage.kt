package com.ravi.exploremovie.sampleModel.onboarding

import com.ravi.exploremovie.R

data class OnboardingPage(
    val imageRes: Int,
    val title: String,
    val description: String
)

val pages = listOf(
    OnboardingPage(R.drawable.onboarding_v2, "The biggest international and local film streaming", "Semper in cursus magna et eu varius nunc adipiscing. Elementum justo, laoreet id sem semper parturient. "),
    OnboardingPage(R.drawable.onboarding_v3, "Offers ad-free viewing of high quality", "Semper in cursus magna et eu varius nunc adipiscing. Elementum justo, laoreet id sem semper parturient. "),
    OnboardingPage(R.drawable.onboarding_v2, "Our service brings together your favorite series", "Semper in cursus magna et eu varius nunc adipiscing. Elementum justo, laoreet id sem semper parturient. ")
)
