package com.readyvery.readyverydemo.src.order;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.readyvery.readyverydemo.src.order.dto.OrderUpdateMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventProducer {

	private final KafkaTemplate<String, OrderUpdateMessage> kafkaTemplate;

	public void sendOrderUpdate(OrderUpdateMessage message) {
		try {
			// Kafka로 주문 업데이트 이벤트 전송
			kafkaTemplate.send("order_updates", message);
			log.info("주문 이벤트 전송 성공: {}", message);
		} catch (Exception e) {
			// Kafka 연결 실패 시에도 주문 처리는 계속 진행
			log.warn("Kafka 연결 실패로 주문 이벤트 전송 실패, 주문 처리는 계속 진행: {}", e.getMessage());
			log.debug("Kafka 에러 상세: ", e);
		}
	}
}
