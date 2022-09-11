package ru.ermolnik.news

import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.mts.data.news.db.News
import ru.mts.data.news.db.NewsType

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state = viewModel.state.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        when (val value = state.value) {
            is NewsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
            is NewsState.Error -> {
                Error(viewModel, value.throwable)
            }
            is NewsState.Content -> {
                Content(viewModel = viewModel, items = value.items)
            }
        }
    }
}

@Composable
private fun Content(viewModel: NewsViewModel, items: List<News>) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshing),
        onRefresh = { viewModel.refresh() }
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            items.forEach { NewsItem(it) }
        }
    }
}

@Composable
private fun NewsItem(news: News) {
    Row(
        Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier
                .weight(weight = 1f, fill = false)
                .padding(end = 12.dp)
        ) {

            val icon = when (news.type) {
                NewsType.FINANCE -> R.drawable.ic_baseline_analytics_24
                NewsType.WEATHER -> R.drawable.ic_baseline_wb_sunny_24
                NewsType.POLITICS -> R.drawable.ic_baseline_business_center_24
                NewsType.SCIENCE -> R.drawable.ic_baseline_science_24
            }

            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(44.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = news.title,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Start),
                    textAlign = TextAlign.Start,
                    fontSize = with(LocalDensity.current) { 18.dp.toSp() },
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = news.title,
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.Start),
                    textAlign = TextAlign.Start,
                    fontSize = with(LocalDensity.current) { 14.dp.toSp() },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
private fun BoxScope.Error(viewModel: NewsViewModel, throwable: Throwable) {
    Column(
        modifier = Modifier
            .wrapContentSize()
            .padding(16.dp)
            .align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = throwable.toString(),
            textAlign = TextAlign.Center
        )
        Button(
            onClick = { viewModel.retry() }
        ) {
            Text(text = stringResource(id = R.string.retry_button))
        }
    }
}
