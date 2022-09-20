package br.com.websocket.webSocketApp.messages

import br.com.websocket.webSocketApp.dtos.Message
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy
import io.awspring.cloud.messaging.listener.annotation.SqsListener
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class MessageListener(private val template: SimpMessagingTemplate, private val objectMapper: ObjectMapper) {

    @SqsListener(
        value = ["\${app.sqs.queue-name}"],
        deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS
    )
    fun receiveNotification(message: String) {
        val info = this.objectMapper.readValue(message, Array<Message>::class.java)
        template.convertAndSend("/statusProcessor", info)
    }

}