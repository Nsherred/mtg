package io.nicronomicon.mtg.model
import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class DeckVersion(val version: String) {
    val major: Int
        get() = version.split(".")[0].toInt()
    val minor: Int
        get() = version.split(".")[2].toInt()
    val patch: Int
        get() = version.split(".")[3].toInt()
    companion object{
        val default = DeckVersion("0.0.0")
    }
}

