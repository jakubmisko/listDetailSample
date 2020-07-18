package sk.goodreq.model

import com.squareup.moshi.Json

data class User(val id: Long, val email: String, val first_name: String, val last_name: String, val avatar: String) {
}