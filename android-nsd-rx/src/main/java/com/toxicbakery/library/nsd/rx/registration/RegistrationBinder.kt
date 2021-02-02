package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.NsdManagerCompat

data class RegistrationBinder(
        private val nsdManagerCompat: NsdManagerCompat,
        private val listener: NsdManager.RegistrationListener
) {

    fun unregister() = nsdManagerCompat.unregisterService(listener)

}