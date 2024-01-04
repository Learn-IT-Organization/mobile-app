package com.example.learnit.data.courses.chapters.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChaptersData(
    //altalban nem hasznaljuk igy a valtozo neveket
    //Peldaul chapterId, ha nem muszaj ne hasznaljatok "_"
    //De ha ti jobban szeretitek igy akkor igy is jo
    @SerialName("chapter_id") val chapter_id: Int,
    @SerialName("chapter_name") val chapter_name: String,
    @SerialName("chapter_course_id") val chapter_course_id: Int,
    @SerialName("chapter_description") val chapter_description: String,
    @SerialName("chapter_sequence_number") val chapter_sequence_number: Int,
)
