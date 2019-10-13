package com.mystyle.messagelisterner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.mystyle.config.ApplicationConfigReader;

/**
 * Message Listener for RabbitMQ
 */

@Service
public class MessageListener {
	
	
    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);
    
    @Autowired
    ApplicationConfigReader applicationConfigReader;
    
    /**
     * Message listener for Product
     * @param UserDetails a user defined object used for deserialization of message
     */
    @RabbitListener(queues = "${product.queue.name}")
    public void receiveMessageForProduct(final Product data) {
    log.info("Received message: {} from product queue.", data);
    try {
    log.info("Making REST call to the  API");
    //TODO: Code to make REST call
        log.info("<< Exiting receiveMessageForProduct() after API call.");
    } catch(HttpClientErrorException  ex) {
    if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.info("Delay...");
        try {
    Thread.sleep(9000);
    } catch (InterruptedException e) { }
    log.info("Throwing exception so that message will be requed in the queue.");
    // Note: Typically Application specific exception should be thrown below
    throw new RuntimeException();
    } else {
    throw new AmqpRejectAndDontRequeueException(ex); 
    }
    } catch(Exception e) {
    log.error("Internal server error occurred in API call. Bypassing message requeue {}", e);
    throw new AmqpRejectAndDontRequeueException(e); 
    }
    }
    
    /**
     * Message listener for USER
     * 
     */
    @RabbitListener(queues = "${user.queue.name}")
    public void receiveMessageForUser(String reqObj) {
    log.info("Received message: {} from user queue.", reqObj);
    try {
    log.info("Making REST call to the API");
    //TODO: Code to make REST call
        log.info("<< Exiting receiveMessageUSER() after API call.");
    } catch(HttpClientErrorException  ex) {
    if(ex.getStatusCode() == HttpStatus.NOT_FOUND) {
        log.info("Delay...");
        try {
    Thread.sleep(9000);
    } catch (InterruptedException e) { }
    log.info("Throwing exception so that message will be requed in the queue.");
    // Note: Typically Application specific exception can be thrown below
    throw new RuntimeException();
    } else {
    throw new AmqpRejectAndDontRequeueException(ex); 
    }
    } catch(Exception e) {
    log.error("Internal server error occurred in  server. Bypassing message requeue {}", e);
    throw new AmqpRejectAndDontRequeueException(e); 
    }
    }
}