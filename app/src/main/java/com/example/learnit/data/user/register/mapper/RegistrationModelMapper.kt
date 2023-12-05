package com.example.learnit.data.user.register.mapper

import com.example.learnit.data.user.register.model.RegistrationData
import com.example.learnit.ui.feature.register.model.RegistrationModel

fun RegistrationModel.mapToRegistration() = RegistrationData(
    first_name = this.firstName!!,
    last_name = this.lastName!!,
    user_name = this.userName!!,
    user_password = this.userPassword!!,
    gender = this.gender!!,
    user_level = this.userLevel!!
)