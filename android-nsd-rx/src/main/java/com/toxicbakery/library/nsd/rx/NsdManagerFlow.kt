package com.toxicbakery.library.nsd.rx

import android.content.Context
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryEvent
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryListenerFlow
import com.toxicbakery.library.nsd.rx.registration.RegistrationConfiguration
import com.toxicbakery.library.nsd.rx.registration.RegistrationEvent
import com.toxicbakery.library.nsd.rx.registration.RegistrationListenerFlow
import com.toxicbakery.library.nsd.rx.resolve.ResolveEvent
import com.toxicbakery.library.nsd.rx.resolve.ResolveListenerFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.withContext

class NsdManagerFlow(private val nsdManagerCompat: NsdManagerCompat) {

    constructor(context: Context) : this(NsdManagerCompatImpl.fromContext(context))

    fun discoverServices(discoveryConfiguration: DiscoveryConfiguration): Flow<DiscoveryEvent> = callbackFlow {
        val discoveryListener = DiscoveryListenerFlow(this)
        nsdManagerCompat.discoverServices(
                serviceType = discoveryConfiguration.type,
                protocolType = discoveryConfiguration.protocolType,
                listener = discoveryListener,
        )
        awaitClose()
    }

    fun registerService(registrationConfiguration: RegistrationConfiguration): Flow<RegistrationEvent> = callbackFlow {
        val registrationListener = RegistrationListenerFlow(this)
        nsdManagerCompat.registerService(
                serviceInfo = NsdServiceInfo().apply {
                    serviceName = registrationConfiguration.serviceName
                    port = registrationConfiguration.port
                    serviceType = registrationConfiguration.serviceType
                },
                protocolType = registrationConfiguration.protocolType,
                listener = registrationListener
        )
        awaitClose()
    }

    fun resolveService(serviceInfo: NsdServiceInfo): Flow<ResolveEvent> = callbackFlow {
        val resolveListener = ResolveListenerFlow(this)
        nsdManagerCompat.resolveService(serviceInfo = serviceInfo, listener = resolveListener)
        awaitClose()
    }

}