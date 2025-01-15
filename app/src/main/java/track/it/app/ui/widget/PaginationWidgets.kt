package track.it.app.ui.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import track.it.app.R
import track.it.app.ui.theme.marginDefault
import track.it.app.ui.theme.paddingLarge

fun <T : Any> LazyListScope.handlePagingState(
    paging: LazyPagingItems<T>,
    @StringRes emptyMessage: Int
) {
    with(paging) {
        when {
            loadState.refresh is LoadState.Loading -> {
                // Show full-screen loader during initial load
                item { PageLoader(modifier = Modifier.fillParentMaxSize()) }
            }

            loadState.refresh is LoadState.Error -> {
                // Show error message for initial load error
                val error = loadState.refresh as LoadState.Error
                item {
                    ErrorMessage(
                        modifier = Modifier.fillParentMaxSize(),
                        message = error.error.message.orEmpty(),
                        showRetryButton = error.error !is NoSuchElementException,
                        onClickRetry = { retry() }
                    )
                }
            }

            // Check for empty list state only when the refresh is not loading or error
            itemCount == 0 && loadState.refresh is LoadState.NotLoading -> {
                // Show empty state message
                item {
                    EmptyStateMessage(
                        modifier = Modifier.fillMaxSize(),
                        message = stringResource(emptyMessage)
                    )
                }
            }

            loadState.append is LoadState.Loading -> {
                // Show loading indicator at the bottom for paginated loading
                item { LoadingNextPageItem(modifier = Modifier) }
            }

            loadState.append is LoadState.Error -> {
                // Show error message at the bottom for paginated load failure
                val error = loadState.append as LoadState.Error
                item {
                    ErrorMessage(
                        modifier = Modifier,
                        message = error.error.localizedMessage.orEmpty(),
                        onClickRetry = { retry() }
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyStateMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(
        modifier,
        verticalArrangement = Arrangement.spacedBy(marginDefault),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_folder_error),
            contentDescription = null
        )
        Text(
            message,
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Composable
fun PageLoader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.let_me_cook),
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        CircularProgressIndicator(Modifier.padding(top = 10.dp))
    }
}

@Composable
fun LoadingNextPageItem(modifier: Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )
}

@Composable
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    showRetryButton: Boolean = true,
    onClickRetry: () -> Unit
) {
    if (showRetryButton) {
        Column(
            modifier = modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(marginDefault))
            OutlinedButton(onClick = onClickRetry) {
                Text(text = stringResource(id = R.string.retry))
            }
        }
    } else {
        Text(
            text = message,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingLarge.all),
            textAlign = TextAlign.Center
        )
    }
}