package com.example.pmuprojekat.aktivnosti

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.pmuprojekat.R
import com.example.pmuprojekat.apihandlers.NorthwindApiHandler
import com.example.pmuprojekat.data.ApiRoutes
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.data.Product
import com.example.pmuprojekat.viewadapters.CategoryViewAdapter
import com.example.pmuprojekat.viewadapters.ProductViewAdapter
import com.example.pmuprojekat.viewmodels.ProductViewModel
import android.util.Log
import android.widget.*
import androidx.lifecycle.LifecycleCoroutineScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*

class ProizvodiActivity : AppCompatActivity() {
    val viewModel: ProductViewModel by viewModels()
    var productViewAdapter:ProductViewAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proizvodi)
        dohvatiProizvode(this,ApiRoutes.products)
    }
    private fun dohvatiProizvode(ctx: Context, sUrl: String): List<Product>? {
        var products: List<Product>? = null
        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)

            if (result != null) {
                try {
                    val gson= Gson()
                    val typeToken=object: TypeToken<List<Product>>() {}.type
                    products=gson.fromJson(result,typeToken)

                    withContext(Dispatchers.Main) {
                        viewModel.lstProizvodi.value = products
                        val lstProizvodiView = findViewById<RecyclerView>(R.id.lstProizvodiView)
                        lstProizvodiView.layoutManager = LinearLayoutManager(ctx)
                        productViewAdapter = ProductViewAdapter(ctx, viewModel.lstProizvodi)
                        lstProizvodiView.adapter = productViewAdapter
                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }

        }
        return products
    }

    fun prikaziUnosProizvoda(view: View) {
        setContentView(R.layout.product_item)
        val productButton = findViewById<Button>(R.id.proizvodAkcija)
        productButton.text = "Dodaj"

        val nazivProizvoda = findViewById<EditText>(R.id.nazivProizvoda)
        val cenaProizvoda = findViewById<EditText>(R.id.cenaProizvoda)
        val ukNaLageru = findViewById<EditText>(R.id.ukupnoNaLageru)
        val discon = findViewById<CheckBox>(R.id.chkDiscontinuedID)

        val naslovNazivProizvoda=findViewById<TextView>(R.id.dpNazivProizvoda)
        var defColor:Int=naslovNazivProizvoda.currentTextColor

        var mojSpinner:Spinner?=null
        val tmpContext:Context=getApplicationContext()

        var izborKategorija:List<Category>?=null

        lifecycleScope.launch(Dispatchers.IO){
            try {

                val apiHandler = NorthwindApiHandler()
                val result2 = apiHandler.getRequest(ApiRoutes.categories)
                Log.d("Test",result2.toString())
                if (result2 != null) {
                    izborKategorija= Klaxon().parseArray(result2)

                    for(el in izborKategorija!!)
                    {
                        Log.d("Test",el.toString())
                    }


                    withContext(Dispatchers.Main){
                        mojSpinner=findViewById<Spinner>(R.id.kategorijaListaSpinner)
                        var mojAdapter=ArrayAdapter<Category>(tmpContext, android.R.layout.simple_spinner_item,izborKategorija!!)
                        mojAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        mojSpinner!!.adapter=mojAdapter

                        productButton.setOnClickListener {

                            if (nazivProizvoda.text.toString().trim().length==0 ){
                                naslovNazivProizvoda.setTextColor(Color.RED)
                                return@setOnClickListener
                            }
                            if(naslovNazivProizvoda.currentTextColor==Color.RED)
                                naslovNazivProizvoda.setTextColor(defColor)

                            var kat:Category= mojSpinner!!.selectedItem as Category

                            val noviProizvod = Product(0,nazivProizvoda.text.toString(),kat.categoryId,cenaProizvoda.text.toString().toDouble(),ukNaLageru.text.toString().toInt(),discon.isChecked)

                            lifecycleScope.launch(Dispatchers.IO) {
                                try {

                                    val apiHandler = NorthwindApiHandler()
                                    val result = apiHandler.postRequest(
                                        ApiRoutes.products,
                                        Gson().toJson(noviProizvod)
                                    )
                                    if (result != null) {
                                        withContext(Dispatchers.Main) {
                                            Toast.makeText(getApplicationContext(), "Usepsno dodavanje proizvoda", Toast.LENGTH_LONG)
                                                .show()
                                        }

                                    } else {
                                        print("Error: Get request returned no response")
                                    }
                                } catch (err: Error) {
                                    print("Error when parsing JSON: " + err.localizedMessage)
                                }
                            }
                        }
                    }
                } else {
                    print("Error: Get request returned no response")
                }
            } catch (err: Error) {
                print("Error when parsing JSON: " + err.localizedMessage)
            }
        }






    }



}