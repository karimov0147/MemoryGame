package com.example.memorygame.ui.adapters

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.R
import com.example.memorygame.databinding.ItemTwoBinding
import com.example.memorygame.utills.ImageModel

class RememberAdapter : RecyclerView.Adapter<RememberAdapter.ViewHolder>() {

    private var array = IntArray(15){0}
    lateinit var listener : ((Int , View ) -> Unit )
    lateinit var timedListener: ((Int , View) -> Unit)
    private var selectedList = Array(15){false}
    private var answerArray = ArrayList<Int>(6)
    var clickable = true


    inner class ViewHolder(var view : ItemTwoBinding) : RecyclerView.ViewHolder(view.root){


        fun bind(){

            if (array[bindingAdapterPosition] != 0) {
                answerArray.add(array[bindingAdapterPosition])
                view.root.setBackgroundResource(R.drawable.item_special_bg)
                timedListener.invoke(array[bindingAdapterPosition], view.root)
            }

            itemView.rootView.setOnClickListener {

                if (clickable){
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
        ItemTwoBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = array.size

    fun submitList(newArray : IntArray){
        array = newArray
        notifyDataSetChanged()
    }

    fun initTimedListener( l : ((Int , View) -> Unit)){
        timedListener = l
    }

    @JvmName("setClickable1")
    fun setClickable(state: Boolean){
        this.clickable = state
    }


    fun initListener(l : ((Int , View ) -> Unit )){
        listener = l
    }

    fun getArray() = answerArray

}