package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.learnitevekri.R
import com.learnitevekri.data.ApiConstants.ARG_CHAPTER_ID
import com.learnitevekri.data.ApiConstants.ARG_COURSE_ID
import com.learnitevekri.data.ApiConstants.ARG_LESSON_ID
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.databinding.ExitConfirmationDialogBinding
import com.learnitevekri.databinding.FragmentQuizBinding
import com.learnitevekri.databinding.QuizResultDialogBinding
import com.learnitevekri.ui.activities.MainActivity
import com.learnitevekri.ui.feature.courses.quiz.QuizPagerAdapter
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding
    private val viewModel: SharedQuizViewModel by activityViewModels()
    private var maxNumberOfQuestion: Int = -1
    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1
    private var totalScore: Float = 0.0f
    private var questionsAnswers: List<BaseQuestionData> = emptyList()
    private var mainActivity: MainActivity? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var mediaPlayer: MediaPlayer

    companion object {
        val TAG: String = QuizFragment::class.java.simpleName
        val currentQuestionNumber: MutableLiveData<Int> by lazy {
            MutableLiveData<Int>(0)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentQuizBinding.inflate(inflater, container, false)

        courseId = arguments?.getInt(ARG_COURSE_ID, -1) ?: -1
        chapterId = arguments?.getInt(ARG_CHAPTER_ID, -1) ?: -1
        lessonId = arguments?.getInt(ARG_LESSON_ID, -1) ?: -1

        viewPager = binding.viewPager
        viewModel.loadAllQuestionsAnswers(courseId, chapterId, lessonId)

        mainActivity?.hideBottomNavigationView()
        mediaPlayer = MediaPlayer()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        observeState()

        binding.escapeButton.setOnClickListener {
            showExitConfirmationDialog()
        }

        observeScore()
        observeSound()
        observeCurrentQuestionNumber()
        observeCurrentQuestionItem()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    when (state) {
                        is SharedQuizViewModel.QuestionAnswersPageState.Loading -> {
                            Log.d(TAG, "Loading questionsAnswers...")
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Success -> {
                            Log.d(TAG, "QuestionsAnswers loaded")
                            questionsAnswers = state.questionsAnswersData
                            maxNumberOfQuestion = questionsAnswers.size
                            setUpAdapter()
                        }

                        is SharedQuizViewModel.QuestionAnswersPageState.Failure -> {
                            Log.e(
                                TAG,
                                "Error loading QuestionsAnswers: ${state.throwable}"
                            )
                            (activity as MainActivity?)?.errorHandling(state.throwable)
                        }
                    }
                }
            }
        }
    }

    private fun showExitConfirmationDialog() {
        val view = ExitConfirmationDialogBinding.inflate(layoutInflater).root

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)

        val dialog = builder.create()
        dialog.show()

        view.findViewById<Button>(R.id.yesButton).setOnClickListener {
            val bundle = Bundle().apply {
                putInt(ARG_COURSE_ID, courseId)
                putInt(ARG_CHAPTER_ID, chapterId)
                putInt(ARG_LESSON_ID, lessonId)
            }

            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.quizFragment, true)
                .build()

            findNavController().navigate(
                R.id.action_quizFragment_to_chaptersFragment,
                bundle,
                navOptions
            )

            Log.d(TAG, "Deleting responses for lessonId: $lessonId")
            viewModel.deleteResponses(lessonId)
            dialog.dismiss()
        }

        view.findViewById<Button>(R.id.noButton).setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun showQuizResultDialog() {
        if (!isAdded || activity == null) {
            return
        }

        val dialogView = QuizResultDialogBinding.inflate(layoutInflater).root

        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.dialogMessage)
        val okButton = dialogView.findViewById<Button>(R.id.okButton)

        dialogTitle.text = getString(R.string.quiz_result)
        dialogMessage.text = getString(R.string.your_score_is, totalScore.toInt().toString())

        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setView(dialogView)

        val alertDialog = alertDialogBuilder.create()

        okButton.setOnClickListener {
            val bundle = Bundle().apply {
                putInt(ARG_COURSE_ID, courseId)
                putInt(ARG_CHAPTER_ID, chapterId)
                putInt(ARG_LESSON_ID, lessonId)
            }

            findNavController().navigate(
                R.id.action_quizFragment_to_chaptersFragment,
                bundle
            )


            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun setUpAdapter() {
        viewPager.isUserInputEnabled = false
        viewPager.setCurrentItem(0, false)

        val adapter = QuizPagerAdapter(
            requireActivity(),
            questionList = questionsAnswers,
            courseId = courseId,
            chapterId = chapterId,
            lessonId = lessonId
        )
        viewPager.adapter = adapter

        binding.progressBar.max = maxNumberOfQuestion

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.progressBar.progress = position + 1
                updateLessonProgressText(position + 1, maxNumberOfQuestion)
            }
        })

        viewPager.setCurrentItem(
            0, true
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateLessonProgressText(currentQuestion: Int, totalQuestions: Int) {
        binding.textLessonProgress.text = "$currentQuestion/$totalQuestions"
    }

    private fun observeScore() {
        val observer = Observer<Float> { totalScore ->
            updateScoreUI(totalScore)
        }
        viewModel.scoreLiveData.observe(requireActivity(), observer)
    }

    private fun observeSound() {
        viewModel?.let { viewModel ->
            viewModel.soundLiveData?.observe(viewLifecycleOwner) { shouldPlaySound ->
                shouldPlaySound?.let {
                    if (shouldPlaySound) {
                        mediaPlayer.apply {
                            reset()
                            setDataSource(
                                requireContext(),
                                Uri.parse("android.resource://${requireContext().packageName}/${R.raw.correct}")
                            )
                            prepare()
                            start()
                        }
                    } else {
                        mediaPlayer.apply {
                            reset()
                            setDataSource(
                                requireContext(),
                                Uri.parse("android.resource://${requireContext().packageName}/${R.raw.wrong}")
                            )
                            prepare()
                            start()
                        }
                    }
                }
            }
        }
    }

    private fun observeCurrentQuestionNumber() {
        val observer = Observer<Int> { currentQuestionNumber ->
            if (currentQuestionNumber == maxNumberOfQuestion) {
                showQuizResultDialogWithDelay()
            }
        }
        currentQuestionNumber.observe(requireActivity(), observer)
    }

    private fun observeCurrentQuestionItem() {
        viewModel.currentQuestionItemLiveData.observe(viewLifecycleOwner) { currentItem ->
            viewPager.currentItem = currentItem
            Log.d(TAG, "currentItem: $currentItem")
        }
    }

    @SuppressLint("SetTextI18n")
    fun updateScoreUI(newScore: Float) {
        totalScore += newScore * 10
        binding.textScore.text = "Score: ${totalScore.toInt()}"
    }

    @SuppressLint("RestrictedApi")
    override fun onDestroyView() {
        super.onDestroyView()
        mainActivity?.showBottomNavigationView()

        viewModel.scoreLiveData.removeObservers(requireActivity())
        viewModel.scoreLiveData.value = 0.0f
        viewModel.soundLiveData.removeObservers(requireActivity())
        viewModel.soundLiveData.value = null
        viewModel.currentQuestionItemLiveData.removeObservers(requireActivity())
        viewModel.currentQuestionItemLiveData.value = 0
        currentQuestionNumber.removeObservers(requireActivity())
        currentQuestionNumber.value = 0
        totalScore = 0.0f
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun showQuizResultDialogWithDelay() {
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            showQuizResultDialog()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as? MainActivity
    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }

}