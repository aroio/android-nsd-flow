package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import java.util.concurrent.CancellationException

class RegistrationListenerFlow(
        private val producerScope: ProducerScope<RegistrationEvent>
) : NsdManager.RegistrationListener {
    override fun onRegistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        producerScope.cancel(CancellationException("Registration failed with errorCode $errorCode for NsdServiceInfo $nsdServiceInfo"))
    }

    override fun onUnregistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        producerScope.cancel(CancellationException("Unegistration failed with errorCode $errorCode for NsdServiceInfo $nsdServiceInfo"))
    }

    override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo) {
        producerScope.offer(RegistrationEvent.ServiceRegistered(nsdServiceInfo))
    }

    override fun onServiceUnregistered(nsdServiceInfo: NsdServiceInfo) {
        producerScope.offer(RegistrationEvent.ServiceUnregistered(nsdServiceInfo))
    }

}