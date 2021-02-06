package de.aroio.library.nsd.flow.resolve

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import de.aroio.library.nsd.flow.ResolveFailed
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.sendBlocking

internal class ResolveListenerFlow(
        private val producerScope: ProducerScope<ResolveEvent>
) : NsdManager.ResolveListener {
    override fun onResolveFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        producerScope.channel.close(cause = ResolveFailed(nsdServiceInfo, errorCode))
    }

    override fun onServiceResolved(nsdServiceInfo: NsdServiceInfo) {
        producerScope.sendBlocking(ResolveEvent.ServiceResolved(nsdServiceInfo))
    }
}