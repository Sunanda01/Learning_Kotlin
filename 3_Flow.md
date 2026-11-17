# **Kotlin Flow – Basics (Android)**


## **1. Introduction to Kotlin Flows**

**Flow** is a Kotlin Coroutines feature used to emit multiple values asynchronously over time.

Examples:

- Countdown timer.
- Live database updates.
- API data streams.
- Observing user input.

A `suspend` function returns one value, whereas a Flow can emit multiple values.

| Suspend function        | Flow                         |
| ----------------------- | ---------------------------- |
| Returns one value       | Emits multiple values        |
| `suspend fun getUser()` | `fun getUsers(): Flow<User>` |
| One result              | Stream of results            |

## **1.1. Understanding Flow Theory**

A Flow has three main components:

1. **Producer:** Produces and emits data.
2. **Intermediate operators:** Transform or filter data.
3. **Collector:** Receives emitted data.

### **Creating a Flow**

```
val numberFlow = flow {
    emit(1)
    emit(2)
    emit(3)
}
```

### **Collecting a Flow**

```
runBlocking {
    numberFlow.collect { value ->
        println(value)
    }
}
```

### **Cold Flow**

A regular Flow is cold by default.

- It does not execute when created.
- It starts executing when collected.
- Every collector triggers a new execution.

```
val myFlow = flow {
    println("Flow started")
    emit(10)
}

runBlocking {
    myFlow.collect { println(it) }
    myFlow.collect { println(it) }
}
```

`Flow started` is printed twice because the Flow executes separately for each collection.

## **1.2. Creating a Countdown Timer**

A countdown timer is a practical example of Flow because it emits values repeatedly over time.

```
fun countdownTimer(): Flow<Int> = flow {
    for (time in 10 downTo 0) {
        emit(time)
        delay(1000)
    }
}
```

Collecting the countdown:

```
runBlocking {
    countdownTimer().collect { time ->
        println(time)
    }
}
```

**Understanding:**

- `flow { }` creates the Flow.
- `emit(time)` sends the current countdown value.
- `delay(1000)` waits one second without blocking the thread.
- `collect()` receives each value.

## **1.3. Observing Flows in Jetpack Compose**

In Jetpack Compose, Flow values can be observed as Compose state.

Use `collectAsState()` to convert a Flow into Compose `State`.

Example:

```
@Composable
fun TimerScreen(viewModel: TimerViewModel) {

    val time by viewModel.timerFlow
        .collectAsState(initial = 0)

    Text(text = "Time: $time")
}
```

**How it works:**

1. Compose collects the Flow.
2. Whenever a new value is emitted, the state gets updated.
3. Compose recomposes the UI.
4. The updated value appears on the screen.

**Important:** In Android applications, prefer `collectAsStateWithLifecycle()` for lifecycle-aware collection.

## **1.4. Collecting Flows in ViewModel**

A ViewModel can expose Flow data to the UI.

Example:

```
class TimerViewModel : ViewModel() {

    val timerFlow = flow {
        for (time in 10 downTo 0) {
            emit(time)
            delay(1000)
        }
    }
}
```

The UI can collect this Flow using Compose.

**Why use ViewModel?**

- Separates business logic from UI.
- Holds data across configuration changes.
- Provides a lifecycle-aware coroutine scope through `viewModelScope`.

## **1.5. Flow collect vs collectLatest**

Both operators collect values from a Flow, but they behave differently when processing takes time.

### **collect()**

Processes every emitted value sequentially.

```
flow.collect { value ->
    delay(2000)
    println(value)
}
```

If a new value arrives while processing the current one, it waits for the current processing to finish.

### **collectLatest()**

Cancels the previous collection block when a new value arrives and starts processing the latest value.

```
flow.collectLatest { value ->
    delay(2000)
    println(value)
}
```

### **Difference**

| collect()                         | collectLatest()                             |
| --------------------------------- | ------------------------------------------- |
| Processes every value             | Prioritizes latest value                    |
| Previous processing completes     | Previous processing is cancelled            |
| Sequential processing             | Latest-value processing                     |
| Suitable when every value matters | Suitable when older work becomes irrelevant |

