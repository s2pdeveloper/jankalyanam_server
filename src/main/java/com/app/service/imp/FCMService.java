package com.app.service.imp;

import java.time.Duration;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.app.dto.NotificationRequest;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Service
public class FCMService {
	 private Logger logger = LoggerFactory.getLogger(FCMService.class);

	 	@Value("${cloudinary.url}")
		private String filePath;

	    public void sendMessageToToken(NotificationRequest request)
	            throws InterruptedException, ExecutionException, FirebaseMessagingException {
	        MulticastMessage message = getPreconfiguredMessageBuilder(request);
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        String jsonOutput = gson.toJson(message);
	        BatchResponse response = sendAndGetResponse(message);
	        logger.info("Sent message to token. Device token: " + request.getTokens() + ", " + response+ " msg "+jsonOutput);
	    }

	    private BatchResponse sendAndGetResponse(MulticastMessage message) throws InterruptedException, ExecutionException, FirebaseMessagingException {
	        return FirebaseMessaging.getInstance().sendMulticast(message);
	    }


//	    private AndroidConfig getAndroidConfig(String topic) {
//	        return AndroidConfig.builder()
//	                .setTtl(Duration.ofMinutes(2).toMillis()).setCollapseKey(topic)
//	                .setPriority(AndroidConfig.Priority.HIGH)
//	                .setNotification(AndroidNotification.builder()
//	                        .setTag(topic).build()).build();
//	    }
//	    private ApnsConfig getApnsConfig(String topic) {
//	        return ApnsConfig.builder()
//	                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
//	    }
//	    private MulticastMessage getPreconfiguredMessageToToken(NotificationRequest request) {
//	        return getPreconfiguredMessageBuilder(request).setToken(request.getToken())
//	                .build();
//	    }

	    private MulticastMessage getPreconfiguredMessageBuilder(NotificationRequest request) {
//	        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
//	        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
	        Notification notification = Notification.builder()
	                                        .setTitle(request.getTitle())
	                                        .setBody(request.getBody())
	                                        .build();
	        return MulticastMessage.builder().setNotification(notification).addAllTokens(request.getTokens()).build();
	    }
}
