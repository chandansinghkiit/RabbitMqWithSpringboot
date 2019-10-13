package com.mystyle.service.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mystyle.config.ApplicationConfigReader;
import com.mystyle.messagelisterner.Product;
import com.mystyle.messagesender.MessageSender;

@RestController
@RequestMapping(path = "/userservice")
public class UserService {
private static final Logger log = LoggerFactory.getLogger(UserService.class);

@Value("${product.exchange.name}")
private String productExchange;
@Value("${product.queue.name}")
private String productQueue;
@Value("${product.routing.key}")
private String productRoutingKey;


private final RabbitTemplate rabbitTemplate;
private ApplicationConfigReader applicationConfig;
private MessageSender messageSender;

public ApplicationConfigReader getApplicationConfig() {
return applicationConfig;
}

@Autowired
public void setApplicationConfig(ApplicationConfigReader applicationConfig) {
this.applicationConfig = applicationConfig;
}

@Autowired
public UserService(final RabbitTemplate rabbitTemplate) {
this.rabbitTemplate = rabbitTemplate;
}
public MessageSender getMessageSender() {
return messageSender;
}
@Autowired
public void setMessageSender(MessageSender messageSender) {
this.messageSender = messageSender;
}


@RequestMapping(path = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
public ResponseEntity<?> sendMessage(@RequestBody Product product) {
String exchange = productExchange;
String routingKey =productRoutingKey;
/* Sending to Message Queue */
try {
messageSender.sendMessage(rabbitTemplate, exchange, routingKey, product);
return new ResponseEntity<String>(ApplicationConstant.IN_QUEUE, HttpStatus.OK);
} catch (Exception ex) 
{
log.error("Exception occurred while sending message to the queue. Exception= {}", ex);
return new ResponseEntity(ApplicationConstant.MESSAGE_QUEUE_SEND_ERROR,
HttpStatus.INTERNAL_SERVER_ERROR);
}
}
}