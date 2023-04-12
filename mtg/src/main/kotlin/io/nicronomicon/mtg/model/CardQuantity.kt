package io.nicronomicon.mtg.model

import kotlinx.serialization.Serializable

@Serializable
data class CardQuantity(val quantity: Int, val card: Card)