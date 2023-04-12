package io.nicronomicon.mtg.scryfall

import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
class Bulk(
    val data: List<BulkData>
)

@kotlinx.serialization.Serializable
class BulkData(
    val id: String,
    val type: String,
    val updated_at: Instant,
    val uri: String,
    val name: String,
    val description: String,
    val compressed_size: Double,
    val download_uri: String,
    val content_type: String,
    val content_encoding: String
)


/*

{
  "object": "list",
  "has_more": false,
  "data": [
    {
      "object": "bulk_data",
      "id": "27bf3214-1271-490b-bdfe-c0be6c23d02e",
      "type": "oracle_cards",
      "updated_at": "2022-03-28T09:04:22.878+00:00",
      "uri": "https://api.scryfall.com/bulk-data/27bf3214-1271-490b-bdfe-c0be6c23d02e",
      "name": "Oracle Cards",
      "description": "A JSON file containing one Scryfall card object for each Oracle ID on Scryfall. The chosen sets for the cards are an attempt to return the most up-to-date recognizable version of the card.",
      "compressed_size": 13463765,
      "download_uri": "https://c2.scryfall.com/file/scryfall-bulk/oracle-cards/oracle-cards-20220328090422.json",
      "content_type": "application/json",
      "content_encoding": "gzip"
    },
    {
      "object": "bulk_data",
      "id": "6bbcf976-6369-4401-88fc-3a9e4984c305",
      "type": "unique_artwork",
      "updated_at": "2022-03-28T09:13:44.959+00:00",
      "uri": "https://api.scryfall.com/bulk-data/6bbcf976-6369-4401-88fc-3a9e4984c305",
      "name": "Unique Artwork",
      "description": "A JSON file of Scryfall card objects that together contain all unique artworks. The chosen cards promote the best image scans.",
      "compressed_size": 16722507,
      "download_uri": "https://c2.scryfall.com/file/scryfall-bulk/unique-artwork/unique-artwork-20220328091344.json",
      "content_type": "application/json",
      "content_encoding": "gzip"
    },
    {
      "object": "bulk_data",
      "id": "e2ef41e3-5778-4bc2-af3f-78eca4dd9c23",
      "type": "default_cards",
      "updated_at": "2022-03-28T09:02:53.702+00:00",
      "uri": "https://api.scryfall.com/bulk-data/e2ef41e3-5778-4bc2-af3f-78eca4dd9c23",
      "name": "Default Cards",
      "description": "A JSON file containing every card object on Scryfall in English or the printed language if the card is only available in one language.",
      "compressed_size": 34736502,
      "download_uri": "https://c2.scryfall.com/file/scryfall-bulk/default-cards/default-cards-20220328090253.json",
      "content_type": "application/json",
      "content_encoding": "gzip"
    },
    {
      "object": "bulk_data",
      "id": "922288cb-4bef-45e1-bb30-0c2bd3d3534f",
      "type": "all_cards",
      "updated_at": "2022-03-28T09:12:26.861+00:00",
      "uri": "https://api.scryfall.com/bulk-data/922288cb-4bef-45e1-bb30-0c2bd3d3534f",
      "name": "All Cards",
      "description": "A JSON file containing every card object on Scryfall in every language.",
      "compressed_size": 217554523,
      "download_uri": "https://c2.scryfall.com/file/scryfall-bulk/all-cards/all-cards-20220328091226.json",
      "content_type": "application/json",
      "content_encoding": "gzip"
    },
    {
      "object": "bulk_data",
      "id": "06f54c0b-ab9c-452d-b35a-8297db5eb940",
      "type": "rulings",
      "updated_at": "2022-03-28T09:04:33.370+00:00",
      "uri": "https://api.scryfall.com/bulk-data/06f54c0b-ab9c-452d-b35a-8297db5eb940",
      "name": "Rulings",
      "description": "A JSON file containing all Rulings on Scryfall. Each ruling refers to cards via an `oracle_id`.",
      "compressed_size": 3157975,
      "download_uri": "https://c2.scryfall.com/file/scryfall-bulk/rulings/rulings-20220328090433.json",
      "content_type": "application/json",
      "content_encoding": "gzip"
    }
  ]
}

*/