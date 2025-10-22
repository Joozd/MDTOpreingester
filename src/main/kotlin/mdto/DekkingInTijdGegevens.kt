package nl.joozd.mdtopreingester.mdto

import nl.joozd.mdtopreingester.tools.isValidXsdDateLiteral
import nl.joozd.mdtopreingester.tools.isValidgYearLiteral
import nl.joozd.mdtopreingester.tools.isValidgYearMonthLiteral


/**
 * MDTO 1.0.1
 * @property dekkingInTijdType Nadere typering van het tijdstip of de periode waar de inhoud van het informatieobject betrekking op heeft.
 *  min 1 max 1
 * @property dekkingInTijdBeginDatum
 *  min 1 max 1
 * @property dekkingInTijdEindDatum
 *  min 0 max 1
 * @throws IllegalArgumentException if any date does not conform to xsd:gYear | xsd:gYearMonth | xsd:date
 */
data class DekkingInTijdGegevens(
    val dekkingInTijdType: BegripGegevens,
    val dekkingInTijdBeginDatum: String,
    val dekkingInTijdEindDatum: String?,
){
    init{
        // assert dekkingInTijd are allowed dates
        require(isgYeargYearMonthorDateString(dekkingInTijdBeginDatum)) { "dekkingInTijdBeginDatum is ongeldig: $dekkingInTijdBeginDatum - does not conform to xsd:gYear | xsd:gYearMonth | xsd:date" }
        require(isgYeargYearMonthorDateString(dekkingInTijdEindDatum)) { "dekkingInTijdEindDatum is ongeldig: $dekkingInTijdEindDatum - does not conform to xsd:gYear | xsd:gYearMonth | xsd:date" }
    }

    /**
     * checks if [dateString] conforms to xsd:gYear | xsd:gYearMonth | xsd:date
     */
    private fun isgYeargYearMonthorDateString(dateString: String?): Boolean{
        if(dateString == null) return true
        return dateString.isValidgYearLiteral() || dateString.isValidgYearMonthLiteral() || dateString.isValidXsdDateLiteral()
    }
}

private fun isgYearMonthString(s: String){

}