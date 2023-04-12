package io.nicronomicon.ecs

interface Component {
    val id: ID
    val parentId: ID
}