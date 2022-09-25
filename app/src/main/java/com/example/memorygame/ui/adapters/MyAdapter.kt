package com.example.memorygame.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.example.memorygame.R
import com.example.memorygame.databinding.ItemBinding
import com.example.memorygame.utills.ImageModel

class MyAdapter : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    private var array = ArrayList<ImageModel>()
    lateinit var listener : ((ImageModel ) -> Unit )
    private var clicable = true


    inner class ViewHolder(var view : ItemBinding) : RecyclerView.ViewHolder(view.root){


        fun bind(){
            var model = array[bindingAdapterPosition]


                itemView.rootView.setOnClickListener {
                    if (clicable)
                    listener.invoke(ImageModel(model.id , model.image , it))
                }


            view.iconView.setPadding(27)
            view.iconView.setImageResource(R.drawable.questions)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder  = ViewHolder(
        ItemBinding.inflate(LayoutInflater.from(parent.context) , parent , false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 18

    fun submitList(newArray : ArrayList<ImageModel>){
        array.clear()
        array.addAll(newArray)
        notifyDataSetChanged()
    }

    fun initListener(l : ((ImageModel ) -> Unit )){
        listener = l
    }

    fun setClickable(state : Boolean){
        clicable = state
    }


    fun removeItem(id : Int){
        var position = -1
        var position2 = -1

       for (it in 0 until array.size){
            if (array[it].id == id){
                array.removeAt(it)
                position = it
                break
            }
        }
        for (it in 0 until array.size){
            if (array[it].id == id){
                array.removeAt(it)
                position2 = it
                break
            }
        }

        notifyItemRemoved(position)
        notifyItemRemoved(position2)
    }
}