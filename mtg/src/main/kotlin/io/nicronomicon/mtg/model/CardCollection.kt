package io.nicronomicon.mtg.model

import io.nicronomicon.utils.readInstanceProperty
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.*

@Serializable
class CardCollection(
    val updatedAt: Instant,
    val cards: List<Card>
) {

    @Transient
    val byName = cards.associateBy {
        it.name.lowercase(Locale.getDefault())
    }

    fun nameContains(name: String): Card? {
        val searchable = name.replace("/", "//")
        return byName.filter { it.key.contains(searchable) }.values.firstOrNull()
    }

    @Transient
    val byCmc = cards.groupBy { it.cmc }

    @Transient
    val byFormat: Map<String, FormatLegality> = formats.associateWith { format ->
        val legal = mutableListOf<Card>()
        val notLegal = mutableListOf<Card>()
        val restricted = mutableListOf<Card>()
        val banned = mutableListOf<Card>()
        cards.forEach {
            when (readInstanceProperty<String>(it.legalities, format)) {
                "legal" -> legal.add(it)
                "not_legal" -> notLegal.add(it)
                "restricted" -> restricted.add(it)
                "banned" -> banned.add(it)
            }
        }
        FormatLegality(
            legal,
            notLegal,
            restricted,
            banned
        )
    }

    companion object {
        val formats = listOf(
            "standard",
            "future",
            "historic",
            "gladiator",
            "pioneer",
            "modern",
            "legacy",
            "pauper",
            "vintage",
            "penny",
            "commander",
            "brawl",
            "historicbrawl",
            "alchemy",
            "paupercommander",
            "duel",
            "oldschool",
            "premodern"
        )
        val Default = CardCollection(Instant.DISTANT_PAST, emptyList())
    }
}

