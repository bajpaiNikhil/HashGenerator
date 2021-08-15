package com.example.hashgenerator

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.hashgenerator.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginFragment : Fragment() {

    var userName : String    = ""

    lateinit var auth : FirebaseAuth
    lateinit var emailL: EditText
    lateinit var passL: EditText

    private var _binding : FragmentLoginBinding ?= null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments.let{
//            userName = it?.getString("userName").toString()
//        }
    }

    override fun onCreateView(
        inflater : LayoutInflater, container : ViewGroup?,
        savedInstanceState : Bundle?
    ) : RelativeLayout? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        binding?.register?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding?.root
    }

    override fun onViewCreated(view : View, savedInstanceState : Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        emailL = view.findViewById(R.id.email)
        passL = view.findViewById(R.id.password)

        auth = FirebaseAuth.getInstance()
        binding?.login?.setOnClickListener {
            val eMail = emailL.text.toString()
            val passWord = passL.text.toString()
            findUsername()
            Log.d("login" , userName)
            if(userName.isNotEmpty()){
                auth.signInWithEmailAndPassword(eMail,passWord)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            val bundle = bundleOf("userName" to userName)
                            Log.d("login",bundle.toString())
                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment , bundle)
                        }else{
                            Toast.makeText(context, "Something wrong...", Toast.LENGTH_LONG).show()
                        }
                    }
            }


        }
    }

    private fun findUsername(){

        val ref = FirebaseDatabase.getInstance().getReference("UserDetails")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    for(usersnapshot in snapshot.children){
                        val user = usersnapshot.getValue(userDetailis::class.java)
                        Log.d("login11" , "email is ${emailL.text} ," + " ${user?.emailR}")
                        if (emailL.text.toString() == user?.emailR){
                            userName = user.usernameR.toString()
                            Log.d("login11" , userName)
                            break

                        }
                    }
                }
            }

            override fun onCancelled(error : DatabaseError) {

            }
        })
        Log.d("login112" , userName)
//        return userName
    }


}

data class userDetailis(
    val emailR : String? = null,
    val phoneNumberR : String? = null,
    val usernameR : String? = null
)