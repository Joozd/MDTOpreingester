package nl.joozd.mdtopreingester.tools

import java.net.URI

/** Minimal-conforming validator for xsd:anyURI (XML Schema 1.0 §3.2.17). */
fun String.isValidAnyUriLiteral(): Boolean {
    // XSD whitespace facet for anyURI is 'collapse'
    val collapsed = this
        .replace("\t", " ").replace("\r", " ").replace("\n", " ")
        .replace(Regex(" +"), " ").trim()

    // XLink escaping (minimally: encode spaces). No absolutization.
    val candidate = collapsed.replace(" ", "%20")

    return try {
        URI(candidate)        // accepts absolute or relative; RFC2396/2732 compatible
        true
    } catch (_: Exception) {
        false
    }
}