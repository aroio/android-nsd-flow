package de.aroio.library.nsd.flow.discovery

import android.net.nsd.NsdManager
import de.aroio.library.nsd.flow.NsdManagerCompat

data class DiscoveryBinder(
        private val nsdManagerCompat: NsdManagerCompat,
        private val listener: NsdManager.DiscoveryListener
) {

    fun stop() = nsdManagerCompat.stopServiceDiscovery(listener)

}