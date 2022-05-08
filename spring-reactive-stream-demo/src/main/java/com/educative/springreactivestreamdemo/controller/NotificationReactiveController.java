package com.educative.springreactivestreamdemo.controller;

import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.cloud.spring.pubsub.reactive.PubSubReactiveFactory;
import com.google.cloud.spring.pubsub.support.AcknowledgeablePubsubMessage;

import reactor.core.publisher.Flux;

@Controller
@ResponseBody
public class NotificationReactiveController {

	@Autowired
	PubSubReactiveFactory pubSubReactiveFactory;
	
	private static final String NOTIFICATION_SUBSCRIPTION = "notification-subscription";
	
	@GetMapping(value = "/notifications", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> notifications() {
		Flux<AcknowledgeablePubsubMessage> flux = this.pubSubReactiveFactory.poll(NOTIFICATION_SUBSCRIPTION, 1000);
		return flux.doOnNext(notification -> {
			System.out.println("Message received successfully and a Message ID is: " + notification.getPubsubMessage().getMessageId());
			notification.ack();
		}).map(notification -> new String(notification.getPubsubMessage().getData().toByteArray(), Charset.defaultCharset()));
	}
}
