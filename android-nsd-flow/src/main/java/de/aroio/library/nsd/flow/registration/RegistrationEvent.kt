package de.aroio.library.nsd.flow.registration

import android.net.nsd.NsdServiceInfo

sealed class RegistrationEvent {
    data class ServiceRegistered(val nsdServiceInfo: NsdServiceInfo) : RegistrationEvent()
    data class ServiceUnregistered(val nsdServiceInfo: NsdServiceInfo) : RegistrationEvent()
}