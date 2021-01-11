package ocilators

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.netty.*
import kotlin.random.Random

internal val mapper = jacksonObjectMapper()
private data class OscillatorResponse(val v: List<Int>, val stddev: Double)

/**
 * Simple KTOR module
 */
fun Application.module() {
    install(DefaultHeaders)

    routing {
        // Entry for the graphql service where we
        get("/oscillators/{samples}") {
            val samples = call.parameters["samples"]?.toInt() ?: 1000
            val random = genRandom(samples)
            val jsonResponse = mapper.writeValueAsString(OscillatorResponse(random, random.standardDeviation()))
            call.respondText(jsonResponse, ContentType.Application.Json)
        }
    }
}

/**
 * Generate random list of size.
 */
private fun genRandom(size: Int) = List(size) { Random.nextInt(0, 100) }

/**
 * Extension Syntactic sugar: map list of numbers to standard deviation
 */
internal fun List<Number>.standardDeviation() : Double {
    val mean = map(Number::toDouble).sum() / size
    val variance = map {
        val delta = it.toDouble() - mean
        delta * delta
    }.sum() / size
    return StrictMath.sqrt(variance)
}

/**
 * Start the app server
 */
fun main(args: Array<String>) {
    EngineMain.main(args)
}

