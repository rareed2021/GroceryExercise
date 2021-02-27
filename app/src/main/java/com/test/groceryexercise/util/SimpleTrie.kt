package com.test.groceryexercise.util

import java.util.*


/**
 * A basic general suffix tree implemented using a naive Trie
 * strings are associated with data, and can be searched to get values associated with string
 */
class SimpleSuffixTrie<V>(){

    private val root = TrieNode()


    /**
     * Inserts string and all of its suffixes into tree
     * @key [String] to insert
     * @value [V] associated with this string
     */
    fun insert(key:String, value:V){
        val k = key.toLowerCase(Locale.getDefault())
        for(i in key.indices) {
            root.insert(k, i, value)
        }
    }

    /**
     * Gets set of all V associated with this string
     */
    fun get(key:String):Set<V>{
        val k = key.toLowerCase(Locale.getDefault())
        return root.find(k,0)?:setOf()
    }

    /**
     * Numbre of distinct values in suffix tree.
     */
    val size : Int
        get()=root.mData.size


    //need to think about this more. Removing substrings is dangerous if same-key string gets added with substrings
    //for my use case here, that shouldn't matter
    //not that I intend to remove anyway
    /**
     * Removes string (and all of its substrings
     * @value If set to null, remove for all possible values. May be dangerous
     */
    fun remove(key:String, value:V?=null){
        val k = key.toLowerCase(Locale.getDefault())
        for(i in key.indices) {
            root.remove(k, i, value)
        }
    }




    internal inner class TrieNode(){
        private val children = mutableMapOf<Char,TrieNode>()
        val mData = mutableSetOf<V>()
        fun insert(s:String, current:Int,value:V){
            if(current<s.length){
                val next : TrieNode = children.getOrPut(s[current], {TrieNode()})
                next.insert(s,current+1,value)
            }
            mData.add(value)
        }
        fun find(s:String, current:Int) : Set<V>?{
            return if(current<s.length){
                val next = children[s[current]]
                next?.find(s,current+1)
            }else{
                mData
            }
        }

        /**
         * @return True if node is safe to delete
         */
        fun remove(s:String,current:Int, value:V?=null):Boolean{
            if(current!=s.length) {
                val next = children[s[current]]
                val canDelete = next?.remove(s, current + 1, value) ?: true
                if (canDelete) {
                    children.remove(s[current])
                }
            }
            if(value!=null){
                //we can assume that if V is found anywhere in the subtree, it will be in the immediate children
                val lastWithValue = children.filter { it.value.mData.contains(value) }.isEmpty()
                if(lastWithValue){
                    mData.remove(value)
                }
            }
            //If there are no remaining children, and no other values are using this substring
            //if value is null, we erase all V in this substring
            return children.isEmpty() && (mData.isEmpty() || value==null)
        }
    }
}