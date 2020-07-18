package sk.goodreq.model

data class UsersPage(
    val page: Long,
    val per_page: Long,
    val total: Long,
    val total_pages: Long,
    val data: List<User>
)