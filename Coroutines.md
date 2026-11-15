# Coroutines

## What distinguishes Coroutines from Threads?
- Executed within a thread
- Coroutines are suspendable
- They can switch their context

---

**Coroutines** are a Kotlin feature used to perform asynchronous and non-blocking operations.

They allow long-running tasks (API calls, database operations, file operations) to execute without blocking the main thread.

**Important functions:**
- runBlocking() – Starts a coroutine and blocks the current thread until it completes.
- launch() – Starts a coroutine without returning a result.
- delay() – Suspends a coroutine for a specified time without blocking the thread.

*Example*

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {
        delay(1000)
        println("Coroutine executed")
    }

    println("Main function executed")
}
```

*Example use cases:*
- Fetching data from an API.
- Reading data from a database.
- Performing background tasks.
- Handling multiple tasks concurrently.

| Thread | Coroutine |
| --- | --- |
| Operating system level execution unit | Lightweight Kotlin execution unit |
| Relatively expensive | Lightweight |
| More resource-intensive | Can have thousands of coroutines |
| Managed by OS/JVM | Managed by coroutine framework |

---

## 1. Suspend Function
function that can pause its execution and resume later without blocking the thread.
Use the suspend keyword to declare it.

```kotlin
import kotlinx.coroutines.*

suspend fun fetchData() {
    delay(2000)
    println("Data fetched")
}

fun main() = runBlocking {
    println("Start")

    fetchData()

    println("End")
}
```

---

## 2. Coroutine Context
Collection of elements that defines how a coroutine behaves and executes.

It provides information such as:
- Which thread or thread pool the coroutine uses.
- How the coroutine is identified.
- How cancellation is handled.
- How exceptions are handled.

### 2.1. Main Elements of Coroutine Context
|Element |	Purpose |
| --- | --- |
|Job | Manages coroutine lifecycle and cancellation |
|CoroutineDispatcher | Determines where the coroutine executes |
|CoroutineName | Assigns a name to the coroutine |
|CoroutineExceptionHandler | Handles uncaught coroutine exceptions |

### 2.2. Coroutine Dispatchers
A dispatcher is one of the most important elements of coroutine context.
|Dispatcher|	Usage|
| --- | --- |
|Dispatchers.Main|	UI operations|
|Dispatchers.IO|	Network calls, database and file operations|
|Dispatchers.Default|	CPU-intensive tasks|
|Dispatchers.Unconfined|	Starts in the caller thread; resumes according to suspension behaviour|

### 2.3. Job in Coroutine Context
A Job represents the lifecycle of a coroutine.

It helps to:
- Start and manage coroutine execution.
- Cancel a coroutine.
- Check whether a coroutine is active or completed.
- Wait for coroutine completion using join().

### 2.4. Coroutine Context vs Coroutine Scope

|Coroutine Context	|Coroutine Scope|
| --- | --- |
|Defines coroutine execution properties	|Defines the lifecycle boundary for coroutines|
|Contains Job, Dispatcher, Name, etc.	|Provides a context in which coroutines are launched|
|Controls execution behaviour	|Controls coroutine lifetime|

```kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    val handler = CoroutineExceptionHandler { _, exception ->
        println("Error: ${exception.message}")
    }

    // Creates a scope to launch coroutines
    val scope = CoroutineScope(
        Dispatchers.IO +                            // 	Executes coroutine on IO dispatcher
        CoroutineName("MyCoroutine") +              //  Assigns a name to coroutine
        handler                                     //  Handles uncaught exceptions
    )

    // Manages coroutine lifecycle
    val job = scope.launch {
        println("Name: ${coroutineContext[CoroutineName]}")
        println("Thread: ${Thread.currentThread().name}")
        println("Task executing...")
    }

    job.join()                                     // Waits for coroutine completion 
    scope.cancel()                                 // Cancels the scope and its child coroutines 
}
```

---

### 3. `runBlocking()`

`runBlocking()` is a coroutine builder that starts a coroutine and **blocks the current thread until the coroutine and its child coroutines complete**.

**Example:**

``` kotlin
import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {
        delay(1000)
        println("Coroutine executed")
    }

    println("Main executed")
}
```

**Output:**

```
Main executed
Coroutine executed
```

**Key points:**

- Acts as a bridge between regular code and suspending code.
- Blocks the current thread until completion.
- Can call suspend functions inside it.
- Commonly used in `main()` functions and testing.
- Avoid using it on Android's Main/UI thread because it blocks the UI.

**Remember:** `runBlocking` blocks the thread, whereas `delay()` suspends the coroutine without blocking the thread.

---

## 4. Jobs, Waiting & Cancellation in Kotlin Coroutines

### **4.1. Job**

A `Job` represents the lifecycle of a coroutine. It is returned by the `launch()` builder and helps manage coroutine execution.

```
val job = launch {
    println("Coroutine running")
}
```

**Common Job functions:**

| Function          | Purpose                                |
| ----------------- | -------------------------------------- |
| `start()`         | Starts a coroutine (if lazily created) |
| `join()`          | Waits for coroutine completion         |
| `cancel()`        | Cancels the coroutine                  |
| `cancelAndJoin()` | Cancels and waits for completion       |
| `isActive`        | Checks whether coroutine is active     |
| `isCancelled`     | Checks whether coroutine is cancelled  |
| `isCompleted`     | Checks whether coroutine is completed  |

### **4.2. Waiting (**`join()`**)**

`join()` suspends the calling coroutine until the specified coroutine completes.

```
fun main() = runBlocking {

    val job = launch {
        delay(1000)
        println("Task completed")
    }

    job.join()

    println("Main completed")
}
```

**Output:**

```
Task completed
Main completed
```

### **4.3. Cancellation (**`cancel()`**)**

`cancel()` requests cancellation of a coroutine.

```
fun main() = runBlocking {

    val job = launch {
        repeat(5) {
            println("Working...")
            delay(500)
        }
    }

    delay(1000)
    job.cancelAndJoin()

    println("Job cancelled")
}
```

**Possible output:**

```
Working...
Working...
Job cancelled
```

---

## 5. **Async and Await in Kotlin Coroutines**

`async` and `await()` are used when we want to execute a coroutine and **get a result from it**.

- `async` → Starts a coroutine and returns a `Deferred` object.
- `await()` → Waits for the result of the coroutine and retrieves it.

## ** 5.1. Async with Multiple Coroutines**

Multiple tasks can execute concurrently.

```
import kotlinx.coroutines.*

