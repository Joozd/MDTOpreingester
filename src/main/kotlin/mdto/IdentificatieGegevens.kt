package nl.joozd.mdtopreingester.mdto

/**
 * MDTO 1.0.1
 * @property identificatieKenmerk Een kenmerk waarmee een object geïdentificeerd kan worden.
 *  min 1 max 1
 * @property identificatieBron
 *  min 1 max 1
 */
data class IdentificatieGegevens(
    val identificatieKenmerk: String,
    val identificatieBron: String
)