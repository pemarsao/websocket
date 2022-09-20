package br.com.websocket.webSocketApp.config

import brave.propagation.B3Propagation
import brave.propagation.Propagation
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.auth.WebIdentityTokenCredentialsProvider
import com.amazonaws.services.sqs.AmazonSQSAsync
import com.fasterxml.jackson.databind.ObjectMapper
import io.awspring.cloud.core.env.ResourceIdResolver
import io.awspring.cloud.core.env.StackResourceRegistryDetectingResourceIdResolver
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory
import io.awspring.cloud.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.core.DestinationResolver

@Configuration
class AwsConfig {

    @Bean
    fun queueMessagingTemplate(
        amazonSQS: AmazonSQSAsync,
        destinationResolver: DestinationResolver<String>,
        objectMapper: ObjectMapper
    ): QueueMessagingTemplate {
        val jackson2MessageConverter = MappingJackson2MessageConverter()
        jackson2MessageConverter.objectMapper = objectMapper
        jackson2MessageConverter.serializedPayloadClass = String::class.java

        return QueueMessagingTemplate(amazonSQS, destinationResolver, jackson2MessageConverter)
    }

    @Bean
    @Primary
    fun sleuthPropagate(): Propagation.Factory {
        return B3Propagation.newFactoryBuilder()
            .injectFormat(B3Propagation.Format.MULTI)
            .build()
    }

    @Bean
    fun awsCredentialsProvider(): AWSCredentialsProvider {
        if (System.getenv().containsKey("AWS_WEB_IDENTITY_TOKEN_FILE")) {
            return WebIdentityTokenCredentialsProvider.create()
        }
        return DefaultAWSCredentialsProviderChain()
    }

    @Bean
    fun destinationResolver(
        amazonSQS: AmazonSQSAsync,
        resourceIdResolver: ResourceIdResolver
    ): DestinationResolver<String> {
        return DynamicAccountAwareQueueUrlDestinationResolver(amazonSQS, resourceIdResolver)
    }

    @Bean
    fun simpleMessageListenerFactory(destinationResolver: DestinationResolver<String>): SimpleMessageListenerContainerFactory? {
        val factory = SimpleMessageListenerContainerFactory()
        factory.setDestinationResolver(destinationResolver)
        return factory
    }

    @Bean
    fun resourceIdResolver(): ResourceIdResolver {
        return StackResourceRegistryDetectingResourceIdResolver()
    }

}