fun main() = runBlocking {

    val task1 = async {
        delay(1000)
        "User Data"
    }

    val task2 = async {
        delay(1000)
        "Product Data"
    }

    println(task1.await())
    println(task2.await())
}
```

**Output:**

```
User Data
Product Data
```

Both tasks can run concurrently, rather than waiting for the first task to finish before starting the second.

### **5.2. Launch vs Async**

| Feature  | launch                   | async                              |
| -------- | ------------------------ | ---------------------------------- |
| Purpose  | Execute a task           | Execute a task and return a result |
| Returns  | `Job`                    | `Deferred<T>`                      |
| Result   | No result                | Returns result                     |
| Waiting  | `join()`                 | `await()`                          |
| Use case | Logging, background task | API calls returning data           |


**Remember:** `launch` = Fire and forget (no result). `async` = Execute and return a result.

---

## 6. lifecycleScope and viewModelScope

Both `lifecycleScope` and `viewModelScope` are Android-specific coroutine scopes used to manage coroutine lifecycles automatically.

They prevent unnecessary background tasks from continuing after their associated component is no longer needed.

### 6.1. lifecycleScope

- Used with Android lifecycle components such as Activity and Fragment.
- Coroutines launched inside it are automatically cancelled when the associated lifecycle is destroyed.
- Commonly used for UI-related operations.

**Example:**

```
lifecycleScope.launch {
    val data = fetchData()
    textView.text = data
}
```

**When is it cancelled?**
*When Activity or Fragment is destroyed.*


### 6.2. viewModelScope

- Used inside an Android `ViewModel`.
- Coroutines launched inside it are automatically cancelled when the ViewModel is cleared.
- Commonly used for API calls, data fetching and business logic.

**Example:**

```
class MyViewModel : ViewModel() {

