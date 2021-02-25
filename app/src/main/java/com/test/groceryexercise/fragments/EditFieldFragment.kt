package com.test.groceryexercise.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.test.groceryexercise.R
import com.test.groceryexercise.activities.AddressListActivity
import com.test.groceryexercise.activities.EditFieldActivity
import com.test.groceryexercise.models.User
import kotlinx.android.synthetic.main.fragment_edit_field.view.*
import java.io.Serializable

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_TITLE = "TITLE"
private const val ARG_OBJECT = "OBJECT"
private const val ARG_FIELD = "FIELD"

/**
 * A simple [Fragment] subclass.
 * Use the [EditFieldFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFieldFragment : Fragment() {
    private var title: String? = null
    private var mObject: Serializable? = null
    private var field:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            mObject = it.getSerializable(ARG_OBJECT)
            field  = it.getString(ARG_FIELD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_field, container, false)
        init(view)
        return view
    }

    private fun init(view: View) {
        view.text_title.text = title
        val obj = mObject
        when(obj){
            is User ->view.text_content.text = obj.fieldString(field?:"")
            is String -> view.text_content.text = obj
        }
        view.setOnClickListener {
            val obj = mObject
            when(obj){
                is String->if(field == "address"){
                    startActivity(Intent(activity, AddressListActivity::class.java))
                }
                is User -> {
                    val intent = Intent(activity, EditFieldActivity::class.java)
                    intent.putExtra(ARG_TITLE, title)
                    intent.putExtra(ARG_OBJECT,mObject)
                    intent.putExtra(ARG_FIELD, field)
                    startActivity(intent)
                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param title Parameter 1.
         * @param obj Parameter 2.
         * @return A new instance of fragment EditFieldFragment.
         */
        @JvmStatic
        fun newInstance(title: String, obj: Serializable, field:String?) =
            EditFieldFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putSerializable(ARG_OBJECT, obj)
                    putString(ARG_FIELD, field)
                }
            }
    }
}