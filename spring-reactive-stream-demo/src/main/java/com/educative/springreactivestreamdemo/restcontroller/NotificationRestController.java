package com.educative.springreactivestreamdemo.restcontroller;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.educative.springreactivestreamdemo.model.Notification;
import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;

@RestController
public class NotificationRestController {

	private static final String[] orderStatusReason = { "successfully processed", "in-progress", "payment failed" };
	private static final String NOTIFICATION_TOPIC = "notification";

	@Autowired
	PubSubPublisherTemplate pubSubPublisherTemplate;

	@PostMapping(value = "/notifications")
	public String publishNotification() {
		Notification notification = new Notification();
		StringBuilder orderMessage = new StringBuilder();
		orderMessage.append("Order ID [" + UUID.randomUUID().toString() + "] ");
		orderMessage.append(orderStatusReason[ThreadLocalRandom.current().nextInt(0, 2 + 1)]);
		notification.setMessage(orderMessage.toString());
		pubSubPublisherTemplate.publish(NOTIFICATION_TOPIC, notification);
		return "Notification published successfully";
	}
}
