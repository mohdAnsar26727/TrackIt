package track.it.app.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CoroutineExtended {
    suspend fun <T> doIoOperation(block: suspend CoroutineScope.() -> T) =
        withContext(
            Dispatchers.IO,
            block = block
        )
}