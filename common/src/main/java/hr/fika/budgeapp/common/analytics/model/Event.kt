package hr.fika.budgeapp.common.analytics.model

enum class Event (val event: String) {
    APP_START("appStart"),
    REGISTRATION("registration"),
    LOGIN("login"),
    ATMS_LOADED("atmsLoaded"),
    BALANCE_LOADED("balanceLoaded"),
    FLOW_LOADED("flowLoaded"),
    TRANSACTION_ADDED("transactionAdded"),
    FLOW_ADDED("flowAdded"),
    CRYPTO_CHECKED("cryptoChecked"),
    STOCK_CHECKED("stockChecked"),
    BUDGET_LOADED("budgetLoaded"),
    BUDGET_CREATED("budgetCreated");

    fun getEventName() = event
}