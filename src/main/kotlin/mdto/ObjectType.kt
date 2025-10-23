package nl.joozd.mdtopreingester.mdto

/**
 * MDTO 1.0.1
 * @property identificatie Gegevens waarmee het object geïdentificeerd kan worden.
 *  min 1 max unbounded.
 * @property naam Een betekenisvolle aanduiding waaronder het object bekend is.
 *  min 1 max 1
 * @throws IllegalArgumentException als [identificatie] geen waarde bevat
 */
sealed class ObjectType(

){
    abstract val identificatie: List<IdentificatieGegevens> // min 1 max unbounded
    abstract val naam: String
    init{
        require(identificatie.isNotEmpty()) { "Er moet minimaal 1 \"identificatie\" opgegeven worden" }
    }
}
