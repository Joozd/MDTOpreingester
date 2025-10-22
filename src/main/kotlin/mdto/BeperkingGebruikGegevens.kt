package nl.joozd.mdtopreingester.mdto

data class BeperkingGebruikGegevens(
    val beperkingGebruikType: BegripGegevens,
    val beperkingGebruikNadereBeschrijving: String,
    val beperkingGebruikDocumentatie: VerwijzingGegevens,
    val beperkingGebruikTermijn: TermijnGegevens
)