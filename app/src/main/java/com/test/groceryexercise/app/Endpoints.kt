package com.test.groceryexercise.app

class Endpoints {
    companion object{
        private const val URL_CATEGORY = "category"
        private const val URL_SUBCATEGORY = "subcategory"
        private const val URL_PRODUCTS = "products"
        private const val URL_PRODUCT_SUB = "sub"
        private const val URL_AUTH = "auth"
        private const val URL_REGISTER = "${URL_AUTH}/register"
        private const val URL_LOGIN = "${URL_AUTH}/login"


        const val categories = "${Config.API_BASE}/${URL_CATEGORY}"
        const val register = "${Config.API_BASE}/${URL_REGISTER}"
        const val login = "${Config.API_BASE}/${URL_LOGIN}"

        fun subcategoriesByCategory(catId:Int):String = "${Config.API_BASE}/${URL_SUBCATEGORY}/${catId}"
        fun productsBySub(subId:Int):String = "${Config.API_BASE}/${URL_PRODUCTS}/${URL_PRODUCT_SUB}/${subId}"



    }
}