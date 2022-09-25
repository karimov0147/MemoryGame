package com.example.memorygame.ui.screens

import android.animation.Animator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.View.OnTouchListener
import android.widget.Toast
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.memorygame.R
import com.example.memorygame.databinding.DialogWinCustomBinding
import com.example.memorygame.databinding.ScreenEasyGameBinding
import com.example.memorygame.source.localStorage.LocaleStorage
import com.example.memorygame.ui.adapters.MyAdapter
import com.example.memorygame.utills.ImageModel
import com.example.memorygame.utills.Presets
import kotlinx.coroutines.*


class EasyGameScreen  : Fragment(R.layout.screen_easy_game) {

    private val binding by viewBinding (ScreenEasyGameBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    var array = ArrayList<ImageModel>()
    var adapter = MyAdapter()
    var one : ImageModel? = null
    var two : ImageModel? = null
    var count = 0
    var clickable = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadData()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().window.statusBarColor = Color.parseColor("#81AFA5")
        adapter.submitList(array)
        val recycler = binding.recyclerView

//        recycler.setOnTouchListener(OnTouchListener { v, event -> true })


        binding.recyclerView.adapter = adapter
        // the Linear Layout Manager inner class
        val myLinearLayoutManager = object : GridLayoutManager(requireContext() , 3) {
            override fun canScrollVertically(): Boolean {
                return true
            }

        }


        recycler.layoutManager = myLinearLayoutManager

        adapter.initListener {
            animateView(it)
        }

    }


    private fun animateView(model : ImageModel){
        model.view?.isClickable = false
        var it = model.view ?: return
        var icon = model.view!!.findViewById<ImageFilterView>(R.id.iconView)

        it.isClickable = false
        it.animate()
            .rotationYBy(90f)
            .setDuration(500)
//            .setListener(object : Animator.AnimatorListener{
//                override fun onAnimationStart(animation: Animator?) {
//                   clickable = false
//                }
//
//                override fun onAnimationEnd(animation: Animator?) {
//                    clickable = true
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                    TODO("Not yet implemented")
//                }
//
//                override fun onAnimationRepeat(animation: Animator?) {
//                    TODO("Not yet implemented")
//                }
//
//            })
            .withEndAction {
                icon.setPadding(0)
                icon.setImageResource(model.image)
                it.animate()
                    .rotationYBy(90f)
                    .setDuration(500)
                    .start()
            }
            .start()

        if (one == null){
            one = model
        }
        else{
            two = model
            adapter.setClickable(false)

            if (two?.id == one?.id){
                isSimilar(one?.view , two?.view , one!!.id)
                one = null
                two = null
            }else{
                isNotSimilar(one?.view , two?.view)
//                one?.view?.isClickable = true
//                two?.view?.isClickable = true
                one = null
                two = null
            }

        }

    }





    private fun isSimilar(view1: View?, view2: View? , id:Int) {

        view1?.isClickable = false
        view2?.isClickable = false
        count++

        CoroutineScope(Dispatchers.IO).launch {
            delay(1200)
            withContext(Dispatchers.Main) {
                view1!!.animate()
                    .alpha(0.4f)
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(500)
                    .start()

                view2!!.animate()
                    .alpha(0.4f)
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(500)
                    .start()

                adapter.removeItem(id)
            }
        }


        if (count == 9) {
            val customView = View.inflate(requireContext(), R.layout.dialog_win_custom, null)
            val bindCustomView = DialogWinCustomBinding.bind(customView)
//        val bindCustomView = DialogWinCustomBinding.inflate(LayoutInflater.from(requireContext()) , null , false )
            val dialogBuilder = AlertDialog.Builder(requireContext())
            dialogBuilder.setView(bindCustomView.root)
            val dialog = dialogBuilder.create()
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

            fireWorks()

            CoroutineScope(Dispatchers.IO).launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    dialog.show()
                }
            }

            bindCustomView.menuBtn.setOnClickListener {
                dialog.dismiss()
                navController.navigateUp()

            }
            bindCustomView.nextBtn.setOnClickListener {
                navController.navigate(R.id.action_easyGameScreen_self)
                dialog.dismiss()
            }


        }
        adapter.setClickable(true)
    }


        private fun isNotSimilar(view1: View?, view2: View?) {
            var icon1 = view1!!.findViewById<ImageFilterView>(R.id.iconView)
            var icon2 = view2!!.findViewById<ImageFilterView>(R.id.iconView)


            CoroutineScope(Dispatchers.IO).launch {
                delay(1200)
                withContext(Dispatchers.Main) {
                    if (Build.VERSION.SDK_INT >= 26) {
                        (requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(
                            VibrationEffect.createWaveform(LongArray(4){110}, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        (requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator).vibrate(150)
                    }
                    view1.animate()
                        .rotationYBy(-90f)
                        .setDuration(500)
                        .withEndAction {
                            icon1.setPadding(27)
                            icon1.setImageResource(R.drawable.questions)
                            view1.animate()
                                .rotationYBy(-90f)
                                .setDuration(500)
                                .withEndAction { view1.isClickable = true ; view1.rotationY  = 0f }
                                .start()
                        }
                        .start()

                    view2.animate()
                        .rotationYBy(-90f)
                        .setDuration(500)
                        .withEndAction {
                            icon2.setPadding(27)
                            icon2.setImageResource(R.drawable.questions)
                            view2.animate()
                                .rotationYBy(-90f)
                                .setDuration(500)
                                .withEndAction { view2.isClickable = true ; view2.rotationY = 0f }
                                .start()
                        }
                        .start()

                }
            }

           adapter.setClickable(true)

        }


        private fun loadData() {
            var firstPart = LocaleStorage.easyArray0
        firstPart.shuffle()
            array.addAll(firstPart)
            var secondPart = LocaleStorage.easyArray1
        secondPart.shuffle()
            array.addAll(secondPart)
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










