package com.toxicbakery.library.nsd.rx.registration

import android.net.nsd.NsdManager
import com.toxicbakery.library.nsd.rx.NsdManagerCompat
import com.toxicbakery.library.nsd.rx.mock
import com.toxicbakery.library.nsd.rx.verify
import org.junit.Test

class RegistrationBinderTest {

    @Test
    fun unregister() {
        val nsdManagerCompat: NsdManagerCompat = mock()
        val listener: NsdManager.RegistrationListener = mock()
        RegistrationBinder(nsdManagerCompat, listener).unregister()
        verify(nsdManagerCompat).unregisterService(listener)
    }

}