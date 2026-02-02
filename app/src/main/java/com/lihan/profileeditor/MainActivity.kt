package com.lihan.profileeditor

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.lihan.profileeditor.ui.theme.ProfileEditorTheme
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route{

    @Serializable
    data object Profile: Route

    @Serializable
    data class AvatarEditor(val uriString: String): Route
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val avatarRepository = remember { UserProfileRepository(this@MainActivity) }

            ProfileEditorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        modifier = Modifier.fillMaxSize().padding(it),
                        navController = navController,
                        startDestination = Route.Profile
                    ){
                        composable<Route.Profile>{
                            val viewModel = viewModel {
                                ProfileViewModel(avatarRepository = avatarRepository)
                            }
                            ProfileScreenRoot(
                                viewModel = viewModel,
                                onNavigateToAvatarEditorScreen = { uriString ->
                                    navController.navigate(Route.AvatarEditor(uriString))
                                }
                            )
                        }

                        composable<Route.AvatarEditor>{ entry ->
                            val uriString = entry.toRoute<Route.AvatarEditor>().uriString
                            val viewModel = viewModel {
                                AvatarEditorViewModel(avatarRepository = avatarRepository)
                            }
                            AvatarEditorScreenRoot(
                                uriString = uriString,
                                onBack = {
                                    navController.navigateUp()
                                },
                                viewModel = viewModel
                            )

                        }
                    }
                }

            }
        }
    }
}
