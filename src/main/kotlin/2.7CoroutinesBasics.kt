// Used everywhere for: API calls, database operations, background work, async programming

// Suppose app does: API call
// This takes: 2 sec, 5 sec, maybe longer
// If done on main thread: App freezes

// Old Solution : People used: Threads, Callbacks, AsyncTask (old Android), Messy and difficult.

// Coroutines provide: Lightweight asynchronous programming, Cleaner and easier
// "Pause work without blocking thread"

import kotlinx.coroutines.*

fun main() = runBlocking {
    launch {
        delay(2000)
        println("Coroutine finished")
    }
    println("Main function")
}

// runBlocking = creates coroutine scope. Used mostly for: learning and testing
// launch = starts a coroutine.
// delay means: Pause coroutine for 2 seconds WITHOUT blocking thread. Pauses only coroutine
// but Thread.sleep() blocks entire thread

//Coroutines are: lightweight, scalable, easier than threads
//We can create: thousands of coroutines very efficiently.

// * Suspend Function : A function that can: pause and resume
// eg suspend fun fetchData() {
//    delay(2000)
//    println("Data fetched")}

// suspend function can ONLY be called from: coroutine or another suspend function
// async used when: Need result from coroutine

fun main() = runBlocking {
    val result = async {
        delay(2000)
        10 + 20
    }
    println(result.await())
}

// Note
// 1. launch returns: Job nut No result.
// Used for: fire-and-forget tasks
// 2. async returns: Deferred<T> ,Result expected.
// 3. await() used to: wait for async result

// Coroutines need lifecycle.
// Android commonly uses: viewModelScope, lifecycleScope to avoid memory leaks.

// NEVER do: API calls, database, heavy computation on: Main/UI thread; Use coroutines.
