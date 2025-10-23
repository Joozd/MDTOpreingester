package nl.joozd.mdtopreingester.mdto

import nl.joozd.mdtopreingester.tools.isValidLanguageLiteral

/**
 * MDTO 1.0.1
 * @property identificatie Gegevens waarmee het object geïdentificeerd kan worden.
 *  min 1 max unbounded. // checked in super
 * @property naam Een betekenisvolle aanduiding waaronder het object bekend is.
 *  min 1 max 1
 * @property aggregatieniveau Het aggregatieniveau van het informatieobject.
 *  min 0 max 1
 * @property classificatie Ordening van informatieobjecten in een logisch verband, zoals vastgelegd in een classificatieschema.
 *  min 0 max unbounded
 * @property trefwoord Trefwoord dat aan het informatieobject is toegekend.
 *  min 0 max unbounded
 * @property omschrijving Omschrijving van de inhoud van het informatieobject.
 *  min 0 max 1
 * @property raadpleeglocatie Actuele verwijzing naar de locatie waar het informatieobject ter inzage ligt.
 *  min 0 max unbounded
 * @property dekkingInTijd Een tijdstip of de periode waarop de inhoud van het informatieobject betrekking heeft.
 *  min 0 max unbounded
 * @property dekkingInRuimte Plaats of locatie waar de inhoud van het informatieobject betrekking op heeft.
 *  min 0 max unbounded
 * @property taal De taal waarin het informatieobject gesteld is. Must conform to xsd:language
 *  min 0 max unbounded
 * @property event Gebeurtenis die heeft plaatsgevonden met betrekking tot het ontstaan, wijzigen, vernietigen en beheer van het informatieobject en de bijbehorende metagegevens.
 *  min 0 max unbounded
 * @property waardering De waardering van het informatieobject volgens de van toepassing zijnde en vastgestelde selectielijst.
 *  min 1 max 1
 * @property bewaartermijn Termijn waarin het informatieobject bewaard dient te worden, zoals gespecificeerd in de van toepassing zijnde en vastgestelde selectielijst.
 *  min 0max 1
 * @property informatiecategorie De informatiecategorie uit een vastgestelde selectielijst of hotspotlijst waar de bewaartermijn op gebaseerd is.
 *  min 0 max 1
 * @property isOnderdeelVan De direct bovenliggende aggregatie waar dit informatieobject onderdeel van is.
 *  min 0 max unbounded
 * @property bevatOnderdeel Een Informatieobject dat direct onderliggend onderdeel is van deze aggregatie. Opmerking: Dit is de omgekeerde relatie van isOnderdeelVan.
 *  min 0 max unbounded
 * @property heeftRepresentatie Verwijzing naar het bestand dat een (deel van een) representatie van het informatieobject is.
 *  min 0 max unbounded
 * @property aanvullendeMetaGegevens Verwijzing naar een bestand dat aanvullende (domeinspecifieke) metagegevens over het informatieobject bevat.
 *  min 0 max unbounded
 * @property gerelateerdInformatieobject Relatie met een ander informatieobject dat relevant is binnen de context van het ontstaan, gebruik en/of beheer van dit informatieobject.
 *  min 0 max unbounded
 * @property archiefVormer De organisatie die verantwoordelijk is voor het opmaken en/of ontvangen van het informatieobject.
 *  min 1 max unbounded
 * @property betrokkene Persoon of organisatie die relevant is binnen de context van het ontstaan en gebruik van dit informatieobject.
 *  min 0 max unbounded
 * @property activiteit De bedrijfsactiviteit waarbij het informatieobject door de archiefvormer is ontvangen of gemaakt.
 *  min 0 max unbounded
 * @property beperkingGebruik Een beperking die gesteld is aan het gebruik van het informatieobject.
 *  min 1 max unbounded
 * @throws IllegalArgumentException als [archiefVormer] geen waarde bevat
 * @throws IllegalArgumentException als [beperkingGebruik] geen waarde bevat
 * @throws IllegalArgumentException als [taal] niet voldoet aan xsd:language
 * @throws IllegalArgumentException als [identificatie] geen waarde bevat (by parent class)
 */
data class InformatieobjectType(
    override val identificatie: List<IdentificatieGegevens>,
    override val naam: String,
    val aggregatieniveau: BegripGegevens?,
    val classificatie: List<BegripGegevens>,
    val trefwoord: List<String>,
    val omschrijving: List<String>,
    val raadpleeglocatie: List<RaadpleegLocatieGegevens>,
    val dekkingInTijd: List<DekkingInTijdGegevens>,
    val dekkingInRuimte: List<VerwijzingGegevens>,
    val taal: List<String>,
    val event: List<EventGegevens>,
    val waardering: BegripGegevens,
    val bewaartermijn: TermijnGegevens?,
    val informatiecategorie: BegripGegevens?,
    val isOnderdeelVan: List<VerwijzingGegevens>,
    val bevatOnderdeel: List<VerwijzingGegevens>,
    val heeftRepresentatie: List<VerwijzingGegevens>,
    val aanvullendeMetaGegevens: List<VerwijzingGegevens>,
    val gerelateerdInformatieobject: List<GerelateerdInformatieobjectGegevens>,
    val archiefVormer: List<VerwijzingGegevens>, // must not be empty
    val betrokkene: List<BetrokkeneGegevens>,
    val activiteit: VerwijzingGegevens?,
    val beperkingGebruik: List<BeperkingGebruikGegevens> // must not be empty

): ObjectType(){
    init{
        require(archiefVormer.isNotEmpty()) { "Er moet minimaal 1 \"archiefVormer\" opgegeven worden" }
        require(beperkingGebruik.isNotEmpty()) { "Er moet minimaal 1 \"beperkingGebruik\" opgegeven worden" }
        taal.forEach{
            require(it.isValidLanguageLiteral()) { "$it is geen geldige taal volgens xsd:language"}
        }
    }
}