**Example use cases:**

- `collect()` – Processing every transaction or saving every event.
- `collectLatest()` – Search suggestions, where a newer search query makes the previous one irrelevant.

---

# **2. Kotlin Flow – Operators**

Flow operators are functions used to **transform, filter, combine, and control the flow of emitted values**.

This lesson covers five areas: simple operators, terminal operators, flattening operators, and buffering-related operators.

## **2.1. What are Flow Operators?**

Flow operators define what happens to each emitted value.

They are mainly divided into:

1. **Intermediate operators:** Transform or modify the Flow.
2. **Terminal operators:** Start collection and produce a final result.

Example:

```
numberFlow
    .filter { it % 2 == 0 }
    .map { it * 10 }
    .collect { println(it) }
```

## **2.2. Simple Operators – filter, map, onEach**

### **filter()**

Filters emitted values based on a condition.

```
flowOf(1, 2, 3, 4, 5)
    .filter { it % 2 == 0 }
    .collect { println(it) }
```

Output: `2, 4`

### **map()**

Transforms each emitted value into another value.

```
flowOf(1, 2, 3)
    .map { it * 10 }
    .collect { println(it) }
```

Output: `10, 20, 30`

### **onEach()**

Performs an action on every emitted value without changing it.

```
flowOf(1, 2, 3)
    .onEach { println("Received: $it") }
    .collect { println("Collected: $it") }
```

Output:

```
Received: 1
Collected: 1
Received: 2
Collected: 2
Received: 3
Collected: 3
```

**Remember:**

| Operator | Purpose                                    |
| -------- | ------------------------------------------ |
| `filter` | Selects values                             |
| `map`    | Transforms values                          |
| `onEach` | Performs an action without changing values |

## **2.3. Terminal Flow Operators – count, reduce, fold**

Terminal operators trigger Flow execution and return a final result.

### **count()**

Counts the number of emitted values.

```
val result = flowOf(1, 2, 3, 4, 5)
    .count { it % 2 == 0 }

println(result) // 2
```

### **reduce()**

Combines all emitted values into a single result.

```
val result = flowOf(1, 2, 3, 4)
    .reduce { accumulator, value ->
        accumulator + value
    }

println(result) // 10
```

- First value becomes the initial accumulator.
- Remaining values are combined one by one.
- Throws an exception if the Flow is empty.

### **fold()**

Similar to `reduce()`, but allows us to provide an initial value.

```
val result = flowOf(1, 2, 3, 4)
    .fold(100) { accumulator, value ->
        accumulator + value
    }

println(result) // 110
```

### **reduce vs fold**

| reduce                                 | fold                             |
| -------------------------------------- | -------------------------------- |
| No initial value                       | Accepts initial value            |
| First emitted value is the accumulator | Starts with the supplied value   |
| Empty Flow throws exception            | Empty Flow returns initial value |

## **2.4. Flattening Operators**

Flattening operators are used when a Flow produces another Flow.

For example, imagine a Flow of user IDs. For each user ID, we make an API call that returns another Flow of user details.

We need to combine these nested Flows into one Flow.

Three important flattening operators:

- `flatMapConcat()`
- `flatMapMerge()`
- `flatMapLatest()`

### **A. flatMapConcat()**

Processes inner Flows sequentially.

It waits for the current inner Flow to complete before collecting the next one.

```
flowOf(1, 2, 3)
    .flatMapConcat { number ->
        flow {
            emit(number)
            delay(1000)
            emit(number * 10)
        }
    }
    .collect { println(it) }
```

Conceptual output:

```
1
10
2
20
3
30
```

**Use case:** When order matters and each inner Flow must complete before the next begins.

### **B. flatMapMerge()**

Processes multiple inner Flows concurrently.

It does not wait for one inner Flow to complete before starting another.

```
flowOf(1, 2, 3)
    .flatMapMerge { number ->
        flow {
            delay(1000)
            emit(number)
        }
    }
    .collect { println(it) }
```

The values can arrive in a different order because the inner Flows execute concurrently.

**Use case:** When independent operations can execute concurrently.

