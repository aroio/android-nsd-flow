package com.toxicbakery.application.nsd.rx

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toxicbakery.library.nsd.rx.NsdManagerRx
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var toggleButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private val nsdManagerRx: NsdManagerRx by lazy { NsdManagerRx(this) }
    private val adapter: DiscoveryAdapter = DiscoveryAdapter()
    private var subscription: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggleButton = findViewById(R.id.toggle)
        statusTextView = findViewById(R.id.status)
        recyclerView = findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        toggleButton.setOnClickListener { toggle() }
        updateUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopDiscovery()
    }

    private fun updateUI() {
        statusTextView.setText(
                if (subscription.isActive) R.string.activity_main_status_discovery_on
                else R.string.activity_main_status_discovery_off)
    }

    private fun toggle() {
        if (subscription.isActive) stopDiscovery()
        else startDiscovery()
    }

    private fun startDiscovery() {
        subscription = CoroutineScope(Dispatchers.Main).launch {
            nsdManagerRx.discoverServices(DiscoveryConfiguration("_services._dns-sd._udp"))
                    .collect { event ->
                        Log.d(TAG, "Event $event")
                        when (event) {
                            is DiscoveryEvent.DiscoveryServiceFound -> adapter.addItem(event.service.toDiscoveryRecord())
                            is DiscoveryEvent.DiscoveryServiceLost -> adapter.removeItem(event.service.toDiscoveryRecord())
                        }
                    }
        }

        updateUI()
    }

    private fun stopDiscovery() {
        subscription.cancel()
        updateUI()
        adapter.clear()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}