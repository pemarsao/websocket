package br.com.websocket.webSocketApp.dtos

import java.time.LocalDate

data class Message(
    val subject: String? = null,
    val message: String? = null,
    val createAt: LocalDate? = LocalDate.now()
)