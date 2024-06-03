package com.learnitevekri.ui.feature.courses.courses.viewModel

import androidx.lifecycle.ViewModel

class SharedLessonViewModel : ViewModel() {
    var lessonButtonVisibilityAndEditTextMap: MutableMap<Int, Boolean> = mutableMapOf()
}