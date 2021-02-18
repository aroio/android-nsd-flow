package de.aroio.library.nsd.flow.registration

import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import de.aroio.library.nsd.flow.ProtocolType

fun RegistrationConfiguration(
        serviceName: String = "default",
        serviceType: String = "_http._tcp.",
        port: Int,
        @ProtocolType
        protocolType: Int = NsdManager.PROTOCOL_DNS_SD
) = RegistrationConfiguration(
        nsdServiceInfo = NsdServiceInfo().apply {
            setServiceName(serviceName)
            setServiceType(serviceType)
            setPort(port)
        },
        protocolType = protocolType
)

data class RegistrationConfiguration(
        val nsdServiceInfo: NsdServiceInfo,
        @ProtocolType
        val protocolType: Int = NsdManager.PROTOCOL_DNS_SD
)
