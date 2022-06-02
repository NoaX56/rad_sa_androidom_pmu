package com.example.pmuprojekat.data


data class Category(var categoryId:Int, var categoryName: String, var description: String){
    override fun toString(): String {
        return categoryName
    }
}