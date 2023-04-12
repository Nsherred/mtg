package io.nicronomicon.mtg

import io.nicronomicon.mtg.model.Card
import io.nicronomicon.utils.createLogger
import java.util.*


class CardService(
    private val collectionProvider: CollectionProvider
) {

    suspend fun findCardByName(name: String): Card? {
        val parsedName = name.lowercase(Locale.getDefault())
        return collectionProvider.collection.await().byName[parsedName]
            ?: collectionProvider.collection.await().nameContains(parsedName)
    }

    suspend fun findByCmc(cmc: Double) = collectionProvider.collection.await().byCmc[cmc]

    suspend fun findByFormat(format: String) =
        collectionProvider.collection.await().byFormat[format]?.legal

    companion object {
        val log = createLogger<CardService>()
    }


}