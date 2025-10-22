package nl.joozd.mdtopreingester.mdto

import nl.joozd.mdtopreingester.tools.isValidXsdDateLiteral
import nl.joozd.mdtopreingester.tools.isValidXsdDateTimeLiteral
import nl.joozd.mdtopreingester.tools.isValidgYearLiteral
import nl.joozd.mdtopreingester.tools.isValidgYearMonthLiteral

/**
 * MDTO 1.0.1
 * @property BegripGegevens Aanduiding van het type event.
 *  min 1 max 1
 * @property eventTijd Het tijdstip waarop het event heeft plaatsgevonden.
 *  min 0 max 1
 * @property eventVerantwoordelijkeActor De actor die verantwoordelijk was voor de gebeurtenis.
 *  min 0 max 1
 * @property eventResultaat Beschrijving van het resultaat van het event voor zover relevant voor de duurzame toegankelijkheid van het informatieobject.
 *  min 0 max 1
 *
 * @throws IllegalArgumentException if [eventTijd] does not conform to xsd:gYear | xsd:gYearMonth | xsd:date | xsd:dateTime
 */
data class EventGegevens(
    val eventType: BegripGegevens,
    val eventTijd: String?,
    val eventVerantwoordelijkeActor: VerwijzingGegevens?,
    val eventResultaat: String?
){
    init{
        require (eventTijd == null ||
                isgYeargYearMonthDateOrDateTimeString(eventTijd)
        ) { "eventTijd is ongeldig: $eventTijd - does not conform to xsd:gYear | xsd:gYearMonth | xsd:date | xsd:dateTime" }
    }

    /**
     * checks if [s] conforms to xsd:gYear | xsd:gYearMonth | xsd:date
     */
    private fun isgYeargYearMonthDateOrDateTimeString(s: String?): Boolean{
        if(s == null) return true
        return s.isValidgYearLiteral() ||
                s.isValidgYearMonthLiteral() ||
                s.isValidXsdDateLiteral() ||
                s.isValidXsdDateTimeLiteral()
    }
}
