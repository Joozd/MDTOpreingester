package nl.joozd.mdtopreingester.tools

import java.time.LocalDate
import java.time.ZoneOffset
import java.time.Month
import java.time.Year
import kotlin.math.abs

/**
 * Validates an XML Schema xsd:dateTime literal per §3.2.7 (XML Schema 1.0).
 *
 * Rules enforced:
 *  - Lexical structure: [-]yyyy-MM-dd'T'hh:mm:ss(.fraction)?(Z|±hh:mm)?
 *  - Year: no leading '+', not "0000"; if more than 4 digits, no leading zero.
 *  - Calendar: month/day valid in proleptic Gregorian (leap years allowed via [java.time]).
 *  - Time:
 *      * 00:00:00 .. 23:59:59(.fraction) valid
 *      * 24:00:00(.0*) allowed (represents the first instant of the next day)
 *      * Seconds may be 60 (leap second), but only with absent or all-zero fractional part.
 *  - Timezone: 'Z' or ±hh:mm, where 0 <= hh <= 14, 0 <= mm <= 59; if hh==14 then mm==0.
 *    '+00:00' and '-00:00' are allowed and equivalent to 'Z'.
 */
fun String.isValidXsdDateTimeLiteral(): Boolean {
    // yyyy (>=4 digits), mm, dd, T, hh:mm:ss, optional .fraction, optional TZ
    val re = Regex(
        pattern = """^(-?\d{4,})-(\d{2})-(\d{2})T(\d{2}):(\d{2}):(\d{2})(\.(\d+))?(Z|([+\-])(0\d|1[0-4]):([0-5]\d))?$"""
    )
    val m = re.matchEntire(this) ?: return false

    // ---- Year checks (no '+', disallow 0000, no leading zero if >4 digits) ----
    val yearStr = m.groupValues[1] // includes optional leading '-'
    val yearDigits = if (yearStr.startsWith('-')) yearStr.substring(1) else yearStr
    if (yearDigits == "0000") return false
    if (yearDigits.length > 4 && yearDigits.first() == '0') return false
    val year = try {
        yearStr.toInt()
    } catch (_: NumberFormatException) {
        // Out of Int range? Very large years: we still need day-of-month validity.
        // Fall back to manual month/day validation below (without java.time Year.isLeap for overflow).
        // But Kotlin Int max is ample for realistic XSD usage; reject extreme values to keep impl tight.
        return false
    }

    // ---- Date (month/day) validity ----
    val month = m.groupValues[2].toInt()
    val day = m.groupValues[3].toInt()
    if (month !in 1..12) return false
    val maxDay =
        try { Month.of(month).length(Year.isLeap(year.toLong())) }
        catch (_: Exception) { return false }
    if (day !in 1..maxDay) return false

    // ---- Time fields ----
    val hour = m.groupValues[4].toInt()
    val minute = m.groupValues[5].toInt()
    val second = m.groupValues[6].toInt()
    val frac = m.groupValues[8] // only digits if present

    // Minute always 0..59
    if (minute !in 0..59) return false

    // Second: allow 0..59; allow 60 (leap-second) only if fraction absent or all zeros
    val fractionAllZeros = frac.isNotEmpty() && frac.all { it == '0' }
    if (second !in 0..59) {
        if (second != 60) return false
        if (frac.isNotEmpty() && !fractionAllZeros) return false
    }

    // Hour rules:
    // - Normal: 00..23
    // - Special: 24:00:00(.0*) only (i.e., minute==0, second==0, fraction absent or zeros)
    val isAllZeroTime = (minute == 0 && second == 0 && (frac.isEmpty() || fractionAllZeros))
    if (hour == 24) {
        if (!isAllZeroTime) return false
    } else if (hour !in 0..23) {
        return false
    }

    // ---- Timezone ----
    val tzWhole = m.groupValues[9] // 'Z' or empty or full ±hh:mm
    if (tzWhole.isNotEmpty() && tzWhole != "Z") {
        val sign = if (m.groupValues[10] == "-") -1 else 1
        val tzHour = m.groupValues[11].toInt()
        val tzMin = m.groupValues[12].toInt()
        // hh bounds
        if (tzHour !in 0..14) return false
        // if 14 then minutes must be 00
        if (tzHour == 14 && tzMin != 0) return false
        // minutes 0..59 always
        if (tzMin !in 0..59) return false
        // (+00:00) / (-00:00) are allowed; no extra check needed
        // sign captured but magnitude rules already enforced
        if (abs(tzHour) == 0 && tzMin == 0) {
            // ok
            @Suppress("UNUSED_VARIABLE") val sgn = sign // just to emphasize sign can be + or -
        }
    }

    // All structural/domain checks passed.
    return true
}


