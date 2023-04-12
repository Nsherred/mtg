package io.nicronomicon.mtg.result

import io.nicronomicon.mtg.model.MTGResult
import java.util.function.Predicate

object IsTop8 : Predicate<MTGResult> {
    private val top8Set = setOf(
        "1", "2", "3", "4", "5", "6", "7", "8", "3-4", "5-8"
    )
    override fun test(t: MTGResult) = top8Set.contains(t.placement)

}