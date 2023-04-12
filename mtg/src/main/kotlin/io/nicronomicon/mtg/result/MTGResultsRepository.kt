package io.nicronomicon.mtg.result

import io.nicronomicon.ecs.storage.Storage
import io.nicronomicon.mtg.model.MTGResult
import java.util.function.Predicate


class MTGResultsRepository(private val storage: Storage<MTGResult>) {
    suspend fun find(predicate: Predicate<MTGResult>): Sequence<MTGResult> {
        return storage.getAll().filter { predicate.test(it) }
    }
}