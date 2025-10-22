package nl.joozd.mdtopreingester.mdto

/**
 * MDTO 1.0.1
 * @property begripLabel De tekstweergave van het begrip dat is toegekend in de begrippenlijst.
 *  min 1 max 1
 * @property begripCode De code die aan het begrip is toegekend in de begrippenlijst.
 *  min 0 max 1
 * @property begripBegrippenLijst Een beschrijving van de begrippen die voor een bepaald toepassingsgebied gebruikt worden is opgesomd.
 *  Samen met hun betekenis en hun onderlinge relaties.
 *  min 1 max 1
 */
data class BegripGegevens(
    val begripLabel: String,
    val begripCode: String?,
    val begripBegrippenLijst: VerwijzingGegevens
)