    fun loadData() {
        viewModelScope.launch {
            val data = fetchData()
            // Update UI state
        }
    }
}
```

**When is it cancelled?**
*When the ViewModel is cleared (no longer needed).*

### 6.3. Difference Between lifecycleScope and viewModelScope

| Feature                         | lifecycleScope                                             | viewModelScope                         |
| ------------------------------- | ---------------------------------------------------------- | -------------------------------------- |
| Associated with                 | Activity / Fragment                                        | ViewModel                              |
| Cancellation                    | Lifecycle destroyed                                        | ViewModel cleared                      |
| Main purpose                    | UI lifecycle tasks                                         | UI-related data/business logic         |
| Survives configuration changes? | Activity scope is cancelled when old Activity is destroyed | Yes, if the same ViewModel is retained |

### 6.4. Dependency Required

For Android projects, add:

```
dependencies {
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:<version>")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:<version>")
}
```

---

## 7. Coroutines with Firebase (Kotlin)

Kotlin Coroutines can be used with Firebase to perform asynchronous operations such as fetching data, inserting records, authentication and reading database values without blocking the main thread.

### 7.1. Dependency

For Firebase operations using Kotlin Coroutines, commonly used dependencies include:

```
dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:<version>")
    implementation("com.google.firebase:firebase-auth:<version>")
}
```

`kotlinx-coroutines-play-services` provides coroutine integration with Google Play Services `Task` APIs through functions such as `await()`.

### 7.2. Basic Example – Firebase Authentication

Using `await()` with a suspend function:

```
suspend fun login(email: String, password: String) {
    try {
        val result = FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .await()

        println("Login successful")

    } catch (e: Exception) {
        println("Login failed: ${e.message}")
    }
}
```

### 7.3. How It Works

1. `signInWithEmailAndPassword()` initiates Firebase authentication.
2. Firebase returns a `Task<AuthResult>`.
3. `await()` suspends the coroutine until the task completes.
4. On success, the result is returned.
5. On failure, an exception is thrown and handled by `catch`.

### 7.4. Calling from ViewModel

```
class AuthViewModel : ViewModel() {

    fun login(email: String, password: String) {
        viewModelScope.launch {
            login(email, password)
        }
    }
}
```

### **Quick Revision**

| Concept          | Purpose                                              |
| ---------------- | ---------------------------------------------------- |
| Firebase         | Backend services such as authentication and database |
| Coroutine        | Handles asynchronous operations                      |
| `suspend`        | Allows suspension without blocking a thread          |
| `await()`        | Waits for Firebase Task completion                   |
| `viewModelScope` | Manages coroutine lifecycle in ViewModel             |
| `try-catch`      | Handles success/failure exceptions                   |

**Remember:** Coroutines + Firebase allow us to write asynchronous Firebase operations in a sequential, readable way using `suspend` functions and `await()`.

---

## 8. Coroutines with Retrofit

Retrofit supports Kotlin Coroutines, allowing us to make API calls using `suspend` functions instead of traditional callbacks.

### 8.1. How it works

- `suspend` – Allows an API function to suspend until the response is available.
- Retrofit handles the network request asynchronously.
- `viewModelScope.launch` – Calls the API from a coroutine.
- `withContext(Dispatchers.IO)` is generally not required because Retrofit handles network execution internally.

### 8.2. Retrofit API interface

```
interface ApiService {

    @GET("users")
    suspend fun getUsers(): List<User>
}
```

Here:

- `@GET("users")` defines the API endpoint.
- `suspend` allows the function to be called from a coroutine.
- `List<User>` is the expected response.

### 8.3. Calling the API

``` kotlin
viewModelScope.launch {
    try {
        val users = apiService.getUsers()
        println(users)
    } catch (e: Exception) {
        println("Error: ${e.message}")
    }
}
```

- `viewModelScope.launch` starts a coroutine.
- `getUsers()` makes the API request.
- The response is received after the request completes.
- `try-catch` handles errors.

---

The video covers **Kotlin Coroutine Exception Handling and Cancellation**. Based on the chapter titles in your screenshot, I’ve organized the notes in the same sequence, with simple explanations and code examples.

The main idea to understand is: **coroutine failures, cancellation, and supervision are connected through the parent-child relationship.**

## 9. Kotlin Coroutines – Exception Handling and Cancellation

### 9.1. Coroutines and Exceptions

Exceptions in coroutines are handled using:

- `try-catch` – Handles exceptions directly inside a coroutine.
- `CoroutineExceptionHandler` – Handles uncaught exceptions in certain coroutines.
- `supervisorScope` – Prevents one child coroutine's failure from cancelling its sibling coroutines.

```
CoroutineScope(Dispatchers.IO).launch {
    try {
        // Risky operation
        throw Exception("Something went wrong")
    } catch (e: Exception) {
        println(e.message)
    }
}
```

### 9.2. Coroutine Exception Propagation

Exception propagation means **an exception in a child coroutine can travel upwards to its parent**.

In a regular coroutine hierarchy:

1. A child coroutine throws an exception.
2. The parent coroutine is cancelled.
3. Other child coroutines are also cancelled.
4. The exception propagates upwards.

Example:

```
coroutineScope {
    launch {
        throw Exception("Child failed")
    }

    launch {
        delay(2000)
        println("Second child")
    }
}
```

When the first child fails, the second child is cancelled too.

**Important:** This is the default behaviour of regular structured concurrency.

### 9.3. Coroutine Cancellation Basics

Cancellation is used to stop a coroutine when its work is no longer required.

Common Job functions:

| Function          | Purpose                                   |
| ----------------- | ----------------------------------------- |
| `cancel()`        | Requests cancellation                     |
| `join()`          | Waits for coroutine completion            |
| `cancelAndJoin()` | Cancels and waits                         |
| `isActive`        | Checks whether coroutine is active        |
| `ensureActive()`  | Throws cancellation exception if inactive |

Example:

```
val job = launch {
    repeat(10) {
        println("Working...")
        delay(500)
    }
}

