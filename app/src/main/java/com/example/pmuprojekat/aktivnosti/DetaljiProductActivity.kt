package com.example.pmuprojekat.aktivnosti

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.beust.klaxon.Klaxon
import com.example.apitest.viewmodels.CategoryViewModel
import com.example.pmuprojekat.R
import com.example.pmuprojekat.apihandlers.NorthwindApiHandler
import com.example.pmuprojekat.data.ApiRoutes
import com.example.pmuprojekat.data.Category
import com.example.pmuprojekat.data.Product
import com.example.pmuprojekat.viewadapters.CategoryViewAdapter
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetaljiProductActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.product_item)
        val g_proizvodID=intent.extras!!["proizvodID"]
        dohvatiProizvode(this,ApiRoutes.products + "/${g_proizvodID}")

    }


    fun snimanjeIzmena(idProduct:Int,productName:String,idCategory:Int,unitPrice:Double,unitsInStock:Int,discontinued:Boolean) {
        val izmenjenProizvod = Product(idProduct,productName,idCategory,unitPrice,unitsInStock,discontinued)


        lifecycleScope.launch(Dispatchers.IO) {
            try {
                //Klaxon().toJsonString(izmenjenProizvod)
                val apiHandler = NorthwindApiHandler()
                Log.d("Test",idProduct.toString())
                Log.d("Test",Gson().toJson(izmenjenProizvod))
                Log.d("Test",Klaxon().toJsonString(izmenjenProizvod))

                val result = apiHandler.putRequest(
                    ApiRoutes.products + "/${idProduct}",
                    Gson().toJson(izmenjenProizvod)

                )
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getApplicationContext(),
                            "Usepsno izmenjeni podaci proizvoda",
                            Toast.LENGTH_LONG
                        )
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

    fun obrisiProizvod(idBrisi: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiHandler = NorthwindApiHandler()
                val result = apiHandler.deleteRequest(ApiRoutes.products + "/${idBrisi}")
                if (result != null) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            getApplicationContext(),
                            "Usepsno obrisan proizvod",
                            Toast.LENGTH_LONG
                        )
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

    fun dohvatiProizvode(contextM: Context,sUrl: String): Product? {
        var product: Product? = null
        var izborKategorija:MutableList<Category>?=null
        lifecycleScope.launch(Dispatchers.IO) {
            val apiHandler = NorthwindApiHandler()
            val result = apiHandler.getRequest(sUrl)
            val result2= apiHandler.getRequest(ApiRoutes.categories)

            if (result != null && result2 !=null) {
                try {
                    // Parse result string JSON to data class

                    product = Gson().fromJson(result,Product::class.java)
                    var tmpLista:List<Category>?=Klaxon().parseArray(result2)
                    izborKategorija= tmpLista as MutableList<Category>?
                    var mojAdapter=ArrayAdapter<Category>(contextM, android.R.layout.simple_spinner_item,izborKategorija!!)
                    mojAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)



                    withContext(Dispatchers.Main) {
                        val nazivProizvoda = findViewById<EditText>(R.id.nazivProizvoda)
                        nazivProizvoda.setText(product!!.productName)

                        val cenaProizvoda = findViewById<EditText>(R.id.cenaProizvoda)
                        cenaProizvoda.setText((product!!.unitPrice).toString())

                        val ukupnoNaLageru= findViewById<EditText>(R.id.ukupnoNaLageru)
                        ukupnoNaLageru.setText((product!!.unitsInStock).toString())

                        val chkDiscontinued= findViewById<CheckBox>(R.id.chkDiscontinuedID)
                        chkDiscontinued.isChecked=product!!.discontinued

                        val mojSpinner=findViewById<Spinner>(R.id.kategorijaListaSpinner)
                        mojSpinner.adapter=mojAdapter

                        var brojac:Int=0
                        for(el in izborKategorija!!)
                        {
                            if (el.categoryId==product!!.categoryId) {
                                mojSpinner.setSelection(brojac)
                                break
                            }
                            brojac+=1
                        }




                        val brisanjeProizvoda = findViewById<Button>(R.id.proizvodAkcija2)
                        brisanjeProizvoda.text = "Obrisi"
                        brisanjeProizvoda.isVisible = true
                        brisanjeProizvoda.setOnClickListener { obrisiProizvod(product!!.productId) }
                        val snimiIzmene = findViewById<Button>(R.id.proizvodAkcija)
                        snimiIzmene.text = "Snimi"

                        snimiIzmene.setOnClickListener {
                            var kat:Category= mojSpinner.selectedItem as Category
                            Log.d("Test",kat.categoryId.toString())
                            snimanjeIzmena(product!!.productId,nazivProizvoda.text.toString(),kat.categoryId,cenaProizvoda.text.toString().toDouble(),ukupnoNaLageru.text.toString().toInt(),chkDiscontinued.isChecked)


                        }

                    }
                } catch (err: Error) {
                    print("Error when parsing JSON: " + err.localizedMessage)
                }
            } else {
                print("Error: Get request returned no response")
            }

        }
        return product
    }






}