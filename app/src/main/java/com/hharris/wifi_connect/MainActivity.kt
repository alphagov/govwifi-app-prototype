package com.hharris.wifi_connect

// Check internet connection dependencies

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var connectButton: Button
    private lateinit var ssid: String
    private lateinit var ssidPassword: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ssid = getString(R.string.SSID);
        ssidPassword = getString(R.string.SSID_PASSWORD)

        Log.i(TAG, ssid)
        Log.i(TAG, ssidPassword)

        wifiManager = applicationContext.getSystemService()!!
        connectButton = findViewById(R.id.connect_button)
        val checkButton: Button = findViewById(R.id.buttonCheck)
        val disconnectButton: Button = findViewById(R.id.disconnectButton)

        connectButton.setOnClickListener {

            if (checkForWifi(this)) {
                Toast.makeText(this, "Already connected to the WiFi Network!!!", Toast.LENGTH_SHORT).show()
            } else {
                connectToWifi(ssid, ssidPassword)
            }

        }

        checkButton.setOnClickListener {
            if (checkForWifi(this)) {
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show()
            }
        }

        disconnectButton.setOnClickListener {
            disconnectFromNetwork(ssid, ssidPassword)
    }

}

        private fun connectToWifi(ssid: String, password: String) {

            // Log checker executed
            Log.i(TAG, "Log checker executed")

            // Create a WifiNetworkSuggestion object with the provided SSID and password
            val suggestion1 = WifiNetworkSuggestion.Builder()
                .setSsid(ssid)
                .setWpa2Passphrase(password)
                // Optional (Needs location permission)
                .setIsAppInteractionRequired(true)
                .build()

            // Create a MutableList to hold the WifiNetworkSuggestion
            val suggestionsList: MutableList<WifiNetworkSuggestion> = ArrayList()
            suggestionsList.add(suggestion1)

            // Log the suggestions list
            Log.i(TAG, "$suggestionsList")

            // Get the WifiManager system service
            val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

            // Add the suggestions list to the wifi manager
            val status = wifiManager.addNetworkSuggestions(suggestionsList)

            // Log the wifi manager and the status
            Log.i(TAG, "$wifiManager")
            Log.i(TAG, "$status")

            // Check if the operation was not successful
            if (status != WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS) {
                // Error handling
                Log.i(TAG, "Couldn't connect")
            }

            // Create an IntentFilter to listen for the "WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION" intent
            val intentFilter = IntentFilter(WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION)

            // Create a BroadcastReceiver to receive the intent
            val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    // Check if the intent action is the expected one
                    if (intent.action != WifiManager.ACTION_WIFI_NETWORK_SUGGESTION_POST_CONNECTION) {
                        return
                    }
                    // Post connection
                    Log.i(TAG, "Post connection")
                }
            }

            // Register the BroadcastReceiver
            applicationContext.registerReceiver(broadcastReceiver, intentFilter)

            // Start the wifi settings activity
            startActivity(Intent(Settings.Panel.ACTION_WIFI))

            Log.i(TAG, "$intentFilter")
        }

    private fun checkForWifi(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false
            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.type == ConnectivityManager.TYPE_WIFI && networkInfo.isConnected
        }
    }

    private fun disconnectFromNetwork(ssid: String, password: String) {

        val wifiManager = applicationContext.getSystemService(AppCompatActivity.WIFI_SERVICE) as WifiManager

        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> {
                val suggestion = WifiNetworkSuggestion.Builder()
                    .setSsid(ssid)
                    .setWpa2Passphrase(password)
                    .build()
                val suggestionsList: MutableList<WifiNetworkSuggestion> = ArrayList()
                suggestionsList.add(suggestion)
                wifiManager.removeNetworkSuggestions(suggestionsList)
            }
        }
    }

}




/*

This code defines the MainActivity class, which is the starting point of the app. The class is
defined as a subclass of AppCompatActivity, which is a part of the AndroidX library and provides
a compatibility layer for newer features on older versions of Android.

The onCreate (savedInstanceState, Bundle) method:

This method is called when the activity is first created, and it is used to set
up the initial state of the activity. This method takes a single parameter named savedInstanceState,
which is a Bundle object that contains any data that was saved from the activity when it was last
destroyed. This is where you would typically initialize the layout, set up any listeners, and
perform other setup tasks.

The setContentView method: This method sets the layout file that will be used to display the
activity on the screen.

The findViewById method: This method is used to look up views in the layout file and assign them to
variables that can be used in the code.


applicationContext is a property of the Context class in Android that represents the global
application context. This context is used to access resources and services that are not
associated with any particular activity or service.

 */
