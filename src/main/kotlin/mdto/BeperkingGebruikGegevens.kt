package nl.joozd.mdtopreingester.mdto

/**
 * MDTO 1.0
 * @property beperkingGebruikType Typering van de beperking. Op grond waarvan bepaald kan worden wie toegang heeft tot het informatieobject en welke voorwaarden op het gebruik van toepassing zijn.
 *  min 1 max 1
 * @property beperkingGebruikNadereBeschrijving Nadere beschrijving van de beperking op het gebruik. Als aanvulling op beperkingGebruikType.
 *  min 0 max 1
 * @property beperkingGebruikDocumentatie Verwijzing naar een tekstdocument waarin een nadere beschrijving van de beperking is opgenomen.
 *  min 0 max unbounded
 * @property beperkingGebruikTermijn De termijn waarbinnen de beperking op het gebruik van toepassing is.
 *  min 0 max 1
 */
data class BeperkingGebruikGegevens(
    val beperkingGebruikType: BegripGegevens,
    val beperkingGebruikNadereBeschrijving: String?,
    val beperkingGebruikDocumentatie: List<VerwijzingGegevens>,
    val beperkingGebruikTermijn: TermijnGegevens?
)