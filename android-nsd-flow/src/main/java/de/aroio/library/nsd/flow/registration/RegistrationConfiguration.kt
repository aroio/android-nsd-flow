package de.aroio.library.nsd.flow.registration

import android.net.nsd.NsdManager
import de.aroio.library.nsd.flow.ProtocolType

data class RegistrationConfiguration(
        val serviceName: String = "default",
        val serviceType: String = "_http._tcp.",
        val port: Int,

        @ProtocolType
        val protocolType: Int = NsdManager.PROTOCOL_DNS_SD
)