package io.nicronomicon.ecs

import kotlinx.serialization.Serializable
import java.util.*

@JvmInline
@Serializable
value class ID(val value: String){
    companion object{
        val TEMP = ID("0")
        fun makeRandomID() =  UUID.randomUUID().toID()
    }
}

fun UUID.toID() = ID(toString())
