package io.nicronomicon.mtg.model
import io.nicronomicon.ecs.Entity
import io.nicronomicon.ecs.ID
import kotlinx.serialization.Serializable


@Serializable
data class Card(
    override val id: ID,
    val oracleId: String,
    val name: String,
    val lang: String,
    val releasedAt: String,
    val cmc: Double,
    val manaCost: String? = null,
    val legalities: Legalities
): Entity


//interface Card {
//    val id: String
//    val oracleId: String
//    val name: String
//    val lang: String
//    val releasedAt: Instant
//    val uri: String
//    val scryfallUri: String
//    val layout: String
//    val highresImage: Boolean
//    val imageStatus: String
//    val imageUris: ImageUris
//    val manaCost: String
//    val cmc: Number
//    val typeLine: String
//    val oracleText: String
//    val colors: List<String>
//    val colorIdentity: List<String>
//    val keywords: List<String>
//    val legalities: Legalities
//    val games: List<String>
//    val reserved: Boolean
//    val foil: Boolean
//    val nonfoil: Boolean
//    val finishes: List<String>
//    val oversized: Boolean
//    val promo: Boolean
//    val reprint: Boolean
//    val variation: Boolean
//    val setId: String
//    val set: String
//    val setName: String
//    val setType: String
//    val setUri: String
//    val setSearchUri: String
//    val scryfallSetUri: String
//    val rulingsUri: String
//    val printsSearchUri: String
//    val collectorNumber: String
//    val digital: Boolean
//    val rarity: String
//    val flavorText: String
//    val cardBackId: String
//    val artist: String
//    val artistIds: List<String>
//    val illustrationId: String
//    val borderColor: String
//    val frame: String
//    val fullArt: Boolean
//    val textless: Boolean
//    val booster: Boolean
//
//}
//
//interface ImageUris {
//    val small: String
//    val normal: String
//    val large: String
//    val png: String
//    val artCrop: String
//    val borderCrop: String
//}
//interface Legalities {
//    val standard: String
//    val future: String
//    val historic: String
//    val gladiator: String
//    val pioneer: String
//    val modern: String
//    val legacy: String
//    val pauper: String
//    val vintage: String
//    val penny: String
//    val commander: String
//    val brawl: String
//    val historicbrawl: String
//    val alchemy: String
//    val paupercommander: String
//    val duel: String
//    val oldschool: String
//    val premodern: String
//}
//interface Prices {
//    val usd: String
//    val usdFoil: String
//    val usdEtched: String
//    val eur: String
//    val eurFoil: String
//    val tix: String
//}
//interface RelatedUris {
//    val gatherer: String
//    val tcgplayerInfiniteArticles: String
//    val tcgplayerInfiniteDecks: String
//    val edhrec: String
//    val mtgtop8: String
//}
