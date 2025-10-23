package nl.joozd.mdtopreingester.tools

/**
 * Check if this String matches the requirements set in https://www.w3.org/TR/xmlschema11-2/#language
 */
fun String.isValidLanguageLiteral(): Boolean{
    val re = """[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*""".toRegex()
    return re matches this
}