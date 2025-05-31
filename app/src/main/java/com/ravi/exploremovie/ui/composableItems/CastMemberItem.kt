package com.ravi.exploremovie.ui.composableItems

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.ravi.exploremovie.R
import com.ravi.exploremovie.common.movie.model.PersonResult
import com.ravi.exploremovie.movieDetails.data.model.Cast
import com.ravi.exploremovie.sampleModel.movie.Actor
import com.ravi.exploremovie.ui.theme.TextPrimary
import com.ravi.exploremovie.ui.theme.TextSecondary
import com.ravi.exploremovie.utils.ConstantUtils

/**
 * A universal cast member item that can display different types of cast/actor data.
 * 
 * @param modifier Modifier to be applied to the composable
 * @param name The name of the cast member
 * @param role The character/role name (optional)
 * @param profilePath The path to the profile image
 * @param imageSize The size of the profile image
 * @param onClick Click handler for the item
 * @param useGlide Whether to use Glide for image loading (default: false)
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CastMemberItem(
    modifier: Modifier = Modifier,
    name: String,
    role: String? = null,
    profilePath: String? = null,
    localImageRes: Int? = null,
    imageSize: Int = 70,
    onClick: (() -> Unit)? = null,
    useGlide: Boolean = false
) {
    val columnModifier = if (onClick != null) {
        modifier.clickable(onClick = onClick)
    } else {
        modifier
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = columnModifier.width(80.dp)
    ) {
        // Profile image
        Box(
            modifier = Modifier
                .size(imageSize.dp)
                .clip(CircleShape)
                .background(Color(0xFF1D1D35))
        ) {
            when {
                profilePath != null && useGlide -> {
                    // Use Glide for image loading
                    val imageUrl = "${ConstantUtils.BASE_URL_IMAGE}$profilePath"
                    GlideImage(
                        model = imageUrl,
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        loading = placeholder(R.drawable.profile),
                        failure = placeholder(R.drawable.profile)
                    )
                }
                profilePath != null -> {
                    // Use Coil for image loading
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("${ConstantUtils.BASE_URL_IMAGE}$profilePath")
                            .crossfade(true)
                            .build(),
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        error = painterResource(id = R.drawable.profile)
                    )
                }
                localImageRes != null -> {
                    // Use local resource
                    Image(
                        painter = painterResource(id = localImageRes),
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                else -> {
                    // Fallback
                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Name
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        
        // Role/Character (if provided)
        if (!role.isNullOrBlank()) {
            Text(
                text = role,
                fontSize = 12.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Extension function to create a CastMemberItem from a Cast object
 */
@Composable
fun CastMemberItem(cast: Cast, onClick: (() -> Unit)? = null) {
    CastMemberItem(
        name = cast.name ?: "Unknown",
        role = cast.character,
        profilePath = cast.profilePath,
        onClick = onClick,
        useGlide = true
    )
}

/**
 * Extension function to create a CastMemberItem from an Actor object
 */
@Composable
fun CastMemberItem(actor: Actor, onClick: (() -> Unit)? = null) {
    CastMemberItem(
        name = actor.name,
        localImageRes = actor.imageRes,
        onClick = onClick
    )
}

/**
 * Extension function to create a CastMemberItem from a PersonResult object
 */
@Composable
fun CastMemberItem(person: PersonResult, onClick: (() -> Unit)? = null) {
    CastMemberItem(
        name = person.name ?: "Unknown",
        profilePath = person.profilePath,
        onClick = onClick
    )
}

@Preview(showBackground = true)
@Composable
fun CastMemberItemPreview() {
    Box(
        modifier = Modifier
            .background(Color(0xFF0F0F1E))
            .padding(16.dp)
    ) {
        CastMemberItem(
            name = "Tom Cruise",
            role = "Ethan Hunt",
            profilePath = null
        )
    }
} 