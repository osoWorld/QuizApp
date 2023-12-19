package com.example.mindmoneychallenge.AdapterClass

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindmoneychallenge.DataAndModelClass.Category
import com.example.mindmoneychallenge.Quiz.QuizActivity
import com.example.mindmoneychallenge.databinding.CategoryItemBinding
import kotlin.coroutines.coroutineContext

class CategoryAdapter(private var categoryList: ArrayList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(var binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = categoryList.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val dataList = categoryList[position]
        holder.binding.categoryImg.setImageResource(dataList.catImg)
        holder.binding.categoryText.text = dataList.catName

//        holder.itemView.setOnClickListener {
//
////            holder.itemView.context.startActivity(
////                Intent(
////                    holder.itemView.context,
////                    QuizActivity::class.java
////                )
////                    .putExtra("categoryImg", dataList.catImg)
////                    .putExtra("questionType", dataList.catName)
////            )
//        }

        holder.binding.categoryCard.setOnClickListener {
            holder.itemView.context.startActivity(Intent(holder.itemView.context,QuizActivity::class.java)
                .putExtra("categoryImg",dataList.catImg)
                .putExtra("questionType",dataList.catName))
        }
    }
}