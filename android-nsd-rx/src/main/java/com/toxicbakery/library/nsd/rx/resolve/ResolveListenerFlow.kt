package com.toxicbakery.library.nsd.rx.resolve

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.ProducerScope
import java.util.concurrent.CancellationException

internal class ResolveListenerFlow(
        private val producerScope: ProducerScope<ResolveEvent>
) : NsdManager.ResolveListener {
    override fun onResolveFailed(nsdServiceInfo: NsdServiceInfo, errorCode: Int) {
        producerScope.cancel(CancellationException("Resolving failed with errorCode $errorCode for serviceInfo $nsdServiceInfo"))
    }

    override fun onServiceResolved(nsdServiceInfo: NsdServiceInfo) {
        producerScope.offer(ResolveEvent.ServiceResolved(nsdServiceInfo))
    }
}