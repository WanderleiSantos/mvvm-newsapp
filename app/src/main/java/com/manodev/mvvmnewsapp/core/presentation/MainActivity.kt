package com.manodev.mvvmnewsapp.core.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.manodev.mvvmnewsapp.core.presentation.ui.theme.MVVMNewsAppTheme
import com.manodev.mvvmnewsapp.news.presentation.NewsScreenCore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MVVMNewsAppTheme {
                Navigation()
            }
        }
    }

    @Composable
    fun Navigation(modifier: Modifier = Modifier) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = Screen.News
        ) {
            composable<Screen.News> {
                NewsScreenCore {
                    navController.navigate(Screen.Article(it))
                }
            }

            composable<Screen.Article> { navBackStackEntry ->
                val article: Screen.Article = navBackStackEntry.toRoute()
                article.articleId
            }
        }
    }
}

