package de.aroio.application.nsd.rx

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.toxicbakery.application.nsd.rx.R
import de.aroio.library.nsd.flow.NsdManagerFlow
import de.aroio.library.nsd.flow.discovery.DiscoveryConfiguration
import de.aroio.library.nsd.flow.discovery.DiscoveryEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

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
        job = CoroutineScope(Dispatchers.Main).launch {
            nsdManagerFlow.discoverServices(DiscoveryConfiguration("_services._dns-sd._udp"))
                    .flowOn(Dispatchers.IO)
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
        job.cancel()
        adapter.clear()
        updateUI()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}