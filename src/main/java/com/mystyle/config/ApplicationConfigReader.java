package com.mystyle.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
@PropertySource("classpath:application.properties")
public class ApplicationConfigReader  implements RabbitListenerConfigurer{
	
@Value("${product.exchange.name}")
private String productExchange;
@Value("${product.queue.name}")
private String productQueue;
@Value("${product.routing.key}")
private String productRoutingKey;

@Value("${user.exchange.name}")
private String userExchange;
@Value("${user.queue.name}")
private String userQueue;
@Value("${user.routing.key}")
private String userRoutingKey;




@Bean
public TopicExchange getProductExchange() {
return new TopicExchange(productExchange);
}
/* Creating a bean for the Message queue */
@Bean
public Queue getProductQueue() {
return new Queue(productQueue);
}
/* Binding between Exchange and Queue using routing key */
/* Binding between Exchange and Queue using routing key */
@Bean
public Binding declareBindingProduct() {
return BindingBuilder.bind(getProductQueue()).to(getProductExchange()).with(productRoutingKey);
}



/* Creating a bean for the Message queue Exchange */
@Bean
public TopicExchange getUserExchange() {
return new TopicExchange(userExchange);
}
/* Creating a bean for the Message queue */
@Bean
public Queue getUserQueue() {
return new Queue(userQueue);
}
/* Binding between Exchange and Queue using routing key */
@Bean
public Binding declareBindingUser() {
return BindingBuilder.bind(getUserQueue()).to(getUserExchange()).with(userRoutingKey);
}



/* Bean for rabbitTemplate */
@Bean
public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
return rabbitTemplate;
}
@Bean
public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
return new Jackson2JsonMessageConverter();
}
@Bean
public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
return new MappingJackson2MessageConverter();
}
@Bean
public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
factory.setMessageConverter(consumerJackson2MessageConverter());
return factory;
}

@Override
public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
}




}