### **C. flatMapLatest()**

Only processes the latest inner Flow.

When a new value arrives, the previous inner Flow is cancelled.

```
flowOf(1, 2, 3)
    .flatMapLatest { number ->
        flow {
            delay(1000)
            emit(number)
        }
    }
    .collect { println(it) }
```

Since each new value arrives before the previous inner Flow completes, earlier inner Flows get cancelled.

**Use case:** Search suggestions. When the user enters a new query, the previous search operation becomes irrelevant.

### **Comparison**

| Operator        | Behaviour                   |
| --------------- | --------------------------- |
| `flatMapConcat` | Sequential execution        |
| `flatMapMerge`  | Concurrent execution        |
| `flatMapLatest` | Cancels previous inner Flow |

## **2.5. Buffer, Conflate, collectLatest**

These operators control what happens when the producer emits values faster than the collector processes them.

### **A. buffer()**

Allows the producer and collector to work independently using a buffer.

Without buffering, the producer may have to wait for the collector to finish processing each value.

```
flow {
    for (i in 1..3) {
        delay(100)
        emit(i)
    }
}
    .buffer()
    .collect {
        delay(500)
        println(it)
    }
```

**Purpose:** Improves throughput by allowing emission and collection to overlap.

### **B. conflate()**

If the collector is busy, older pending values can be dropped. The latest value is retained.

```
flow {
    for (i in 1..5) {
        emit(i)
        delay(100)
    }
}
    .conflate()
    .collect {
        delay(500)
        println(it)
    }
```

**Purpose:** Useful when only the latest value matters, such as live sensor readings or UI state updates.

### **C. collectLatest()**

Cancels the previous collector block when a new value arrives.

```
flow {
    emit(1)
    delay(100)
    emit(2)
    delay(100)
    emit(3)
}
    .collectLatest { value ->
        println("Started: $value")
        delay(500)
        println("Completed: $value")
    }
```

The previous processing is cancelled when a new value arrives.

**Purpose:** Useful for search queries or other operations where processing old values is unnecessary.

### **Comparison**

| Operator          | Behaviour                                            |
| ----------------- | ---------------------------------------------------- |
| `buffer()`        | Stores pending values while collector processes      |
| `conflate()`      | Drops older pending values and keeps the latest      |
| `collectLatest()` | Cancels previous processing when a new value arrives |

---

# **3. Kotlin – StateFlow & SharedFlow**

Both `StateFlow` and `SharedFlow` are **hot streams** provided by Kotlin Coroutines. They are used to share data with multiple collectors.

Unlike a regular Flow (cold Flow), they can emit values even when nobody is collecting them.

## **3.1. What is StateFlow?**

`StateFlow` is a hot Flow used to **hold and observe the current state of an object**.

Important characteristics:

- Always requires an initial value.
- Holds the latest emitted value.
- New collectors immediately receive the current value.
- Emits updates when the value changes.
- Multiple collectors can observe the same state.
- Uses equality-based conflation: equal consecutive values are not emitted.

### **Example**

```
private val _count = MutableStateFlow(0)

val count: StateFlow<Int> = _count

fun increment() {
    _count.value++
}
```

Here:

- `MutableStateFlow` allows us to update the value.
- `StateFlow` exposes read-only state.
- `_count.value` accesses or updates the current value.

### **Collecting StateFlow**

```
viewModelScope.launch {
    count.collect { value ->
        println(value)
    }
}
```

Whenever the count changes, the collector receives the latest value.

**Why use private MutableStateFlow and public StateFlow?**

It follows encapsulation.

- ViewModel can modify the state.
- UI can only observe the state.
- UI cannot directly modify the ViewModel's state.

## **3.2. What is SharedFlow?**

`SharedFlow` is a hot Flow used to **broadcast values to multiple collectors**.

Important characteristics:

- Multiple collectors can receive the same emitted values.
- Does not require an initial value.
- Can replay previously emitted values to new collectors.
- Replay and buffer behaviour can be configured.
- Useful for events that need to be shared.

### **Example**

