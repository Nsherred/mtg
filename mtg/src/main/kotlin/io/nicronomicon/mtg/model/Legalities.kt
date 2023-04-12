package io.nicronomicon.mtg.model

import kotlinx.serialization.Serializable

@Serializable
data class Legalities(
    val standard: String,
    val future: String,
    val historic: String,
    val gladiator: String,
    val pioneer: String,
    val modern: String,
    val legacy: String,
    val pauper: String,
    val vintage: String,
    val penny: String,
    val commander: String,
    val brawl: String,
    val historicbrawl: String,
    val alchemy: String,
    val paupercommander: String,
    val duel: String,
    val oldschool: String,
    val premodern: String
)