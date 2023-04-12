package io.nicronomicon.mtg

import io.nicronomicon.mtg.model.CardCollection
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TestCollectionProvider(
    json: Json = defaultJson
) : CollectionProvider {
    private val resource = this::class.java.getResource("/collection.json")
    override val collection = CompletableDeferred<CardCollection>()

    init {
        if (resource == null) {
            throw Exception("Oracle file does not exist")
        }
        val fileContent = resource.readText()
        collection.complete(json.decodeFromString(fileContent))
    }
}