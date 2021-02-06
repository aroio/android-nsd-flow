package de.aroio.library.nsd.flow

import android.net.nsd.NsdServiceInfo

data class DiscoveryStopFailed(val serviceType: String, val errorCode: Int)
    : Throwable("Discovery stop failed for $serviceType with error $errorCode")

data class DiscoveryStartFailed(val serviceType: String, val errorCode: Int)
    : Throwable("Discovery start failed for $serviceType with error $errorCode")

data class RegistrationFailed(val nsdServiceInfo: NsdServiceInfo, val errorCode: Int)
    : Throwable("Registration failed for $nsdServiceInfo with error $errorCode")

data class UnregistrationFailed(val nsdServiceInfo: NsdServiceInfo, val errorCode: Int)
    : Throwable("Unregistration failed for $nsdServiceInfo with error $errorCode")

data class ResolveFailed(val nsdServiceInfo: NsdServiceInfo, val errorCode: Int)
    : Throwable("Resolve failed for $nsdServiceInfo with error $errorCode")