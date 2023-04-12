package io.nicronomicon.mtg

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.nicronomicon.utils.defaultClient
import org.slf4j.LoggerFactory

class DeckDownloader(private val httpClient: HttpClient = defaultClient) {

    suspend fun downloadDecks(startId: Int, endId: Int): List<RawDeck> {
        val result = mutableListOf<RawDeck>()
        for (id in startId..endId) {
            result.add(downloadDeck(id))
        }
        return result
    }

    suspend fun downloadDeck(id: Int): RawDeck {
        val url = "https://mtgtop8.com/mtgo?d=${id}"
        log.info("getting deck $url")
        val request = HttpRequestBuilder().apply {
            header(
                "Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9"
            )
            header("Accept-Language", "en-US,en;q=0.9")
            url(url)
        }
        val data = httpClient.get(request).bodyAsText()
        return RawDeck(id.toString(), data)
    }

    companion object {
        val log = LoggerFactory.getLogger(DeckDownloader::class.java)!!
    }
}
