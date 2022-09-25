package com.example.memorygame.ui.screens

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Color
import android.os.*
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.memorygame.R
import com.example.memorygame.databinding.ScreenEasyGameBinding
import com.example.memorygame.databinding.ScreenNormalGameBinding
import com.example.memorygame.source.localStorage.LocaleStorage
import com.example.memorygame.ui.adapters.RememberAdapter
import com.example.memorygame.utills.Presets
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class NormalGameScreen : Fragment(R.layout.screen_normal_game) {

    private val binding by viewBinding (ScreenNormalGameBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private val adapter = RememberAdapter()
    private val args : NormalGameScreenArgs by navArgs()
    private var dataArray = IntArray(15)
    private var answerArray = ArrayList<Int>(7)
    private var typedAnswer = ArrayList<Int>(7)
    private var typedAnswerId = ArrayList<Int>(7)
    lateinit var textview : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData(args.list)
        requireActivity().window.statusBarColor = Color.parseColor("#81AFA5")
        textview = binding.textView

        answerArray = adapter.getArray()

        binding.recyclerView.adapter = adapter

        adapter.initListener { id, view ->
            val itemId = view.tag as Int
            if (typedAnswerId.contains(itemId)){
                typedAnswerId.remove(itemId)
                typedAnswer.remove(id)
            }
            else {
                typedAnswerId.add(itemId)
                typedAnswer.add(id)
            }



            if (typedAnswerId.size >= 7){
                if (typedAnswer.sorted() == answerArray.sorted()){
                    typedAnswerId.clear()
                    fireWorks()
                    LocaleStorage.midLevel++
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1100)
                        withContext(Dispatchers.Main) {
                            navController.navigate(NormalGameScreenDirections.actionNormalGameScreenSelf(LocaleStorage.getMidArray()))
                        }}
                }
                else{
                    typedAnswerId.clear()
                    val snackBar = Snackbar.make(binding.root, "Wrong Answer", 800)
                    snackBar.anchorView = binding.linearAnchor
                    snackBar.setBackgroundTint(Color.RED)
                    snackBar.show()
                    if (Build.VERSION.SDK_INT >= 26) {
                (requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(
                    VibrationEffect.createWaveform(LongArray(4){100}, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                (requireContext().getSystemService(VIBRATOR_SERVICE) as Vibrator).vibrate(150)
            }
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            navController.navigate(NormalGameScreenDirections.actionNormalGameScreenSelf(dataArray))
                        }}
                           }
            }


        }


        adapter.initTimedListener { id, view ->
            adapter.setClickable(false)
            CoroutineScope(Dispatchers.IO).launch {
                delay(3000)
                withContext(Dispatchers.Main) {
                    view.setBackgroundResource(R.drawable.item_easy_bg)
                    textview.text = (LocaleStorage.midLevel.toString())
                    textview.apply {
                        visibility = View.VISIBLE
                        animate()
                            .scaleX(100f)
                            .scaleY(100f)
                            .alpha(0f)
                            .withEndAction { textview.visibility = View.GONE}
                            .setDuration(1200)
                            .start()
                    }
                    adapter.setClickable(true)
                }}
        }



    }

    private fun loadData(list: IntArray){
        typedAnswer.clear()
        dataArray = list
        adapter.submitList(list)
        adapter.notifyDataSetChanged()
    }


    private fun fireWorks(){
        binding.konfettiView.start(
            Presets.mix()
        )
        if (Build.VERSION.SDK_INT >= 26) {
            (requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            (requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(150)
        }
    }

}