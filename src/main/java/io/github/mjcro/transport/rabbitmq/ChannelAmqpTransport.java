package io.github.mjcro.transport.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import io.github.mjcro.interfaces.experimental.integration.Option;
import io.github.mjcro.interfaces.experimental.integration.amqp.AmqpAsyncTransport;
import io.github.mjcro.interfaces.experimental.integration.amqp.AmqpRequest;
import io.github.mjcro.interfaces.experimental.integration.amqp.AmqpResponse;
import io.github.mjcro.interfaces.experimental.integration.amqp.Properties;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class ChannelAmqpTransport implements AmqpAsyncTransport<CompletableFuture<AmqpResponse>> {
    private final AtomicReference<String> responseQueueNameContainer = new AtomicReference<>();
    private final Object lock = new Object();
    private final Channel channel;

    public ChannelAmqpTransport(Channel channel) {
        this.channel = Objects.requireNonNull(channel, "channel");
    }

    @Override
    public void publish(AmqpRequest request, Option... options) {
        AMQP.BasicProperties properties = buildBasicProperties(request, options, null, null);
        doPublish(request, options, properties);
    }

    @Override
    public CompletableFuture<AmqpResponse> sendAsync(AmqpRequest request, Option... options) {
        String responseQueue = allocateResponseQueueName();
        String correlationID = generateCorrelationId();
        AMQP.BasicProperties properties = buildBasicProperties(request, options, responseQueue, correlationID);
        doPublish(request, options, properties);
        CompletableFuture<AmqpResponse> future = new CompletableFuture<>();

        return null;
    }

    private void doPublish(AmqpRequest request, Option[] options, AMQP.BasicProperties properties) {
        try {
            channel.basicPublish(
                    request.getExchange().orElse(""),
                    request.getRoutingKey().orElse(""),
                    true,
                    properties,
                    request.getBody()
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AMQP.BasicProperties buildBasicProperties(
            AmqpRequest request,
            Option[] options,
            String replyTo,
            String correlationId
    ) {
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder();
        request.getProperties().flatMap(Properties::getType).ifPresent(builder::type);
        request.getProperties().flatMap(Properties::getContentType).ifPresent(builder::contentType);
        request.getProperties().flatMap(Properties::getContentEncoding).ifPresent(builder::contentEncoding);
        if (replyTo == null) {
            request.getProperties().flatMap(Properties::getReplyTo).ifPresent(builder::replyTo);
        } else {
            builder.replyTo(replyTo);
        }
        if (correlationId == null) {
            request.getProperties().flatMap(Properties::getCorrelationId).ifPresent(builder::correlationId);
        } else {
            builder.correlationId(correlationId);
        }
        // TODO apply options

        return builder.build();
    }

    private String allocateResponseQueueName() {
        String name = responseQueueNameContainer.get();
        if (name == null) {
            synchronized (lock) {
                name = responseQueueNameContainer.get();
                if (name == null) {
                    try {
                        name = channel.queueDeclare("", false, true, true, null).getQueue();
                        channel.basicConsume(name, new DefaultConsumer(channel) {
                            @Override
                            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                                ChannelAmqpTransport.this.handleDelivery(consumerTag, envelope, properties, body);
                            }
                        });
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return null;
    }

    private String generateCorrelationId() {
        return null; // TODO
    }

    private void registerFuture(String correlationId, CompletableFuture<AmqpResponse> future) {
        // TODO
    }

    private void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        // TODO
    }
}
