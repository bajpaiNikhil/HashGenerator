package com.example.hashgenerator

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.hashgenerator.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    var userName : String    = ""
    var userIpText : String  = ""
    var userIpAlgo : String  = ""
    var userHash : String    = ""
    var userId : Int = 0

    private val homeViewModel : HomeViewModel by viewModels()

    lateinit var db : FirebaseDatabase
    lateinit var auth : FirebaseAuth

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        val hasAlgorithm = resources.getStringArray(R.array.hash_algorithms)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item , hasAlgorithm)
        binding.autoCompleteTextview.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        arguments.let{
            userName = it?.getString("userName").toString()
        }
        Log.d("HOme" , userName)

        db = Firebase.database
        auth = FirebaseAuth.getInstance()
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        binding.generatButton.setOnClickListener {
            onGenerateClicked()
        }

        return binding.root
    }

    private fun onGenerateClicked(){
        if(binding.plainText.text.isEmpty()){
            showSnackBar("Field Empty .")
        }else{
            lifecycleScope.launch {
                applyAnimation()

                navigateToSuccess(getHashData())

                addUserHashToDb()
            }
        }

    }

    private fun addUserHashToDb() {

        val userObj = UserHashDetail(userIpText , userIpAlgo , userHash )
        Log.d("Home" ,"reacged till here $userName")
        val ref = db.getReference("UserDetails").child(userName).child("UserHashDetail")
        ref.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot : DataSnapshot) {
                if(snapshot.exists()){
                    userId=(snapshot.childrenCount.toInt())
                    Log.d("Home" , "count is ${userId.toString()}")
                }
            }

            override fun onCancelled(error : DatabaseError) {}

        })
        Log.d("Home" , "1--"+userId.toString())
        ref.child((userId+1).toString()).setValue(userObj)
    }

    private fun showSnackBar(message : String) {
        val snackBar = Snackbar.make(binding.rootLayout, message, Snackbar.LENGTH_SHORT)
        snackBar.setAction("OKay"){}
        snackBar.setActionTextColor(ContextCompat.getColor(requireContext(),R.color.blue))
        snackBar.show()

    }

    private fun getHashData():String{
        val algorithm = binding.autoCompleteTextview.text.toString()
        val plainText = binding.plainText.text.toString()
        userIpText = plainText
        userIpAlgo = algorithm
        Log.d("HOme" , "$userIpText $userIpAlgo")

        return homeViewModel.getHash(plainText,algorithm)
    }

    private suspend fun applyAnimation() {
        binding.generatButton.isClickable = false
        binding.titleTextView.animate().alpha(0f).duration  = 400L
        binding.generatButton.animate().alpha(0f).duration = 400L
        binding.textInputLayout.animate()
            .alpha(0f)
            .translationXBy(1200f)
            .duration = 400L

        binding.plainText.animate()
            .alpha(0f)
            .translationXBy(-1200f)
            .duration = 400L

        delay(300)

        binding.successBackground.animate().alpha(1f).duration = 600L
        binding.successBackground.animate().rotationBy(720f).duration = 600L
        binding.successBackground.animate().scaleXBy(900f).duration = 800L
        binding.successBackground.animate().scaleYBy(900f).duration = 800L

        delay(500)

        binding.successImageView.animate().alpha(1f).duration = 1000L
        delay(2000)
    }

    private fun navigateToSuccess(hash:String) {
        val bundle = bundleOf("hash" to hash)
        userHash = hash.replace(", " , "")
        Log.d("HOme" , "$userIpText $userIpAlgo $userHash $userName")
        findNavController().navigate(R.id.action_homeFragment_to_successFragment , bundle)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu , menu)
    }

    override fun onOptionsItemSelected(item : MenuItem) : Boolean {
        when(item.itemId){
            R.id.clear_menu ->{
                binding.plainText.setText("")
                showSnackBar("Cleared. Cheers!!!!")
            }
            R.id.sign_Out ->{
                showSnackBar("Logout!!!")
                auth.signOut()
                findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
            R.id.view ->{
                val bundle = bundleOf("userName" to userName)
                findNavController().navigate(R.id.action_homeFragment_to_viewFragment , bundle)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

class UserHashDetail(){
    var userInputText : String = ""
    var userInputAlgorithm : String = ""
    var userOutputHash : String = ""
    constructor(
        userInputText : String,
        userInputAlgorithm : String,
        userOutputHash : String,
    ): this(){
        this.userInputText = userInputText
        this.userInputAlgorithm = userInputAlgorithm
        this.userOutputHash = userOutputHash
    }
}
