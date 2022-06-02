package com.example.pmuprojekat.viewadapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.pmuprojekat.R
import com.example.pmuprojekat.aktivnosti.DetaljiProductActivity
import com.example.pmuprojekat.data.Product

class ProductViewAdapter(val ctx: Context, val data: LiveData<List<Product>>): RecyclerView.Adapter<ProductViewAdapter.ProductViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater:LayoutInflater=LayoutInflater.from(ctx)
        val view=inflater.inflate(R.layout.product_row,parent,false)
        return ProductViewHolder(view)
    }
    override fun onBindViewHolder(holder:ProductViewHolder,position:Int){
        holder.bindItems(data.value!!.get(position))
    }
    override fun getItemCount(): Int {
        if(data.value!=null){
            return data.value!!.size
        }
        return 0;
    }

    inner class ProductViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun bindItems(model:Product){
            val prodId=model.productId
            val nam=itemView.findViewById<TextView>(R.id.productName)
            nam.text=model.productName

            if(model.discontinued==true)
                nam.setTextColor(Color.parseColor("#FFA500"))


            val detaljnije=itemView.findViewById<Button>(R.id.detaljnijeProizvod)
            detaljnije.setOnClickListener{
                val intent=Intent(ctx, DetaljiProductActivity::class.java)
                intent.putExtra("proizvodID",prodId)
                ctx.startActivity(intent)
            }
        }
    }
}