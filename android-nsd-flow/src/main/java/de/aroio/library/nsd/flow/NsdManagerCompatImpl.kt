package de.aroio.library.nsd.flow

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo

internal class NsdManagerCompatImpl(private val nsdManager: NsdManager) : NsdManagerCompat {

    override fun registerService(
            serviceInfo: NsdServiceInfo,
            protocolType: Int,
            listener: NsdManager.RegistrationListener
    ) = nsdManager.registerService(serviceInfo, protocolType, listener)

    override fun unregisterService(
            listener: NsdManager.RegistrationListener
    ) = nsdManager.unregisterService(listener)


    override fun discoverServices(
            serviceType: String,
            protocolType: Int,
            listener: NsdManager.DiscoveryListener
    ) = nsdManager.discoverServices(serviceType, protocolType, listener)

    override fun stopServiceDiscovery(
            listener: NsdManager.DiscoveryListener
    ) = nsdManager.stopServiceDiscovery(listener)

    override fun resolveService(
            serviceInfo: NsdServiceInfo,
            listener: NsdManager.ResolveListener
    ) = nsdManager.resolveService(serviceInfo, listener)

    companion object {
        fun fromContext(
                context: Context
        ) = NsdManagerCompatImpl(context.getSystemService(Context.NSD_SERVICE) as NsdManager)
    }

}