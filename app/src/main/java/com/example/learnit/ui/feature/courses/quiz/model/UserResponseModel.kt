import java.util.Date

data class UserResponseModel(
    val uqrQuestionId: Int,
    val uqrUserId: Int,
    var response: QuizResponseModel,
    val responseTime: Date
)

data class QuizResponseModel(
    val answer: List<Boolean>
)