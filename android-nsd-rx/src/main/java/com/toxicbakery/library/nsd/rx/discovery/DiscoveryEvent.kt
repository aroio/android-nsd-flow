package com.toxicbakery.library.nsd.rx.discovery

import android.net.nsd.NsdServiceInfo

sealed class DiscoveryEvent {
    data class DiscoveryServiceFound(val service: NsdServiceInfo) : DiscoveryEvent()
    data class DiscoveryServiceLost(val service: NsdServiceInfo) : DiscoveryEvent()
    data class DiscoveryStarted(val registeredType: String) : DiscoveryEvent()
    data class DiscoveryStopped(val serviceType: String) : DiscoveryEvent()
}