job.cancel()
job.join()
```

### **Cancellation is cooperative**

A coroutine does not automatically stop every operation immediately.

It needs to reach a cancellation-aware suspension point, such as `delay()`, or explicitly check its cancellation status.

```
while (isActive) {
    // Perform work
}
```

### 9.4. Handling Exceptions in Async

`async` returns a `Deferred<T>` containing the result or exception.

The exception is exposed when `await()` is called.

```
val deferred = async {
    throw Exception("Async failed")
}

try {
    deferred.await()
} catch (e: Exception) {
    println("Error: ${e.message}")
}
```

### 9.5. Coroutine Exception Handler

`CoroutineExceptionHandler` handles uncaught exceptions when there is no other exception propagation mechanism.

Example:

```
val handler = CoroutineExceptionHandler { _, exception ->
    println("Caught: ${exception.message}")
}

val scope = CoroutineScope(Dispatchers.IO + handler)

scope.launch {
    throw Exception("Something went wrong")
}
```

**Important points:**

- It is mainly used for uncaught exceptions.
- It is useful for logging errors or displaying error messages.
- It does not recover a failed coroutine.
- It does not replace `try-catch`.
- It does not handle exceptions from `async`; handle those through `await()`.

### 9.6. CoroutineScope vs SupervisorScope

Both are used to create structured coroutine scopes, but their exception propagation behaviour differs.

### coroutineScope

If one child fails:

- Parent is cancelled.
- Other children are cancelled.
- Exception is propagated to the caller.

```
coroutineScope {
    launch {
        throw Exception("Failed")
    }

    launch {
        delay(1000)
        println("Second task")
    }
}
```

### supervisorScope

If one child fails:

- Other children continue executing.
- Failure of one child does not cancel the supervisor or its siblings.
- Parent cancellation still cancels all children.

```
supervisorScope {
    launch {
        throw Exception("Failed")
    }

    launch {
        delay(1000)
        println("Second task continues")
    }
}
```

### Difference

| coroutineScope                       | supervisorScope                                |
| ------------------------------------ | ---------------------------------------------- |
| Child failure cancels siblings       | Child failure does not cancel siblings         |
| Failure propagates to parent         | Child failure does not propagate to supervisor |
| Used when tasks depend on each other | Used when tasks should fail independently      |

### 9.7. Custom Scopes and Traps

A custom CoroutineScope allows us to define how coroutines execute and how their lifecycle is managed.

Example:

```
val scope = CoroutineScope(
    Dispatchers.IO + SupervisorJob()
)

scope.launch {
    // Task 1
}

