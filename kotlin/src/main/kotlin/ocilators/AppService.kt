package ocilators

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.ContentNegotiation
import io.ktor.features.DefaultHeaders
import io.ktor.http.ContentType
import io.ktor.jackson.JacksonConverter
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.netty.*
import org.slf4j.event.Level
import kotlin.random.Random


internal val mapper = jacksonObjectMapper()

data class OscillatorResponse(val v: List<Int>, val stddev: Double)

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

private fun genRandom(size: Int) = List(size) { Random.nextInt(0, 100) }

/**
 * Start the netty server to host the application.
 */
fun main(args: Array<String>) {
    EngineMain.main(args)
}


/**
 * Syntactic sugar
 */
fun List<Number>.mean() : Double {
    return map(Number::toDouble).sum() / size
}

fun List<Number>.variance() : Double {
    val mean = mean()
    return map {
        val delta = it.toDouble() - mean
        delta * delta
    }.sum() / size
}

fun List<Number>.standardDeviation() : Double {
    return StrictMath.sqrt(variance())
}

