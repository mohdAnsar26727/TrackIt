package track.it.app.domain.model

enum class PlanSortType(val column: String) {
    TITLE("title"),
    BUDGET("initialBudget"),
    UPDATED_AT("updatedAt"),
    CREATED_AT("createdAt"),
}

enum class SortOrder {
    ASC, DESC
}
