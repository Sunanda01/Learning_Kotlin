// Suppose we are making API states.
// An API can be:
//  1. Loading
//  2. Success
//  3. Error
// How do we represent this cleanly?

// Old Approach
//class Result(
//    val isLoading: Boolean,
//    val data: String?,
//    val error: String?
//)
// Problem: confusing, invalid states possible, hard to manage

sealed class Result {
    object Loading : Result()
    data class Success(val data: String) : Result()
    data class Error(val message: String) : Result()
}

fun main() {
    val result: Result = Result.Success("Data fetched")
    when(result) {
        is Result.Loading -> {
            println("Loading...")
        }
        is Result.Success -> {
            println(result.data)
        }
        is Result.Error -> {
            println(result.message)
        }
    }
}

// Sealed Class restricted class hierarchy. Only predefined subclasses allowed.
// Why Better Than Enum?
// Enums only store constants.
// Sealed classes can store: different data, different structures, different logic