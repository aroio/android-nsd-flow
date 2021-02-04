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
class NsdManagerFlowTest {

    private lateinit var nsdManagerCompat: NsdManagerCompat
    private lateinit var nsdManagerFlow: NsdManagerFlow
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
        nsdManagerFlow = NsdManagerFlow(nsdManagerCompat)
    }

    @After
    fun teardown() {
        serverSocket.close()
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun secondConstructor() {
        NsdManagerFlow(InstrumentationRegistry.getInstrumentation().targetContext)
    }

    @Test
    fun discoverServices() = testCoroutineDispatcher.runBlockingTest {
        nsdManagerFlow.discoverServices(nsdConfiguration).first()
    }

    @Test
    fun registerService() = testCoroutineDispatcher.runBlockingTest {
        nsdManagerFlow.registerService(registrationConfiguration).first()
    }

    @Test
    fun resolveService() = testCoroutineDispatcher.runBlockingTest {
        nsdManagerFlow.resolveService(NsdServiceInfo()).first()
    }

}