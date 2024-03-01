import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ChapterResultData(
    @SerializedName("userScore")
    val userScore: Float = 0f,
    @SerializedName("isCompleted")
    val isCompleted: Boolean = false,
    @SerializedName("lessonsCompleted")
    val lessonsCompleted: Int = 0,
    @SerializedName("totalLessons")
    val totalLessons: Int = 0
) : Serializable