```
private val _events = MutableSharedFlow<String>()

val events: SharedFlow<String> = _events

suspend fun sendEvent() {
    _events.emit("Login Successful")
}
```

### **Collecting SharedFlow**

```
viewModelScope.launch {
    events.collect { event ->
        println(event)
    }
}
```

When `sendEvent()` emits a value, active collectors receive it.

**Important:** By default, `SharedFlow` has `replay = 0`. A new collector does not receive events emitted before it started collecting.

## **3.3. SharedFlow Replay**

Replay determines how many previously emitted values are stored and sent to new collectors.

Example:

```
val sharedFlow = MutableSharedFlow<Int>(
    replay = 2
)
```

Suppose we emit:

```
sharedFlow.emit(10)
sharedFlow.emit(20)
sharedFlow.emit(30)
```

A new collector receives:

```
20
30
```

Because replay is set to 2, only the last two values are replayed.

## **3.4. StateFlow vs SharedFlow**

| Feature             | StateFlow              | SharedFlow              |
| ------------------- | ---------------------- | ----------------------- |
| Purpose             | Maintain current state | Share events or values  |
| Initial value       | Required               | Not required            |
| Latest value        | Always maintained      | Depends on replay       |
| Default replay      | 1                      | 0                       |
| Multiple collectors | Yes                    | Yes                     |
| Hot or cold         | Hot                    | Hot                     |
| Updating            | `.value` or `emit()`   | `emit()` or `tryEmit()` |
| Use case            | UI state               | Events and broadcasts   |

## **3.5. StateFlow and SharedFlow in Android**

Both are commonly used in the ViewModel to communicate with the UI.

### **StateFlow – UI State**

Example: Login screen state.

```
class LoginViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)

    val isLoading: StateFlow<Boolean> = _isLoading

    fun login() {
        viewModelScope.launch {
            _isLoading.value = true

            delay(2000)

            _isLoading.value = false
        }
    }
}
```

The UI observes `isLoading` and displays a loading indicator whenever its value is true.

### **SharedFlow – UI Events**

Example: Showing a toast or snackbar after login.

```
class LoginViewModel : ViewModel() {

    private val _events = MutableSharedFlow<String>()

    val events = _events.asSharedFlow()

    fun loginSuccess() {
        viewModelScope.launch {
            _events.emit("Login Successful")
        }
    }
}
```

The UI collects the event and displays a message.

**Remember:**

- StateFlow → What is the current state?
- SharedFlow → What event should be communicated?

## **3.6. MutableStateFlow vs StateFlow**

| MutableStateFlow             | StateFlow               |
| ---------------------------- | ----------------------- |
| Read and write               | Read-only               |
| Allows updating values       | Allows observing values |
| Used internally in ViewModel | Exposed to UI           |

Example:

```
private val _userName = MutableStateFlow("Guest")

val userName: StateFlow<String> = _userName
```

## **3.7. MutableSharedFlow vs SharedFlow**

| MutableSharedFlow             | SharedFlow            |
| ----------------------------- | --------------------- |
| Allows emitting values        | Read-only collection  |
| Supports replay configuration | Exposes shared stream |
| Used internally in ViewModel  | Exposed to collectors |

## **3.8. Converting Flow into StateFlow or SharedFlow**

Sometimes we have a regular cold Flow and want to share it with multiple collectors.

Kotlin provides:

### **stateIn()**

Converts a cold Flow into a StateFlow.

```
val stateFlow = regularFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = emptyList<User>()
)
```

- Requires an initial value.
- Maintains the latest state.
- Shares the upstream Flow.

### **shareIn()**

Converts a cold Flow into a SharedFlow.

```
val sharedFlow = regularFlow.shareIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    replay = 1
)
```

- Shares upstream emissions with multiple collectors.
- Replay determines how many previous values new collectors receive.

### **SharingStarted strategies**

| Strategy            | Behaviour                                                                                                 |
| ------------------- | --------------------------------------------------------------------------------------------------------- |
| `Eagerly`           | Starts sharing immediately                                                                                |
| `Lazily`            | Starts when the first collector appears and continues afterward                                           |
| `WhileSubscribed()` | Starts when collectors appear and stops after the last collector disappears (subject to its stop timeout) |

