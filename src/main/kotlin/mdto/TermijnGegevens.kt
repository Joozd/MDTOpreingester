package nl.joozd.mdtopreingester.mdto

import nl.joozd.mdtopreingester.tools.isValidXsdDateLiteral
import nl.joozd.mdtopreingester.tools.isValidXsdDurationLiteral
import nl.joozd.mdtopreingester.tools.isValidgYearLiteral
import nl.joozd.mdtopreingester.tools.isValidgYearMonthLiteral


/**
 * MDTO 1.0.1
 * @property termijnTriggerStartLooptijd Gebeurtenis waarna de looptijd van de termijn start.
 *  min 0 max 1
 * @property termijnStartdatumLooptijd De datum waarop de trigger heeft plaatsgevonden en de looptijd is gestart.
 *  min 0 max 1
 * @property termijnLooptijd De hoeveelheid tijd waarin de termijnEindDatum bereikt wordt, nadat de trigger heeft plaatsgevonden.
 *  min 0 max 1
 * @property termijnEinddatum Datum waarop de termijn eindigt.
 *  min 0 max 1
 *
 * @throws IllegalArgumentException if a date or duration does not conform to expected format
 */
data class TermijnGegevens(
    val termijnTriggerStartLooptijd: BegripGegevens?,
    val termijnStartdatumLooptijd: String?,
    val termijnLooptijd: String?,
    val termijnEinddatum: String?
){
    init{
        require(termijnStartdatumLooptijd == null || termijnStartdatumLooptijd.isValidXsdDateLiteral()) { "termijnStartdatumLooptijd is ongeldig: $termijnStartdatumLooptijd - moet voldoen aan xsd:date" }
        require(termijnLooptijd == null || termijnLooptijd.isValidXsdDurationLiteral()) { "termijnLooptijd is ongeldig: $termijnLooptijd - moet voldoen aan xsd:duration" }
        require(isgYeargYearMonthorDateString(termijnEinddatum)) { "termijnEinddatum is ongeldig: $termijnEinddatum - does not conform to xsd:gYear | xsd:gYearMonth | xsd:date" }
    }

    /**
     * checks if [dateString] conforms to xsd:gYear | xsd:gYearMonth | xsd:date
     */
    private fun isgYeargYearMonthorDateString(dateString: String?): Boolean{
        if(dateString == null) return true
        return dateString.isValidgYearLiteral() || dateString.isValidgYearMonthLiteral() || dateString.isValidXsdDateLiteral()
    }
}
