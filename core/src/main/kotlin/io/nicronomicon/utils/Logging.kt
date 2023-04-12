package io.nicronomicon.utils

import org.slf4j.LoggerFactory

inline fun <reified T> createLogger() = LoggerFactory.getLogger(T::class.java)!!
fun createLogger(name: String) = LoggerFactory.getLogger(name)!!