---

## **4. Kotlin – Unit Testing Flows**

## 4.1. Introduction

Unit testing Flows means verifying that a Flow emits the expected values and behaves correctly.

We generally test:

- Values emitted by a Flow.
- Number and order of emissions.
- StateFlow updates.
- ViewModel logic that exposes Flows.
- Coroutine cancellation and timing behaviour.

## 4.2. Overview

For testing Kotlin Flows, we commonly use:

| Library | Purpose |
|---|---|
| `kotlinx-coroutines-test` | Provides coroutine testing utilities |
| Turbine | Makes Flow testing easier |
| Truth | Provides readable assertions |
| JUnit | Runs unit tests |

### Basic testing approach

1. Create the Flow or ViewModel.
2. Collect the Flow inside a test.
3. Verify emitted values.
4. Verify expected behaviour using assertions.

## 4.3. Dependencies

Common dependencies for Flow testing:

```kotlin
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:<version>")

testImplementation("app.cash.turbine:turbine:<version>")

testImplementation("com.google.truth:truth:<version>")

testImplementation("junit:junit:<version>")
```

- `kotlinx-coroutines-test`: Provides `runTest`, test dispatchers, and virtual time.
- `Turbine`: Provides convenient Flow testing methods.
- `Truth`: Provides readable assertions.

## 4.4. Generate Test Case

A test case is a function that verifies the expected behaviour of a unit of code.

Example using JUnit:

```kotlin
@Test
fun testFlow() = runTest {
    val result = flowOf(1, 2, 3).toList()

    assertEquals(listOf(1, 2, 3), result)
}
```

Here:

- `@Test` identifies the test function.
- `runTest` provides a coroutine test environment.
- `toList()` collects all Flow emissions.
- `assertEquals()` verifies the expected result.

## 4.5. Test Model

A model represents the data used by the application.

Example:

```kotlin
data class User(
    val id: Int,
    val name: String
)
```

We can test whether a Flow emits the expected model objects.

```kotlin
@Test
fun testUserFlow() = runTest {
    val userFlow = flowOf(
        User(1, "John"),
        User(2, "Sam")
    )

    val users = userFlow.toList()

    assertEquals(2, users.size)
    assertEquals("John", users[0].name)
}
```

## 4.6. Test Case Using Turbine

Turbine makes testing Flow emissions easier.

Instead of manually converting a Flow into a list, we can inspect emissions one by one.

```kotlin
@Test
fun testFlowWithTurbine() = runTest {
    val numbers = flowOf(1, 2, 3)

    numbers.test {
        assertEquals(1, awaitItem())
        assertEquals(2, awaitItem())
        assertEquals(3, awaitItem())

        awaitComplete()
    }
}
```

### Important Turbine functions

| Function | Purpose |
|---|---|
| `test { }` | Starts collecting the Flow for testing |
| `awaitItem()` | Waits for the next emitted value |
| `awaitComplete()` | Verifies Flow completion |
| `awaitError()` | Waits for a Flow exception |
| `cancelAndIgnoreRemainingEvents()` | Cancels collection and ignores remaining events |
| `expectNoEvents()` | Verifies that no event was emitted |

**Important:** Turbine helps verify the sequence of events, including values, completion, and errors.

## 4.7. Flow Turbine

Turbine is a testing library specifically designed to test Kotlin Flows.

Example:

```kotlin
@Test
fun testFlow() = runTest {
    flow {
        emit("Loading")
        emit("Success")
    }.test {
        assertEquals("Loading", awaitItem())
        assertEquals("Success", awaitItem())

        awaitComplete()
    }
}
```

Turbine is useful when testing:

- Multiple emissions.
- StateFlow updates.
- Flow completion.
- Flow exceptions.
- Events emitted over time.

## 4.8. runBlocking

`runBlocking` starts a coroutine and blocks the current thread until its coroutine completes.

Example:

```kotlin
@Test
fun testFlow() = runBlocking {
    flowOf(1, 2, 3).collect {
        println(it)
    }
}
```

### runBlocking vs runTest

