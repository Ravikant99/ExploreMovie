package com.ravi.exploremovie.screens.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ravi.exploremovie.R
import com.ravi.exploremovie.screenRoutes.ScreenRoutes

@Composable
fun LoginScreen(navController: NavController) {
    var mobileNumber by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(60.dp))

            Text("Login with Mobile", fontSize = 24.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                label = { Text("Enter Mobile Number") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate(ScreenRoutes.OtpScreen.route) {
                        popUpTo(ScreenRoutes.LoginScreen.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continue")
            }

            // Social login at bottom
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Or continue with", color = Color.Gray)

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    IconButton(onClick = { /* Handle Google login */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.google), // Put icon in drawable
                            contentDescription = "Google",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    IconButton(onClick = { /* Handle Facebook login */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.facebook), // Put icon in drawable
                            contentDescription = "Facebook",
                            tint = Color.Unspecified,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}



    @Preview(showBackground = true)
    @Composable
    fun PreviewLoginScreen() {
        // Provide a dummy NavController for preview purposes
        val navController = rememberNavController()
        LoginScreen(navController = navController)
    }