package com.example.memorygame.ui.screens

import android.content.Context
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.memorygame.R
import com.example.memorygame.databinding.ScreenHardGameBinding
import com.example.memorygame.databinding.ScreenNormalGameBinding
import com.example.memorygame.source.localStorage.LocaleStorage
import com.example.memorygame.ui.adapters.QueueAdapter
import com.example.memorygame.ui.adapters.RememberAdapter
import com.example.memorygame.utills.Presets
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class HardGameScreen : Fragment(R.layout.screen_hard_game){

    private val binding by viewBinding (ScreenHardGameBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }
    private var adapter : QueueAdapter? = QueueAdapter()
//    private val args : NormalGameScreenArgs by navArgs()
    private var dataArray = IntArray(15)
    private var answerArray = ArrayList<Int>(5)
    private var typedAnswer = ArrayList<Int>(5)
    private var typedAnswerId = ArrayList<Int>(5)
    var counter = 0
    lateinit var textView : TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData(LocaleStorage.getHardArray())
        requireActivity().window.statusBarColor = Color.parseColor("#81AFA5")

        textView = binding.textView

        answerArray = adapter!!.getArray()

        binding.recyclerView.adapter = adapter

        adapter?.initListener { id, view ->
            val itemId = view.tag as Int
            if (typedAnswerId.contains(itemId)){
                typedAnswerId.remove(itemId)
                typedAnswer.remove(id)
            }
            else {
                typedAnswerId.add(itemId)
                typedAnswer.add(id)
            }

            if (typedAnswerId.size >= 5){
                if (typedAnswer == answerArray){
                    typedAnswerId.clear()
                    fireWorks()
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1100)
                        withContext(Dispatchers.Main) {
                            navController.navigate(HardGameScreenDirections.actionHardGameScreenSelf(LocaleStorage.getHardArray()))
                        }
                    }
                }
                else{
                    typedAnswerId.clear()
                    val snackBar = Snackbar.make(binding.root, "Wrong Answer", 800)
                    snackBar.anchorView = binding.linearAnchor
                    snackBar.setBackgroundTint(Color.RED)
                    snackBar.show()
                    if (Build.VERSION.SDK_INT >= 26) {
                        (requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                            VibrationEffect.createWaveform(LongArray(4){100}, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        (requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(150)
                    }
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(1000)
                        withContext(Dispatchers.Main) {
                            navController.navigate(HardGameScreenDirections.actionHardGameScreenSelf(dataArray))
                        }
                    }
                }
            }
        }


        adapter?.initTimedListener { id, view1 ->
            adapter?.setClickable(false)
            binding.textView.scaleX = 0f
            binding.textView.scaleY = 0f


            view1.animate()
                    .withEndAction {
                        view1.setBackgroundResource(R.drawable.item_special_bg)
                        view1.findViewById<TextView>(R.id.iconView).text = answerArray[counter++].toString()
                    }.setDuration(400 * id.toLong())
                    .start()





            CoroutineScope(Dispatchers.IO).launch {
                delay(2500)
                withContext(Dispatchers.Main) {
                    view1.setBackgroundResource(R.drawable.item_easy_bg)
                    view1.findViewById<TextView>(R.id.iconView).text = ""
                    animateView(id)
                }
            }



        }



    }


    private fun loadData(list: IntArray){
        typedAnswer.clear()
        dataArray = list
        adapter?.submitList(list)
        adapter?.notifyDataSetChanged()
    }

    private fun animateView(id : Int){
        if(id==5){

            textView.apply {
                visibility = View.VISIBLE
                animate()
                    .scaleX(100f)
                    .scaleY(100f)
                    .alpha(0f)
                    .withEndAction { textView.visibility = View.GONE
                    adapter?.setClickable(true)}
                    .setDuration(1000)
                    .start()
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
    }

}