scope.launch {
    // Task 2
}
```

Here:

- `Dispatchers.IO` specifies the execution context.
- `SupervisorJob()` prevents one child failure from cancelling other children.
- `scope.cancel()` cancels the scope and its children.

### SupervisorJob vs Job

| Job                                          | SupervisorJob                                    |
| -------------------------------------------- | ------------------------------------------------ |
| Child failure can cancel parent and siblings | Child failure does not cancel parent or siblings |
| Regular structured concurrency               | Independent child failure handling               |

**Use case:** If an application performs multiple independent background tasks, `SupervisorJob` can prevent one task's failure from stopping the others.

### 9.8. Common Cancellation Trap

One common mistake is catching all exceptions without handling cancellation properly.

`CancellationException` is also an exception. If we accidentally swallow it, the coroutine may not stop as expected.

Incorrect:

```
try {
    delay(5000)
} catch (e: Exception) {
    println("Error occurred")
}
```

This catches `CancellationException` as well.

Correct:

```
try {
    delay(5000)
} catch (e: CancellationException) {
    throw e
} catch (e: Exception) {
    println("Error: ${e.message}")
}
```

**Remember:**

Always rethrow `CancellationException` when catching broad exceptions, so cancellation can propagate correctly.

### 9.9 Advanced Exception Handling

## 9.9.1 CoroutineExceptionHandler – Important Rules

`CoroutineExceptionHandler` is not a normal `try-catch` mechanism.

Important rules:

1. It handles uncaught exceptions in eligible coroutines.
2. It is generally useful for root coroutines or children of a `SupervisorJob`.
3. A regular child coroutine delegates its exception to its parent; its own handler does not handle the exception independently.
4. It cannot recover or restart a failed coroutine.
5. Exceptions from `async` are exposed through `await()`.

Example:

```kotlin
val handler = CoroutineExceptionHandler { _, exception ->
    println("Handled: ${exception.message}")
}

val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

scope.launch(handler) {
    throw Exception("Task failed")
}
```

Here, the `SupervisorJob` allows the failed child to fail independently, and the handler can handle its uncaught exception.

### 9.9.2 Exception Handling with supervisorScope

`supervisorScope` is useful when multiple tasks should execute independently.

```kotlin
suspend fun performTasks() = supervisorScope {

    launch {
        try {
            throw Exception("Task 1 failed")
        } catch (e: Exception) {
            println(e.message)
        }
    }

    launch {
        delay(1000)
        println("Task 2 completed")
    }
}
```

Task 1 fails, but Task 2 can continue.

**Important:** `supervisorScope` does not automatically handle exceptions. An uncaught exception from a child `launch` still needs appropriate handling, such as a `CoroutineExceptionHandler`.

### 9.9.3 Handling Multiple Async Exceptions

When using multiple `async` coroutines, exceptions are retrieved through `await()`.

```kotlin
supervisorScope {
    val task1 = async {
        throw Exception("Task 1 failed")
    }

    val task2 = async {
        "Task 2 completed"
    }

    try {
        println(task1.await())
    } catch (e: Exception) {
        println("Task 1 error: ${e.message}")
    }

    println(task2.await())
}
```

Here:

- `supervisorScope` prevents Task 1's failure from cancelling Task 2.
- `await()` exposes the exception from Task 1.
- Task 2 can complete successfully.

### 9.9.4 NonCancellable – Cleanup During Cancellation

Normally, when a coroutine is cancelled, its suspending operations are also cancelled.

Sometimes we need to perform cleanup even after cancellation, such as:

- Closing a resource.
- Saving final state.
- Releasing a connection.

For this, Kotlin provides `NonCancellable`.

```kotlin
val job = launch {
    try {
        delay(5000)
    } finally {
        withContext(NonCancellable) {
            delay(1000)
            println("Cleanup completed")
        }
    }
}

job.cancelAndJoin()
```

**Important:**

- `finally` executes during cancellation.
- `NonCancellable` allows suspending cleanup operations to complete.
- Use it only for short cleanup operations, not for continuing normal work.

### 9.9.5 withTimeout and TimeoutCancellationException

`withTimeout()` cancels a coroutine if the specified time limit is exceeded.

```kotlin
try {
    withTimeout(2000) {
        delay(5000)
        println("Completed")
    }
} catch (e: TimeoutCancellationException) {
    println("Operation timed out")
}
```

Here:

- Timeout is 2 seconds.
- Operation needs 5 seconds.
- The operation is cancelled after the timeout.

`withTimeoutOrNull()` provides an alternative that returns `null` when the timeout occurs.

```kotlin
val result = withTimeoutOrNull(2000) {
    delay(5000)
    "Success"
}

println(result) // null
```

### Difference

| withTimeout | withTimeoutOrNull |
|---|---|
| Throws `TimeoutCancellationException` | Returns `null` on timeout |
| Requires exception handling if timeout is expected | Convenient for optional results |

### 9.9.6 Cancellation and Exception Handling – Best Practices

- Always rethrow `CancellationException` when catching broad exceptions.
- Use `try-catch` for expected failures.
- Use `CoroutineExceptionHandler` for uncaught exception reporting.
- Use `supervisorScope` or `SupervisorJob` for independent tasks.
- Use `finally` for cleanup.
- Use `NonCancellable` only when suspending cleanup is necessary.
- Avoid swallowing exceptions silently.

---
