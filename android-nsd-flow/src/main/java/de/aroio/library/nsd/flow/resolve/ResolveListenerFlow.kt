package de.aroio.library.nsd.flow.resolve

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.sendBlocking
import java.util.concurrent.CancellationException

internal class ResolveListenerFlow(
        private val producerScope: ProducerScope<ResolveEvent>
) : NsdManager.ResolveListener {
    override fun onResolveFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        producerScope.channel.close()
    }

    override fun onServiceResolved(nsdServiceInfo: NsdServiceInfo) {
        producerScope.sendBlocking(ResolveEvent.ServiceResolved(nsdServiceInfo))
    }
}