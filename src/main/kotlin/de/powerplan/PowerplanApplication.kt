package de.powerplan

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PowerplanApplication

fun main(args: Array<String>) {
    runApplication<PowerplanApplication>(*args)
}
