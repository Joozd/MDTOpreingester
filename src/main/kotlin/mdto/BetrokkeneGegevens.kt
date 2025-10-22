package nl.joozd.mdtopreingester.mdto


/**
 * MDTO 1.0.1
 * @property betrokkeneTypeRelatie Typering van de betrokkenheid van de actor bij het informatieobject.
 *  min 1 max 1
 * @property betrokkeneActor De persoon of organisatie die betrokken is bij het informatieobject.
 *  min 1 max 1
 */
data class BetrokkeneGegevens(
    val betrokkeneTypeRelatie: BegripGegevens,
    val betrokkeneActor: VerwijzingGegevens
)
