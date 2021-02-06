package de.aroio.library.nsd.flow

import android.content.Context
import android.net.nsd.NsdServiceInfo
import de.aroio.library.nsd.flow.discovery.DiscoveryConfiguration
import de.aroio.library.nsd.flow.discovery.DiscoveryEvent
import de.aroio.library.nsd.flow.discovery.DiscoveryListenerFlow
import de.aroio.library.nsd.flow.registration.RegistrationConfiguration
import de.aroio.library.nsd.flow.registration.RegistrationEvent
import de.aroio.library.nsd.flow.registration.RegistrationListenerFlow
import de.aroio.library.nsd.flow.resolve.ResolveEvent
import de.aroio.library.nsd.flow.resolve.ResolveListenerFlow
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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