package io.nicronomicon.mtg

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/card")
class CardResource {
    @Serializable
    @Resource("/{name}")
    class NameSearch(val parent: CardResource = CardResource(), val name: String)

    @Serializable
    @Resource("/cmc")
    class CMC(val parent: CardResource = CardResource()) {
        @Serializable
        @Resource("{cmc}")
        class Search(val parent: CMC = CMC(), val cmc: Double)
    }

    @Serializable
    @Resource("/format")
    class Format(val parent: CardResource = CardResource()) {
        @Serializable
        @Resource("{format}")
        class Search(val parent: Format = Format(), val format: String)
    }
}