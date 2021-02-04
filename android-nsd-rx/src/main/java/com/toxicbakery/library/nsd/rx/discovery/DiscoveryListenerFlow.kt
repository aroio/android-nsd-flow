package com.toxicbakery.library.nsd.rx.discovery

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import java.util.concurrent.CancellationException

@ExperimentalCoroutinesApi
internal data class DiscoveryListenerFlow(
        private val producerScope: ProducerScope<DiscoveryEvent>,
) : NsdManager.DiscoveryListener {

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        producerScope.offer(DiscoveryEvent.DiscoveryServiceFound(service = serviceInfo))
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        producerScope.cancel(CancellationException("Stop discovery failed"))
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        producerScope.cancel(CancellationException("StartDiscovery failed"))
    }

    override fun onDiscoveryStarted(serviceType: String) {
        producerScope.offer(DiscoveryEvent.DiscoveryStarted(registeredType = serviceType))
    }

    override fun onDiscoveryStopped(serviceType: String) {
        producerScope.offer(DiscoveryEvent.DiscoveryStopped(serviceType = serviceType))
        producerScope.cancel()
    }

    override fun onServiceLost(service: NsdServiceInfo) {
        producerScope.offer(DiscoveryEvent.DiscoveryServiceLost(service = service))
    }

}