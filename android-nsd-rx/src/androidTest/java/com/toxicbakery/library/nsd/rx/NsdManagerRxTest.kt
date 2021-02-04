package com.toxicbakery.library.nsd.rx

import android.net.nsd.NsdServiceInfo
import androidx.test.platform.app.InstrumentationRegistry
import com.toxicbakery.library.nsd.rx.discovery.DiscoveryConfiguration
import com.toxicbakery.library.nsd.rx.registration.RegistrationConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.ServerSocket

@ExperimentalCoroutinesApi
class NsdManagerRxTest {

    private lateinit var nsdManagerCompat: NsdManagerCompat
    private lateinit var nsdManagerRx: NsdManagerRx
    private lateinit var registrationConfiguration: RegistrationConfiguration
    private lateinit var serverSocket: ServerSocket
    private val nsdConfiguration = DiscoveryConfiguration("_http._tcp.")
    private val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        serverSocket = ServerSocket(0)
        registrationConfiguration = RegistrationConfiguration(port = serverSocket.localPort)
        nsdManagerCompat = mock()
        nsdManagerRx = NsdManagerRx(nsdManagerCompat)
    }

    @After
    fun teardown() {
        serverSocket.close()
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun secondConstructor() {
        NsdManagerRx(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun discoverServices() = testCoroutineDispatcher.runBlockingTest {
        nsdManagerRx.discoverServices(nsdConfiguration).first()
    }

    @Test
    fun registerService() = testCoroutineDispatcher.runBlockingTest {
        nsdManagerRx.registerService(registrationConfiguration).first()
    }

    @Test
    fun resolveService() = testCoroutineDispatcher.runBlockingTest {
        nsdManagerRx.resolveService(NsdServiceInfo()).first()
    }

}