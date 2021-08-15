package com.example.hashgenerator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ViewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1 : String? = null
    private var param2 : String? = null
    private var userName : String? = null
    lateinit var recyclerView : RecyclerView

    lateinit var userHashArrayList : ArrayList<userHash>

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
            userName = it.getString("userName")
        }
    }

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view, container, false)
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("view" , "userName is reached tilll here$userName")
        userHashArrayList = arrayListOf()
        recyclerView = view.findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        getUserHash()
    }

    private fun getUserHash() {
        val ref  = FirebaseDatabase.getInstance().getReference("UserDetails")
            .child(userName!!).child("UserHashDetail")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    for(userhash in snapshot.children){
                        val user =  userhash.getValue(userHash::class.java)
                        userHashArrayList.add(user!!)
                        Log.d("view" , "array list is $userHashArrayList")
                        recyclerView.adapter = UserHashAdapter(userHashArrayList)

                    }
                }
            }

            override fun onCancelled(error : DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

data class userHash(
    var userInputText : String = "",
    var userInputAlgorithm : String = "",
    var userOutputHash : String = ""
)