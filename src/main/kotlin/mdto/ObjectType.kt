package nl.joozd.mdtopreingester.mdto

/**
 * MDTO 1.0.1
 * @property identificatie Gegevens waarmee het object geïdentificeerd kan worden.
 *  min 1 max unbounded.
 * @property naam Een betekenisvolle aanduiding waaronder het object bekend is.
 *  min 1 max 1
 */
data class ObjectType(
    val identificatie: List<IdentificatieGegevens>, // min 1 max unbounded
    val naam: String
){
    init{
        assert(identificatie.isNotEmpty())
    }
}
