package com.lll.demo.fanlayoutmanager.models

enum class Country(val country: String) {
    ITALY("Italy"),
    USA("United States"),
    ROK("South Korea");

    fun getCountryString():String= country
}