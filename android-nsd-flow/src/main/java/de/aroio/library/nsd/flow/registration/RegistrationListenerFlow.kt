package de.aroio.library.nsd.flow.registration

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.sendBlocking
import java.util.concurrent.CancellationException

internal class RegistrationListenerFlow(
        private val producerScope: ProducerScope<RegistrationEvent>
) : NsdManager.RegistrationListener {
    override fun onRegistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        producerScope.channel.close()
    }

    override fun onUnregistrationFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        producerScope.channel.close()
    }

    override fun onServiceRegistered(nsdServiceInfo: NsdServiceInfo) {
        producerScope.sendBlocking(RegistrationEvent.ServiceRegistered(nsdServiceInfo))
    }

    override fun onServiceUnregistered(nsdServiceInfo: NsdServiceInfo) {
        producerScope.sendBlocking(RegistrationEvent.ServiceUnregistered(nsdServiceInfo))
    }

}