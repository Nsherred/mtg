package io.nicronomicon.mtg

import io.nicronomicon.mtg.model.CardCollection
import kotlinx.coroutines.Deferred

interface CollectionProvider {
    val collection: Deferred<CardCollection>
}