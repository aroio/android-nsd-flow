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
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryServiceFound
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryServiceLost
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    private lateinit var toggleButton: Button
    private lateinit var statusTextView: TextView
    private lateinit var recyclerView: RecyclerView

    private val nsdManagerRx: NsdManagerRx by lazy { NsdManagerRx(this) }
    private val adapter: DiscoveryAdapter = DiscoveryAdapter()
    private var subscription: Disposable = Disposables.disposed()

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
                if (subscription.isDisposed) R.string.activity_main_status_discovery_off
                else R.string.activity_main_status_discovery_on)
    }

    private fun toggle() {
        if (subscription.isDisposed) startDiscovery()
        else stopDiscovery()
    }

    private fun startDiscovery() {
        subscription = nsdManagerRx.discoverServices(DiscoveryConfiguration("_services._dns-sd._udp"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { event: DiscoveryEvent ->
                            Log.d(TAG, "Event $event")
                            when (event) {
                                is DiscoveryServiceFound -> adapter.addItem(event.service.toDiscoveryRecord())
                                is DiscoveryServiceLost  -> adapter.removeItem(event.service.toDiscoveryRecord())
                            }
                        },
                        { Log.e(TAG, "Error starting discovery.", it) })

        updateUI()
    }

    private fun stopDiscovery() {
        subscription.dispose()
        updateUI()
        adapter.clear()
    }

    companion object {
        private const val TAG = "MainActivity"
    }

}