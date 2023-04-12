package io.nicronomicon.mtg

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.call.body
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.nicronomicon.mtg.model.Card
import io.nicronomicon.mtg.model.CardCollection
import io.nicronomicon.mtg.scryfall.Bulk
import io.nicronomicon.utils.defaultClient
import io.nicronomicon.utils.defaultJson
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.io.File
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@OptIn(ExperimentalSerializationApi::class)
class ScryFallCollectionProvider(
    private val client: HttpClient = defaultClient,
    private val json: Json = defaultJson,
) : CoroutineScope, CollectionProvider {

    override val collection = CompletableDeferred<CardCollection>()
    override val coroutineContext = Dispatchers.Default + SupervisorJob()
    private val oracleFile = File("./collection.json")
    private val lock = Mutex(true)

    init {
        runBlocking {
            if (oracleFile.exists()) {
                val fileContent = oracleFile.readText()
                collection.complete(json.decodeFromString(fileContent))
            }
        }

        launch {
            CardService.log.info("Getting Card Collection")
            val newCollection = getCollection()
            withContext(Dispatchers.IO) {
                with(oracleFile.outputStream()) {
                    json.encodeToStream(newCollection, this)
                }
            }
            collection.complete(newCollection)
            CardService.log.info("Retrieved Card Collection")
            delay(24.toDuration(DurationUnit.HOURS))
        }
    }

    private suspend fun getCollection(): CardCollection {

        val bulk: Bulk = client.get("https://api.scryfall.com/bulk-data").body()

        val oracle = bulk.data.find { it.name == "Oracle Cards" }
            ?: throw Exception("could not find oracle cards in bult data")
        val url = oracle.download_uri
//        val updatedAt = Instant.parse(oracle.updated_at)
        val currentCollection = withTimeoutOrNull(100) { collection.await() }
            ?: CardCollection.Default
        if (currentCollection.updatedAt >= oracle.updated_at) {
            CardService.log.info("collection has not changed, using existing one")
            return currentCollection
        }
        CardService.log.info("oracle data was updated at ${oracle.updated_at}, pulling down bulk data.")
        val cleanedCards: String = client.get(url).bodyAsText()
            .replace(Regex("([-_][a-z])")) {
                it.value.uppercase()
                    .replace("-", "")
                    .replace("_", "")
            }
        val cards = json.decodeFromString<List<Card>>(cleanedCards)
        return CardCollection(oracle.updated_at, cards)
    }

}
