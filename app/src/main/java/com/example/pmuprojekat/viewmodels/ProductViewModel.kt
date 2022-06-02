package com.example.pmuprojekat.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pmuprojekat.data.Product

class ProductViewModel: ViewModel() {
    val lstProizvodi=MutableLiveData<List<Product>>()
    val proizvod=MutableLiveData<Product>()
}