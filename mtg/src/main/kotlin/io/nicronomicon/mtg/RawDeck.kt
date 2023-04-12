package io.nicronomicon.mtg

import kotlinx.serialization.Serializable

@Serializable
class RawDeck(val name: String, val data: String)