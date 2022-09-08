package com.example.themoviesapp.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.themoviesapp.databinding.ActivityMainBinding
import com.example.themoviesapp.viewmodel.ViewModelMovies
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(){

    // Binding
    private lateinit var binding : ActivityMainBinding

    // ViewModel
    private val viewModel: ViewModelMovies by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve NavController from the NavHostFragment
        val navHostFragment = binding.navHostFragments.getFragment() as NavHostFragment
        val navController = navHostFragment.navController
        binding.bnvMainActivity.setupWithNavController(navController)

        initComponents()

        //registerReceiver(ConnectivityReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    private fun initComponents() {
        // retrieve favorite movies id from firebase
        viewModel.dataFirebase()
    }

    /* override fun onResume() {
         super.onResume()
         ConnectivityReceiver.connectivityReceiverListener = this
     }

     override fun onNetworkConnectionChanged(isConnected: Boolean) {
         showNetworkMessage(isConnected)
     }

     private fun showNetworkMessage(isConnected: Boolean){
         if (isConnected){
             viewModel.connectionInternet(true)
             binding.llNoInternet.visibility = View.GONE
             binding.ivLoadContent.visibility = View.VISIBLE
         } else {
             viewModel.connectionInternet(false)
             showSnackBar("No connection", resources.getColor(R.color.warn_red))
             binding.llNoInternet.visibility = View.VISIBLE
             binding.ivLoadContent.visibility = View.GONE
         }
     }

     /*private fun getGuestSessionId(){
         CoroutineScope(Dispatchers.IO).launch {
             val call = getRetrofit(APIService.urlAuthentication).create(APIService::class.java).getGuestSessionID(
                 APIService.APIkey)
             val guestSession = call.body()
             runOnUiThread {
                 if (call.isSuccessful){
                     APIService.guest_session_id = guestSession!!.guest_session_id
                 } else {
                     Toast.makeText(this@MainActivity, "We could not get the guest session ID ", Toast.LENGTH_SHORT).show()
                 }
             }
         }
     }*/

     */
}