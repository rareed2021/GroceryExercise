package com.test.groceryexercise.app

class Endpoints {
    companion object{
        private const val URL_ORDER = "orders"
        private const val URL_ADDRESS = "address"
        private const val URL_CATEGORY = "category"
        private const val URL_SUBCATEGORY = "subcategory"
        private const val URL_PRODUCTS = "products"
        private const val URL_PRODUCT_SUB = "sub"
        private const val URL_AUTH = "auth"
        private const val URL_USER = "users"
        private const val URL_REGISTER = "${URL_AUTH}/register"
        private const val URL_LOGIN = "${URL_AUTH}/login"


        const val categories = "${Config.API_BASE}/${URL_CATEGORY}"
        const val register = "${Config.API_BASE}/${URL_REGISTER}"
        const val login = "${Config.API_BASE}/${URL_LOGIN}"

        const val addressPost = "${Config.API_BASE}/${ URL_ADDRESS}/"
        const val orderPost = "${Config.API_BASE}/$URL_ORDER/"

        fun putUserById(userId:String) = "${Config.API_BASE}/$URL_USER/$userId"


        fun subcategoriesByCategory(catId:Int):String = "${Config.API_BASE}/${URL_SUBCATEGORY}/${catId}"
        fun productsBySub(subId:Int):String = "${Config.API_BASE}/${URL_PRODUCTS}/${URL_PRODUCT_SUB}/${subId}"
        fun addressesById(userId:String):String = "${Config.API_BASE}/${URL_ADDRESS}/$userId"
        fun ordersByUserId(userId:String):String = "${Config.API_BASE}/$URL_ORDER/$userId"



    }
}