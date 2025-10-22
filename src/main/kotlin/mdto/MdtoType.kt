package nl.joozd.mdtopreingester.mdto

data class MdtoType(
    val informatieobject: InformatieObjectType? = null,
    val bestand: BestandType? = null
){
    init{
        require(informatieobject != null || bestand != null){ "Een MDTO record moet óf een informatieobject, óf een bestand bevatten, niet geen van beide"}
        require(informatieobject == null || bestand == null){ "Een MDTO record moet óf een informatieobject, óf een bestand bevatten, niet allebei"}
    }
}
