package tools

import nl.joozd.mdtopreingester.tools.isValidXsdDurationLiteral
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class XsdDurationValidatorTest {
    private fun isValidXsdDurationLiteral(s: String) = s.isValidXsdDurationLiteral()

    @Test
    fun `basic valid durations`() {
        assertTrue(isValidXsdDurationLiteral("P1Y2M3DT10H30M"))
        assertTrue(isValidXsdDurationLiteral("P1347Y"))
        assertTrue(isValidXsdDurationLiteral("P1347M"))
        assertTrue(isValidXsdDurationLiteral("P1Y2MT2H"))
        assertTrue(isValidXsdDurationLiteral("-P120D"))
        assertTrue(isValidXsdDurationLiteral("P0Y1347M0D"))
        assertTrue(isValidXsdDurationLiteral("P3DT12H"))
    }

    @Test
    fun `valid fractional seconds`() {
        assertTrue(isValidXsdDurationLiteral("PT0.5S"))
        assertTrue(isValidXsdDurationLiteral("PT1.2345S"))
        assertTrue(isValidXsdDurationLiteral("PT10S"))
    }

    @Test
    fun `rejects forbidden forms`() {
        assertFalse(isValidXsdDurationLiteral("P"))               // empty
        assertFalse(isValidXsdDurationLiteral("PT"))              // T but no time fields
        assertFalse(isValidXsdDurationLiteral("P1Y2MT"))          // trailing T
        assertFalse(isValidXsdDurationLiteral("P-1347M"))         // internal minus
        assertFalse(isValidXsdDurationLiteral("1Y2M"))            // missing P
        assertFalse(isValidXsdDurationLiteral("P1Y2MT10H-5M"))    // negative inside
        assertFalse(isValidXsdDurationLiteral("PT1."))            // trailing dot
        assertFalse(isValidXsdDurationLiteral("PT.S"))            // dot without digits
    }

    @Test
    fun `accepts edge minimal and maximal cases`() {
        assertTrue(isValidXsdDurationLiteral("PT1M"))
        assertTrue(isValidXsdDurationLiteral("-P0D"))
        assertTrue(isValidXsdDurationLiteral("P999999999Y999999999M999999999DT999999999H999999999M999999999.999S"))
    }
}