/**
 * Validates an XML Schema xsd:date literal per §3.2.9.
 *
 * Checks:
 *  1) Lexical form: [-]yyyy-MM-dd with optional timezone (Z or ±hh:mm; hh up to 14, and 14:00 only).
 *  2) Calendar validity (leap years, day ranges).
 *  3) If a timezone is present, its *recoverable timezone* (UTC-midnight(same date) minus local midnight)
 *     must be between +12:00 and -11:59 inclusive (i.e. minutes ∈ [-719, 720]).
 */
fun String.isValidXsdDateLiteral(): Boolean {
    println("checking $this")
    val re = Regex("""^(-?\d{4,})-(\d{2})-(\d{2})(Z|([+\-])(0\d|1[0-4]):([0-5]\d))?$""")
    val m = re.matchEntire(this) ?: return false.also { println("No regex match")}

    val year = m.groupValues[1].toInt()
    val month = m.groupValues[2].toInt()
    val day = m.groupValues[3].toInt()
    if (month !in 1..12) return false.also { println("month not OK")}
    val maxDay = Month.of(month).length(Year.isLeap(year.toLong()))
    if (day !in 1..maxDay) return false.also { println("day not OK")}

    val tzToken = m.groupValues[4]
    if (tzToken.isEmpty()) return true // nontimezoned values: lexical + calendar checks suffice
    if(m.groupValues[4] == "Z") return true // Z timezone is OK

    // Lexical TZ bounds (+/-14:00) with 14:00 only at :00
    val sign = if (m.groupValues[5] == "-") -1 else 1
    val hour = m.groupValues[6].toInt()
    val minute = m.groupValues[7].toInt()
    if (hour == 14 && minute != 0) return false.also { println("timezone not OK - $hour:$minute")}
    val shownOffsetMinutes = sign * (hour * 60 + minute)

    return try {
        val localMidnight = LocalDate.of(year, month, day).atStartOfDay()
        val shownOffset = ZoneOffset.ofTotalSeconds(shownOffsetMinutes * 60)
        val instantAtLocalMidnight = localMidnight.toInstant(shownOffset)

// Kandidaten: UTC-middernacht van de UTC-datum van die instant, en van de volgende dag
        val utcDate = instantAtLocalMidnight.atOffset(ZoneOffset.UTC).toLocalDate()
        val diff1 = ((utcDate.atStartOfDay(ZoneOffset.UTC).toInstant().epochSecond - instantAtLocalMidnight.epochSecond) / 60).toInt()
        val diff2 = (((utcDate.plusDays(1)).atStartOfDay(ZoneOffset.UTC).toInstant().epochSecond - instantAtLocalMidnight.epochSecond) / 60).toInt()

// Accepteer als één van beide binnen [+12:00 .. -11:59] valt (minuten [-719..720])
        (diff1 in -719..720) || (diff2 in -719..720)
    } catch (_: Exception) {
        false.also { println("This is a long one")}
    }
}

/**
 * Validates an XML Schema xsd:gYearMonth literal
 *
 * Checks:
 * CCYY-MM format
 * MM in 01-12
 * Parsed date in earliest to latest (if given)
 */
fun String.isValidgYearMonthLiteral(earliest: LocalDate? = null, latest: LocalDate? = null): Boolean{
    val re = Regex("""\d{4}-\d{2}""")
    val m = re.matchEntire(this) ?: return false

    val month = takeLast(2).toInt()
    if(month !in 1..12) return false

    val year = take(4).toInt()
    if(earliest == null && latest == null) return true

    val date = LocalDate.of(year, month, 1)

    earliest?.let{
        if (date < it) return false
    }
    latest?.let{
        if (date > it) return false
    }

    return true
}

/**
 * Validates an XML Schema xsd:gYear literal
 *
 * Checks:
 * CCYY format
 * year in earliest to latest (if given)
 */
fun String.isValidgYearLiteral(earliest: LocalDate? = null, latest: LocalDate? = null): Boolean{
    if(length != 4) return false
    if(any{ !it.isDigit()}) return false
    if(earliest == null && latest == null) return true
    val year = this.toInt()
    earliest?.let{ if (year < it.year) return false }
    latest?.let{ if (year > it.year) return false }

    return true
}
