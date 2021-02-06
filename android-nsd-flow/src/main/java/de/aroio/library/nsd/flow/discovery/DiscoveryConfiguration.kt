package de.aroio.library.nsd.flow.discovery

import android.net.nsd.NsdManager
import de.aroio.library.nsd.flow.ProtocolType

data class DiscoveryConfiguration(
        val type: String,
        @ProtocolType
        val protocolType: Int = NsdManager.PROTOCOL_DNS_SD
)