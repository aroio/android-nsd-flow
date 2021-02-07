package de.aroio.library.nsd.flow

import android.net.nsd.NsdServiceInfo
import de.aroio.library.nsd.flow.discovery.DiscoveryConfiguration
import de.aroio.library.nsd.flow.registration.RegistrationConfiguration
import io.mockk.clearMocks
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class NsdManagerFlowTest {

    private lateinit var nsdManagerFlow: NsdManagerFlow
    private val nsdManagerCompat: NsdManagerCompat = mockk()
    private val nsdConfiguration = DiscoveryConfiguration("_http._tcp.")
    private val registrationConfiguration = RegistrationConfiguration(port = 1)
    private val testCoroutineDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()

    @BeforeEach
    fun setup() {
        clearMocks(nsdManagerCompat)
        nsdManagerFlow = NsdManagerFlow(nsdManagerCompat)
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @AfterEach
    fun teardown() {
        Dispatchers.resetMain()
        testCoroutineDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun discoverServices() = testCoroutineDispatcher.runBlockingTest {
        justRun { nsdManagerCompat.discoverServices(any(), any(), any()) }

        nsdManagerFlow.discoverServices(nsdConfiguration).collect()

        verify(exactly = 1) { nsdManagerCompat.discoverServices(any(), any(), any()) }
    }

    @Test
    fun registerService() = testCoroutineDispatcher.runBlockingTest {
        justRun { nsdManagerCompat.registerService(any(), any(), any()) }

        nsdManagerFlow.registerService(registrationConfiguration).collect()

        verify(exactly = 1) { nsdManagerCompat.registerService(any(), any(), any()) }
    }

    @Test
    fun resolveService() = testCoroutineDispatcher.runBlockingTest {
        justRun { nsdManagerCompat.resolveService(any(), any()) }

        nsdManagerFlow.resolveService(NsdServiceInfo()).collect()

        verify(exactly = 1) { nsdManagerCompat.resolveService(any(), any()) }
    }

}