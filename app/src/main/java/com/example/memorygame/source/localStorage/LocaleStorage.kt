package com.example.memorygame.source.localStorage

import com.example.memorygame.R
import com.example.memorygame.utills.ImageModel
import java.util.*
import kotlin.collections.ArrayList

object LocaleStorage {

    var midLevel = 1

    var easyArray0 = arrayListOf<ImageModel>(
        ImageModel(1 , R.drawable.i ),
        ImageModel(1 , R.drawable.i ),
        ImageModel(2 , R.drawable.ii ),
        ImageModel(2 , R.drawable.ii ),
        ImageModel(3 , R.drawable.iii ),
        ImageModel(3 , R.drawable.iii ),
        ImageModel(4 , R.drawable.iv ),
        ImageModel(4 , R.drawable.iv ),
        ImageModel(5 , R.drawable.v ),
        ImageModel(5, R.drawable.v ),
        ImageModel(6 , R.drawable.vi ),
        ImageModel(6 , R.drawable.vi ),
        ImageModel(7 , R.drawable.vii),
        ImageModel(7, R.drawable.vii ),
        ImageModel(8 , R.drawable.viii ),
        ImageModel(8 , R.drawable.viii ),
        ImageModel(9 , R.drawable.ix ),
        ImageModel(9 , R.drawable.ix ),
    )
    var easyArray1 = arrayListOf<ImageModel>(
        ImageModel(10 , R.drawable.x ),
        ImageModel(10 , R.drawable.x ),
        ImageModel(11 , R.drawable.xi ),
        ImageModel(11 , R.drawable.xi ),
        ImageModel(12 , R.drawable.xii ),
        ImageModel(12 , R.drawable.xii ),
        ImageModel(13 , R.drawable.xiii ),
        ImageModel(13 , R.drawable.xiii ),
        ImageModel(14 , R.drawable.xiv ),
        ImageModel(14, R.drawable.xiv ),
        ImageModel(15 , R.drawable.xv ),
        ImageModel(15 , R.drawable.xv ),
        ImageModel(16 , R.drawable.xvi),
        ImageModel(16, R.drawable.xvi ),
        ImageModel(17 , R.drawable.xvii ),
        ImageModel(17 , R.drawable.xvii ),
        ImageModel(18 , R.drawable.xviii ),
        ImageModel(18 , R.drawable.xviii ),
    )

    @JvmName("getMidArray1")
    fun getMidArray() : IntArray{
        val indices = ArrayList<Int>()
        val numbers = IntArray(15){0}
        val r = Random()
        while (indices.size < 7) {
            val random: Int = (1..14).random()
            if (!indices.contains(random)) {
                indices.add(random)
            }
        }
        for (it in indices){
            numbers[it] = it
        }
        return numbers
    }


    fun getHardArray() : IntArray{
        val indices = ArrayList<Int>()
        val numbers = IntArray(15){0}
        val r = Random()
        while (indices.size < 5) {
            val random: Int = (1..14).random()
            if (!indices.contains(random)) {
                indices.add(random)
            }
        }
        var num = 1
        for (it in indices.sorted()){
            numbers[it] = num++
        }
        return numbers
    }



}