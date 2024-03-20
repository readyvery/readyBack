package com.readyvery.readyverydemo.src.smsauthentication;

import org.springframework.stereotype.Service;

import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageSendingService {
	private final DefaultMessageService messageService;

	public boolean sendMessage(String to, String from, String content) {
		Message message = new Message();
		message.setTo(to);
		message.setFrom(from);
		message.setText(content);

		try {
			messageService.send(message);
			return true;
		} catch (NurigoMessageNotReceivedException exception) {
			log.error(exception.getFailedMessageList().toString());
			return false;
		} catch (Exception exception) {
			log.error(exception.getMessage());
			return false;
		}
	}
}
