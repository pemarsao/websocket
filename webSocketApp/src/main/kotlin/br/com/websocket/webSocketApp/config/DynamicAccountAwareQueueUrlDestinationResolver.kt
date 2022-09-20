package br.com.websocket.webSocketApp.config

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.GetQueueUrlRequest
import io.awspring.cloud.core.env.ResourceIdResolver
import io.awspring.cloud.messaging.support.destination.DynamicQueueUrlDestinationResolver
import org.springframework.messaging.core.DestinationResolutionException
import org.springframework.messaging.core.DestinationResolver

class DynamicAccountAwareQueueUrlDestinationResolver(private val amazonSqs: AmazonSQS,
                                                     private val resourceIdResolver: ResourceIdResolver
) :
    DestinationResolver<String> {

    companion object {
        const val ACCOUNT_QUEUE_SEPARATOR = "/"
    }

    private val dynamicQueueUrlDestinationResolverDelegate: DynamicQueueUrlDestinationResolver
        by lazy { DynamicQueueUrlDestinationResolver(amazonSqs, resourceIdResolver) }

    @Throws(DestinationResolutionException::class)
    override fun resolveDestination(queue: String): String {
        return if (queue.contains(ACCOUNT_QUEUE_SEPARATOR)) {
            val account = queue.substring(0, queue.indexOf(ACCOUNT_QUEUE_SEPARATOR))
            val queueName = queue.substring(queue.indexOf(ACCOUNT_QUEUE_SEPARATOR) + 1)
            try {
                val queueUrlResult = amazonSqs.getQueueUrl(
                    GetQueueUrlRequest()
                        .withQueueName(queueName)
                        .withQueueOwnerAWSAccountId(account)
                )
                queueUrlResult.queueUrl
            } catch (e: RuntimeException) {
                dynamicQueueUrlDestinationResolverDelegate.resolveDestination(queue);
            }
        } else {
            dynamicQueueUrlDestinationResolverDelegate.resolveDestination(queue)
        }
    }
}