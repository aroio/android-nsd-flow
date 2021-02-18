# Android Network Service Discovery Flow

[![](https://jitpack.io/v/aroio/android-nsd-flow.svg)](https://jitpack.io/#aroio/android-nsd-flow)

A Flow wrapper around the Android [NsdManager](https://developer.android.com/reference/android/net/nsd/NsdManager.html) api.
If you are using RxJava I would recommend to check out [Android Network Service Discovery Rx](https://github.com/ToxicBakery/Android-Nsd-Rx)

## Creating a Manager
The NsdManagerFlow can be created with a context instance.

```kotlin
NsdManagerFlow(context)
```

### Discovery
The service name used is defined in rfc6763 and finds all services without filtering.
[ietf.org/rfc/rfc6763.txt](http://www.ietf.org/rfc/rfc6763.txt)

```kotlin
NsdManagerFlow(context)
    .discoverServices(DiscoveryConfiguration("_services._dns-sd._udp"))
    .flowOn(Dispatchers.IO) // discovering must be done on a different CoroutineContext than main
    .collect { event: DiscoveryEvent -> Log.d("Discovery", "${event.service.serviceName}") }
```

### Register
```kotlin
NsdManagerFlow(context)
    .registerService(RegistrationConfiguration(port = 12345))
    .flowOn(Dispatchers.IO)
    .collect { event: RegistrationEvent -> Log.d("Registration", "Registered ${event.nsdServiceInfo.serviceName}") }
```

### Resolve
```kotlin
NsdManagerFlow(context)
    .resolveService(serviceInfo)
    .flowOn(Dispatchers.IO)
    .collect { event: ResolveEvent -> Log.d("Resolve", "Resolved ${event.nsdServiceInfo.serviceName}") }
```

## Demo App
The demo app provides a quick way to run the NSD service against your connected network.

![Demo Application Screenshot][demo-app-screenshot]

[demo-app-screenshot]: https://user-images.githubusercontent.com/1614281/42720862-9e983c8c-86fd-11e8-8d25-70ac04022a68.png