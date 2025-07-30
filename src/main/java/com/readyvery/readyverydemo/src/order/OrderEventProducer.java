package com.readyvery.readyverydemo.src.order;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.src.order.dto.OrderUpdateMessage;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {

	private final KafkaTemplate<String, OrderUpdateMessage> kafkaTemplate;

	public void sendOrderUpdate(OrderUpdateMessage message) {
		// 예: "order_updates"라는 토픽 사용
		kafkaTemplate.send("order_updates", message);
	}
}
