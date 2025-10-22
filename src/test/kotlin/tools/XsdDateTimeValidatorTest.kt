package tools

import nl.joozd.mdtopreingester.tools.isValidXsdDateTimeLiteral
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for isValidXsdDateTimeLiteral() per XML Schema §3.2.7.
 */
class XsdDateTimeValidatorTest {
    private fun isValidXsdDateTimeLiteral(s: String) = s.isValidXsdDateTimeLiteral()

    /** Accepts a basic UTC timestamp with fractional seconds. */
    @Test
    fun `valid basic with Z and fraction`() {
        assertTrue(isValidXsdDateTimeLiteral("2025-10-22T07:15:30.123Z"))
    }

    /** Accepts without timezone (local/untimezoned). */
    @Test
    fun `valid without timezone`() {
        assertTrue(isValidXsdDateTimeLiteral("1999-12-31T23:59:59"))
    }

    /** Rejects year 0000 and a leading '+' sign; accepts negative years like -0001 (1 BCE). */
    @Test
    fun `year rules`() {
        assertFalse(isValidXsdDateTimeLiteral("0000-01-01T00:00:00Z"))
        assertFalse(isValidXsdDateTimeLiteral("+2020-01-01T00:00:00Z"))
        assertTrue(isValidXsdDateTimeLiteral("-0001-01-01T00:00:00Z"))
    }

    /** If year has >4 digits it must not start with 0. */
    @Test
    fun `long year no leading zero`() {
        assertTrue(isValidXsdDateTimeLiteral("12345-01-01T00:00:00Z"))
        assertFalse(isValidXsdDateTimeLiteral("01234-01-01T00:00:00Z"))
    }

    /** Validates calendar day ranges, including leap years. */
    @Test
    fun `calendar validity and leap years`() {
        assertTrue(isValidXsdDateTimeLiteral("2024-02-29T00:00:00Z"))
        assertFalse(isValidXsdDateTimeLiteral("2025-02-29T00:00:00Z"))
        assertFalse(isValidXsdDateTimeLiteral("2025-13-01T00:00:00Z"))
        assertFalse(isValidXsdDateTimeLiteral("2025-04-31T00:00:00Z"))
    }

    /** 24:00:00(.0*) is allowed and represents first instant of next day. Any other 24:xx is invalid. */
    @Test
    fun `24 00 00 special case`() {
        assertTrue(isValidXsdDateTimeLiteral("2000-01-01T24:00:00Z"))
        assertTrue(isValidXsdDateTimeLiteral("2000-01-01T24:00:00.000"))
        assertFalse(isValidXsdDateTimeLiteral("2000-01-01T24:00:01Z"))
        assertFalse(isValidXsdDateTimeLiteral("2000-01-01T24:00:00.001"))
    }

    /** Leap second 23:59:60 allowed only with absent or all-zero fraction. */
    @Test
    fun `leap second handling`() {
        assertTrue(isValidXsdDateTimeLiteral("1998-12-31T23:59:60Z"))
        assertTrue(isValidXsdDateTimeLiteral("1998-12-31T23:59:60.0Z"))
        assertTrue(isValidXsdDateTimeLiteral("1998-12-31T23:59:60.000"))
        assertFalse(isValidXsdDateTimeLiteral("1998-12-31T23:59:60.1Z"))
    }

    /** Timezone lexical rules: Z, ±hh:mm with hh<=14; if hh==14 then mm==00. */
    @Test
    fun `timezone bounds and forms`() {
        assertTrue(isValidXsdDateTimeLiteral("2010-01-01T00:00:00Z"))
        assertTrue(isValidXsdDateTimeLiteral("2010-01-01T00:00:00+00:00"))
        assertTrue(isValidXsdDateTimeLiteral("2010-01-01T00:00:00-00:00"))
        assertTrue(isValidXsdDateTimeLiteral("2010-01-01T00:00:00+14:00"))
        assertTrue(isValidXsdDateTimeLiteral("2010-01-01T00:00:00-13:59"))
        assertFalse(isValidXsdDateTimeLiteral("2010-01-01T00:00:00+14:01"))
        assertFalse(isValidXsdDateTimeLiteral("2010-01-01T00:00:00+15:00"))
        assertFalse(isValidXsdDateTimeLiteral("2010-01-01T00:00:00+05:60"))
    }

    /** Rejects malformed tokens in general. */
    @Test
    fun `malformed strings`() {
        assertFalse(isValidXsdDateTimeLiteral("2025-1-01T00:00:00Z"))
        assertFalse(isValidXsdDateTimeLiteral("2025-01-01 00:00:00Z"))
        assertFalse(isValidXsdDateTimeLiteral("2025-01-01T24:00:00+05:30x"))
        assertFalse(isValidXsdDateTimeLiteral("not-a-datetime"))
    }

    /** Fractional seconds may be present with any length (canonical trimming is not enforced here). */
    @Test
    fun `fractional seconds general`() {
        assertTrue(isValidXsdDateTimeLiteral("2025-10-22T07:15:30.0"))
        assertTrue(isValidXsdDateTimeLiteral("2025-10-22T07:15:30.123456789+01:00"))
        assertTrue(isValidXsdDateTimeLiteral("2025-10-22T07:15:30.000Z"))
    }
}
