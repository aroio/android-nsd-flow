package com.toxicbakery.library.nsd.rx

import android.content.Context
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryEvent
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryListenerFlow
import com.toxicbakery.library.nsd.rx.registration.RegistrationConfiguration
import com.toxicbakery.library.nsd.rx.registration.RegistrationEvent
import com.toxicbakery.library.nsd.rx.resolve.ResolveEvent
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class NsdManagerRx(private val nsdManagerCompat: NsdManagerCompat) {

    constructor(context: Context) : this(NsdManagerCompatImpl.fromContext(context))

    fun discoverServices(discoveryConfiguration: DiscoveryConfiguration): Flow<DiscoveryEvent> = callbackFlow {
        val discoveryListener = DiscoveryListenerFlow(this)
        nsdManagerCompat.discoverServices(
                serviceType = discoveryConfiguration.type,
                protocolType = discoveryConfiguration.protocolType,
                listener = discoveryListener,
        )
        awaitClose { nsdManagerCompat.stopServiceDiscovery(discoveryListener) }
    }

    fun registerService(discoveryConfiguration: RegistrationConfiguration): Flow<RegistrationEvent> = callbackFlow {

        awaitClose()
    }

    fun resolveService(serviceInfo: NsdServiceInfo): Flow<ResolveEvent> = callbackFlow {

        awaitClose()
    }

}