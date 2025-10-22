package nl.joozd.mdtopreingester.mdto

/**
 * MDTO 1.0.1
 * @property verwijzingNaam De naam van het object waarnaar verwezen wordt.
 *  min 1 max 1
 * @property verwijzingIdentificatie De identificatie van het object waarnaar verwezen wordt.
 *  min 0 max 1
 */
data class VerwijzingGegevens(
    val verwijzingNaam: String,
    val verwijzingIdentificatie: IdentificatieGegevens?
)
