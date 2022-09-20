package br.com.websocket.webSocketApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WebSocketAppApplication

fun main(args: Array<String>) {
	runApplication<WebSocketAppApplication>(*args)
}
