package com.toxicbakery.library.nsd.rx.discovery

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.NsdManagerCompat

data class DiscoveryBinder(
        private val nsdManagerCompat: NsdManagerCompat,
        private val listener: NsdManager.DiscoveryListener
) {

    fun stop() = nsdManagerCompat.stopServiceDiscovery(listener)

}