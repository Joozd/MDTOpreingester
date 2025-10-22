package nl.joozd.mdtopreingester.mdto

import nl.joozd.mdtopreingester.tools.isValidAnyUriLiteral

/**
 * MDTO 1.0.1
 * @property raadpleeglocatieFysiek Actuele fysieke locatie waar het informatieobject ter inzage ligt.
 *  min 0 max unbounded
 * @property raadpleegLocatieOnline
 *  min 0, max unbounded
 * @throws java.lang.IllegalArgumentException als [raadpleegLocatieOnline] niet voldoet aan xsd:anyURI
 */
data class RaadpleegLocatieGegevens(
    val raadpleeglocatieFysiek: List<VerwijzingGegevens>,
    val raadpleegLocatieOnline: List<String>
){
    init{
        raadpleegLocatieOnline.forEach{
            require(it.isValidAnyUriLiteral()) { "raadpleegLocatieOnline is ongeldig: $it - moet voldoen aan xsd:anyURI"}
        }
    }
}