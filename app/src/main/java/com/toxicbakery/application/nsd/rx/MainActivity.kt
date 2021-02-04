package com.toxicbakery.application.nsd.rx

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toxicbakery.library.nsd.rx.NsdManagerFlow
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

class MainActivity : AppCompatActivity() {

    private lateinit var toggleButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private val nsdManagerFlow: NsdManagerFlow by lazy { NsdManagerFlow(this) }
    private val adapter: DiscoveryAdapter = DiscoveryAdapter()
    private var job: Job = Job().apply { cancel() }


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
        when {
            job.isActive -> R.string.activity_main_status_discovery_on
            else -> R.string.activity_main_status_discovery_off
        }.also { statusTextView.setText(it) }
    }

    private fun toggle() {
        if (job.isActive) stopDiscovery()
        else startDiscovery()
    }

    private fun startDiscovery() {
        job = CoroutineScope(Dispatchers.IO).launch {
            nsdManagerFlow.discoverServices(DiscoveryConfiguration("_services._dns-sd._udp"))
                    .catch { Log.e(TAG, "Error happened", it) }
                    .collectLatest { event ->
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
        job.cancel()
        adapter.clear()
        updateUI()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}