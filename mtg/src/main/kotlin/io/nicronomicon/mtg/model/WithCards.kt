package io.nicronomicon.mtg.model

interface WithCards {
    val cards: List<CardQuantity>
    fun countOf(card: Card): Double
}