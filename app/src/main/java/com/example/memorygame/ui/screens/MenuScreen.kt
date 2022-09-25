package com.example.memorygame.ui.screens

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.memorygame.R
import com.example.memorygame.databinding.ScreenMenuBinding
import com.example.memorygame.source.localStorage.LocaleStorage
import com.example.memorygame.utills.Presets
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter


class MenuScreen : Fragment(R.layout.screen_menu) {

    private val binding by viewBinding (ScreenMenuBinding::bind)
    private val navController by lazy(LazyThreadSafetyMode.NONE) { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.statusBarColor = Color.parseColor("#000000")

        binding.textView.animate()
            .setDuration(1000)
            .rotationX(0F)
            .start()



        binding.easy.setOnClickListener {
            navController.navigate(R.id.action_menuScreen_to_easyGameScreen)
        }


        binding.medium.setOnClickListener {
            navController.navigate(MenuScreenDirections.actionMenuScreenToNormalGameScreen(LocaleStorage.getMidArray()))
        }


        binding.hard.setOnClickListener {
            navController.navigate(MenuScreenDirections.actionMenuScreenToHardGameScreen(LocaleStorage.getHardArray()))
        }









    }





}