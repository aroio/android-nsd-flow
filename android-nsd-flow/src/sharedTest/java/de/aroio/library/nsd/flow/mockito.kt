package de.aroio.library.nsd.flow

import org.mockito.Mockito

inline fun <reified T> mock() = Mockito.mock(T::class.java)

fun <T> verify(mock: T) = Mockito.verify(mock)