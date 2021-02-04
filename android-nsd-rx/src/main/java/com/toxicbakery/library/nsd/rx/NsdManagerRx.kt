package com.toxicbakery.library.nsd.rx

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryEvent
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryListenerFlow
import com.toxicbakery.library.nsd.rx.registration.RegistrationConfiguration
import com.toxicbakery.library.nsd.rx.registration.RegistrationEvent
import com.toxicbakery.library.nsd.rx.registration.RegistrationListenerRx
import com.toxicbakery.library.nsd.rx.resolve.ResolveEvent
import com.toxicbakery.library.nsd.rx.resolve.ResolveListenerRx
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

@ExperimentalCoroutinesApi
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

    fun discoverServices(
            discoveryConfiguration: DiscoveryConfiguration,
            listenerFactory: (NsdManagerCompat, ObservableEmitter<DiscoveryEvent>) -> NsdManager.DiscoveryListener
    ): Observable<DiscoveryEvent> = Observable.create<DiscoveryEvent> { emitter: ObservableEmitter<DiscoveryEvent> ->
        val listener = listenerFactory(nsdManagerCompat, emitter)
        emitter.setCancellable { nsdManagerCompat.stopServiceDiscovery(listener) }
        nsdManagerCompat.discoverServices(
                serviceType = discoveryConfiguration.type,
                protocolType = discoveryConfiguration.protocolType,
                listener = listener
        )
    }

    fun registerService(discoveryConfiguration: RegistrationConfiguration): Observable<RegistrationEvent> =
            registerService(discoveryConfiguration) { nsdManagerCompat, emitter ->
                RegistrationListenerRx(nsdManagerCompat, emitter)
            }

    fun registerService(
            registrationConfiguration: RegistrationConfiguration,
            listenerFactory: (NsdManagerCompat, ObservableEmitter<RegistrationEvent>) -> NsdManager.RegistrationListener
    ): Observable<RegistrationEvent> = Observable.create<RegistrationEvent> { emitter: ObservableEmitter<RegistrationEvent> ->
        val listener = listenerFactory(nsdManagerCompat, emitter)
        emitter.setCancellable { nsdManagerCompat.unregisterService(listener) }
        val nsdServiceInfo = NsdServiceInfo().apply {
            serviceName = registrationConfiguration.serviceName
            serviceType = registrationConfiguration.serviceType
            port = registrationConfiguration.port
        }
        nsdManagerCompat.registerService(nsdServiceInfo, registrationConfiguration.protocolType, listener)
    }

    fun resolveService(serviceInfo: NsdServiceInfo): Observable<ResolveEvent> =
            resolveService(serviceInfo) { emitter -> ResolveListenerRx(emitter) }

    fun resolveService(
            serviceInfo: NsdServiceInfo,
            listenerFactory: (ObservableEmitter<ResolveEvent>) -> NsdManager.ResolveListener
    ): Observable<ResolveEvent> = Observable.create<ResolveEvent> { emitter: ObservableEmitter<ResolveEvent> ->
        nsdManagerCompat.resolveService(serviceInfo, listenerFactory(emitter))
    }

}