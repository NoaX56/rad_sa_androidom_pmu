package com.example.pmuprojekat.viewadapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.apitest.viewmodels.CategoryViewModel
import com.example.pmuprojekat.MainActivity
import com.example.pmuprojekat.R
import com.example.pmuprojekat.aktivnosti.DetaljiKategorijeActivity
import com.example.pmuprojekat.aktivnosti.KategorijeActivity
import com.example.pmuprojekat.data.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newCoroutineContext
import kotlin.coroutines.coroutineContext

class CategoryViewAdapter(val ctx: Context, val data: LiveData<List<Category>>): RecyclerView.Adapter<CategoryViewAdapter.CategoryViewHodler>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHodler {
        val inflater:LayoutInflater= LayoutInflater.from(ctx)
        val view=inflater.inflate(R.layout.category_row,parent,false)
        return CategoryViewHodler(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHodler, position: Int) {
        holder.bindItems(data.value!!.get(position))
    }

    override fun getItemCount(): Int {
        if(data.value!=null){
            return data.value!!.size
        }
        return 0;
    }
    inner class CategoryViewHodler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(model:Category){
            val catId=model.categoryId
            val nam=itemView.findViewById<TextView>(R.id.categoryName)
            nam.text=model.categoryName
            val detaljnije=itemView.findViewById<Button>(R.id.detaljnijeKkategorija)
            detaljnije.setOnClickListener {
                val intent=Intent(ctx,DetaljiKategorijeActivity::class.java)
                intent.putExtra("cId",catId)
                ctx.startActivity(intent)
            }
        }
    }

}



