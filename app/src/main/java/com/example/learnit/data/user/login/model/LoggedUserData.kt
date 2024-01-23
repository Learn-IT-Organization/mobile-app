package com.example.learnit.data.user.login.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoggedUserData(
    @SerialName("user_id")
    val user_id: Long,
    @SerialName("user_role")
    val user_role: String,
    @SerialName("first_name")
    val first_name: String,
    @SerialName("last_name")
    val last_name: String,
    @SerialName("user_name")
    val user_name: String,
    @SerialName("user_password")
    val user_password: String,
    @SerialName("gender")
    val gender: String,
    @SerialName("user_level")
    val user_level: String,
    @SerialName("user_photo")
    val user_photo: UserPhotoData?,
    @SerialName("streak")
    val streak: Int
)
data class UserPhotoData(
    val type: String,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPhotoData

        if (type != other.type) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}