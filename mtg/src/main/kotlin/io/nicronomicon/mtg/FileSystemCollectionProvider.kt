package io.nicronomicon.mtg

import io.nicronomicon.mtg.model.CardCollection
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class FileSystemCollectionProvider(
    json: Json = defaultJson
) : CollectionProvider {
    private val oracleFile = File("./collection.json")
    override val collection = CompletableDeferred<CardCollection>()

    init {
        if (!oracleFile.exists()) {
            throw Exception("Oracle file does not exist")
        }
        val fileContent = oracleFile.readText()
        collection.complete(json.decodeFromString(fileContent))
    }
}