| runBlocking | runTest |
|---|---|
| Blocks the current thread | Uses a coroutine test environment |
| Uses real delays | Supports virtual time |
| General coroutine bridge | Designed for coroutine testing |
| Does not provide test scheduler control | Provides control over coroutine execution |

**Important:** Prefer `runTest` for coroutine unit tests.

## 4.9. Truth

Truth is an assertion library that makes test assertions more readable.

Example:

```kotlin
@Test
fun testFlow() = runTest {
    val result = flowOf(10, 20, 30).toList()

    assertThat(result).containsExactly(10, 20, 30).inOrder()
}
```

Common Truth assertions:

| Assertion | Purpose |
|---|---|
| `isEqualTo()` | Checks equality |
| `isTrue()` | Checks whether value is true |
| `isFalse()` | Checks whether value is false |
| `isNull()` | Checks for null |
| `isNotNull()` | Checks for non-null |
| `contains()` | Checks whether a collection contains a value |
| `containsExactly()` | Checks exact collection contents |

## 4.10. ViewModel Testing

We can test the Flow exposed by a ViewModel without launching the actual Android UI.

Example ViewModel:

```kotlin
class CounterViewModel : ViewModel() {

    private val _count = MutableStateFlow(0)

    val count: StateFlow<Int> = _count

    fun increment() {
        _count.value++
    }
}
```

Test:

```kotlin
@Test
fun testCounterViewModel() = runTest {
    val viewModel = CounterViewModel()

    assertEquals(0, viewModel.count.value)

    viewModel.increment()

    assertEquals(1, viewModel.count.value)
}
```

For a StateFlow, we can directly inspect its current value using `.value`.

## 4.11. Main Dispatcher

Android applications use `Dispatchers.Main` for UI-related work.

However, unit tests running on the JVM do not have the Android Main dispatcher by default.

We can replace it with a test dispatcher.

Example:

```kotlin
@Before
fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
}

@After
fun tearDown() {
    Dispatchers.resetMain()
}
```

**Important:**

- `Dispatchers.setMain()` replaces the Main dispatcher for tests.
- `Dispatchers.resetMain()` restores the original dispatcher.
- This is useful when testing ViewModels that use `viewModelScope`.

## 4.12. Test Dispatchers

Kotlin provides different test dispatchers to control coroutine execution.

### StandardTestDispatcher

- Does not execute launched coroutines immediately.
- Queues coroutine work until the test scheduler is advanced.
- Useful for controlling the execution order.

### UnconfinedTestDispatcher

- Starts executing coroutines eagerly until their first suspension point.
- Useful when immediate execution is desired.
- Does not provide the same predictable scheduling behaviour as StandardTestDispatcher.

Example:

```kotlin
@Test
fun testCoroutine() = runTest {
    val dispatcher = StandardTestDispatcher(testScheduler)

    launch(dispatcher) {
        println("Coroutine started")
    }

    runCurrent()
}
```

### Difference

| StandardTestDispatcher | UnconfinedTestDispatcher |
|---|---|
| Queues execution | Starts eagerly |
| Requires scheduler advancement | Runs until suspension |
| More control over execution order | Less control over scheduling |

## 4.13. Advanced Time By

Coroutine tests support virtual time.

This means we can advance the test scheduler's clock without actually waiting.

Important functions:

| Function | Purpose |
|---|---|
| `runCurrent()` | Executes tasks scheduled at the current virtual time |
| `advanceTimeBy()` | Advances virtual time by a specified duration |
| `advanceUntilIdle()` | Executes pending tasks until the scheduler is idle |

Example:

```kotlin
@Test
fun testDelay() = runTest {
    var completed = false

    launch {
        delay(5000)
        completed = true
    }

    advanceTimeBy(5000)

    runCurrent()

    assertTrue(completed)
}
```

Here, the test advances virtual time by five seconds without waiting five real seconds.

**Important:** `advanceTimeBy()` advances the clock, but tasks scheduled exactly at the new time may still need `runCurrent()` to execute.

For most finite coroutine tests, `advanceUntilIdle()` is useful to execute all pending scheduled work.

---

