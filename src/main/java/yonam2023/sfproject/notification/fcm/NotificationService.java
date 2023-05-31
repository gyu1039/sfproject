package yonam2023.sfproject.notification.fcm;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final FCMService fcmService;

    private final ConcurrentHashMap<Long, String> tokenMap = new ConcurrentHashMap<>();

    public NotificationService(final FCMService fcmService) {
        this.fcmService = fcmService;
    }



    public void register(final Long userId, final String token) {
        tokenMap.put(userId, token);
    }
    public void register(final String token) {
        //user id를 랜덤 8자리 숫자로 생성.
        //모든 token에 알림을 보내기 위해 임시로 만든 메서드임. (모든 token을 사용하면 user id는 필요 없으므로)
        Random random = new Random();
        tokenMap.put(random.nextLong(), token);
    }

    public String getToken(final Long userId) {
        return tokenMap.get(userId);
    }
    public void deleteToken(final Long userId) {
        tokenMap.remove(userId);
    }

    public Collection<String> getAllTokens(){
        return tokenMap.values();
    }

    public void sendNotification(final NotificationRequest request) {
        try {
            fcmService.send(request);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}

