package io.nicronomicon.mtg.model

import kotlinx.serialization.Serializable

@Serializable
class FormatLegality(
    val legal: List<Card>,
    val notLegal: List<Card>,
    val restricted: List<Card>,
    val banned: List<Card>
)