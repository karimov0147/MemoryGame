package com.example.memorygame.ui.adapters

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.R
import com.example.memorygame.databinding.ItemThreeBinding
import com.example.memorygame.databinding.ItemTwoBinding
import com.example.memorygame.utills.ImageModel
import kotlinx.coroutines.*

class QueueAdapter : RecyclerView.Adapter<QueueAdapter.ViewHolder>() {

    private var array = IntArray(15){0}
    lateinit var listener : ((Int , View ) -> Unit )
    lateinit var timedListener: ((Int , View) -> Unit)
    private var selectedList = Array(15){false}
    private var answerArray = ArrayList<Int>(5)
    private var counter = 0
    var clickable = true


    inner class ViewHolder(var view : ItemThreeBinding) : RecyclerView.ViewHolder(view.root){


        fun bind(){

            if (array[bindingAdapterPosition] != 0) {
                answerArray.add(array[bindingAdapterPosition])

//                view.root.animate()
//                    .withEndAction {
//                        view.root.setBackgroundResource(R.drawable.item_special_bg)
//                        view.iconView.text = answerArray[counter++].toString()
//                        timedListener.invoke(array[bindingAdapterPosition], view.root)
//                    }.setDuration(400 * array[bindingAdapterPosition].toLong())
//                    .start()

                timedListener.invoke(array[bindingAdapterPosition], view.root)
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    delay(400 * array[bindingAdapterPosition].toLong())
//                    withContext(Dispatchers.Main) {
//                        view.root.setBackgroundResource(R.drawable.item_special_bg)
//                        view.iconView.text = answerArray[counter++].toString()
//
//                    }
//                }

            }

            itemView.rootView.setOnClickListener {
               if (clickable) {
                    if (!selectedList[bindingAdapterPosition]) {
                        view.root.setBackgroundResource(R.drawable.item_special_bg)
                        listener.invoke(array[bindingAdapterPosition], view.root)
                        selectedList[bindingAdapterPosition] = true
                    } else if (selectedList[bindingAdapterPosition]) {
                        view.root.setBackgroundResource(R.drawable.item_easy_bg)
                        listener.invoke(array[bindingAdapterPosition], view.root)
                        selectedList[bindingAdapterPosition] = false
                    }
                }

                }

            view.root.tag = bindingAdapterPosition
            view.iconView.setPadding(27)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  = ViewHolder(
        ItemThreeBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    @JvmName("setClickable1")
    fun setClickable(state : Boolean){
        this.clickable = state
    }

    override fun getItemCount(): Int = array.size

    fun submitList(newArray : IntArray){
        array = newArray
        notifyDataSetChanged()
    }

    fun initTimedListener( l : ((Int , View) -> Unit)){
        timedListener = l
    }


    fun initListener(l : ((Int , View ) -> Unit )){
        listener = l
    }

    fun getArray() = answerArray

}