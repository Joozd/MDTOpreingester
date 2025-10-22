package nl.joozd.mdtopreingester.mdto

/**
 * MDTO 1.0.1
 * @property gerelateerdInformatieobjectVerwijzing Verwijzing naar het gerelateerde informatieobject.
 *  min 1 max 1
 * @property gerelateerdInformatieobjectTypeRelatie Typering van de relatie met het andere informatieobject.
 *  min 1 max 1
 */
data class GerelateerdInformatieobjectGegevens(
    val gerelateerdInformatieobjectVerwijzing: VerwijzingGegevens,
    val gerelateerdInformatieobjectTypeRelatie: BegripGegevens
)
