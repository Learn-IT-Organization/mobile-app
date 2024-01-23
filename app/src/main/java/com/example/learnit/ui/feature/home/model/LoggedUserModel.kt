package com.example.learnit.ui.feature.home.model

data class LoggedUserModel(
    val userId: Long,
    val userRole: String,
    val firstName: String,
    val lastName: String,
    val userName: String,
    val userPassword: String,
    val gender: String,
    val userLevel: String,
    val userPhoto: UserPhotoModel?,
    val streak: Int
)

data class UserPhotoModel(
    val type: String,
    val data: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserPhotoModel

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