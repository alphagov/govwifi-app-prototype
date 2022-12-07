package com.example.myapplication

import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Declaring Button and TextView
        // 1. Changes the state of Wi-Fi on button click
        // 2. Shows the state of the Wi-Fi
        val btn = findViewById<Button>(R.id.wifiSwitch)
        val textView = findViewById<TextView>(R.id.tv)

        // Declaring Wi-Fi manager
        val wifi = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        // On button Click
        btn.setOnClickListener {

            // wifi.isWifiEnabled is a boolean, is Wi-Fi is ON,
            // it switches down and vice-versa
            wifi.isWifiEnabled = !wifi.isWifiEnabled

            // For displaying Wi-fi status in TextView
            if (!wifi.isWifiEnabled) {
                textView.text = "Wifi is OFF"
            } else {
                textView.text = "Wifi is ON"
            }
        }
    }
}