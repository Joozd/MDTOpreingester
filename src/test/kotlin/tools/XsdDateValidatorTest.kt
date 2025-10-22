package tools

import nl.joozd.mdtopreingester.tools.isValidXsdDateLiteral
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * Unit tests for isValidXsdDateLiteral() per XML Schema §3.2.9.
 *
 * Covers:
 *  - Pure dates (no timezone)
 *  - Calendar validity (incl. leap years)
 *  - Lexical timezone bounds (±14:00 with :00 only at 14)
 *  - Recoverable timezone ∈ [+12:00, -11:59] after normalization
 *  - Known normative example equivalence
 */
class XsdDateValidatorTest {
    private fun isValidXsdDateLiteral(s: String) =
        s.isValidXsdDateLiteral()

    @Test
    fun `accepts basic date without timezone`() {
        assertTrue(isValidXsdDateLiteral("2025-10-22"))
    }

    @Test
    fun `rejects impossible month and day`() {
        assertFalse(isValidXsdDateLiteral("2025-13-01"))
        assertFalse(isValidXsdDateLiteral("2025-02-30"))
    }

    @Test
    fun `accepts leap day on leap year, rejects on non-leap`() {
        assertTrue(isValidXsdDateLiteral("2024-02-29"))
        assertFalse(isValidXsdDateLiteral("2025-02-29"))
    }

    @Test
    fun `accepts Z timezone`() {
        assertTrue(isValidXsdDateLiteral("2025-10-22Z"))
    }

    @Test
    fun `accepts lower and upper minute bounds for recoverable range`() {
        // Edge of recoverable range after normalization
        assertTrue(isValidXsdDateLiteral("2025-10-22+12:00"))
        assertTrue(isValidXsdDateLiteral("2025-10-22-11:59"))
    }

    @Test
    fun `rejects 14xx except 14 00 lexically, and validates recoverable`() {
        // 14:30 is lexically illegal
        assertFalse(isValidXsdDateLiteral("2025-10-22+14:30"))
        // 14:00 is lexically legal; recoverable must still end up within [+12:00, -11:59]
        // With the mini-fix above, this normalizes and should be valid.
        assertTrue(isValidXsdDateLiteral("2025-10-22+14:00"))
    }

    @Test
    fun `accepts normative example 2002-10-10+13 00 (≡ 2002-10-09-11 00)`() {
        // With the mini-fix, +13:00 normalizes to a recoverable -11:00 and should be valid.
        assertTrue(isValidXsdDateLiteral("2002-10-10+13:00"))
        assertTrue(isValidXsdDateLiteral("2002-10-09-11:00"))
    }

    @Test
    fun `rejects malformed strings`() {
        assertFalse(isValidXsdDateLiteral("2025-1-01"))
        assertFalse(isValidXsdDateLiteral("2025-01-01+5:00"))
        assertFalse(isValidXsdDateLiteral("2025-01-01+05:60"))
        assertFalse(isValidXsdDateLiteral("not-a-date"))
    }

    @Test
    fun `handles negative years lexically`() {
        // XML Schema permits leading '-' on years (no year zero in proleptic Gregorian),
        // our regex allows -yyyy...; calendar check delegates to java.time.
        assertTrue(isValidXsdDateLiteral("-0001-01-01")) // 1 BCE
    }
}
