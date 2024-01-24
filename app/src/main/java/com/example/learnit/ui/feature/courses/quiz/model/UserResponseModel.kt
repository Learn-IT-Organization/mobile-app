import java.util.Date

data class UserResponseModel(
    val uqrQuestionId: Int,
    val uqrUserId: Int,
    var response: QuizResponseModel,
    val responseTime: Date,
    val score: Float
)

data class QuizResponseModel(
    val answer: List<Boolean>
)