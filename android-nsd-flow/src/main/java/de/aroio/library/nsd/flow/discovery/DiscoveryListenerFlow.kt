package de.aroio.library.nsd.flow.discovery

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.sendBlocking

@ExperimentalCoroutinesApi
internal data class DiscoveryListenerFlow(
        private val producerScope: ProducerScope<DiscoveryEvent>,
) : NsdManager.DiscoveryListener {

    override fun onServiceFound(serviceInfo: NsdServiceInfo) {
        producerScope.sendBlocking(DiscoveryEvent.DiscoveryServiceFound(service = serviceInfo))
    }

    override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
        producerScope.channel.close()
    }

    override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
        producerScope.channel.close()
    }

    override fun onDiscoveryStarted(serviceType: String) {
        producerScope.sendBlocking(DiscoveryEvent.DiscoveryStarted(registeredType = serviceType))
    }

    override fun onDiscoveryStopped(serviceType: String) {
        producerScope.sendBlocking(DiscoveryEvent.DiscoveryStopped(serviceType = serviceType))
        producerScope.channel.close()
    }

    override fun onServiceLost(service: NsdServiceInfo) {
        producerScope.sendBlocking(DiscoveryEvent.DiscoveryServiceLost(service = service))
    }

}