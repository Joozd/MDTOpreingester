package nl.joozd.mdtopreingester.mdto

import nl.joozd.mdtopreingester.tools.isValidXsdDateTimeLiteral

/**
 * MDTO 1.0.1
 * @property checksumAlgoritme Naam van het checksum algoritme dat is gebruikt om de checksum te maken.
 *  min 1 max 1
 * @property checksumWaarde De waarde van de checksum.
 *  min 1 max 1
 * @property checksumDatum Datum waarop de checksum is gemaakt.
 *  min 1 max 1
 *
 * @throws java.lang.IllegalArgumentException als checksumDatum niet voldoet aan xsd:dateTime
 */
data class ChecksumGegevens(
    val checksumAlgoritme: BegripGegevens,
    val checksumWaarde: String,
    val checksumDatum: String
){
    init{
        require(checksumDatum.isValidXsdDateTimeLiteral()) { "checksumDatum is ongeldig: $checksumDatum - moet voldoen aan xsd:dateTime" }
    }
}