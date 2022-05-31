package com.hh.data

import com.hh.data.repo.Util
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UtilTest {

    @Test
    fun test_getIntegerPart() {
        assertEquals("123", Util.getIntegerPart("123.456"))
        assertEquals("", Util.getIntegerPart(".123456"))
        assertEquals("123", Util.getIntegerPart("123"))
    }

}