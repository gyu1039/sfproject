package yonam2023.sfproject.notification.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotifyService {


    private final NotificationService notificationService;

    //단일 대상 (추정)
    public void singleTargetNotify(Long userId, String title, String message) {
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .title("[단일]"+title)
                    .token(notificationService.getToken(userId))
                    .message(message)
                    .build();
            notificationService.sendNotification(notificationRequest);
    }

    //다중 대상 (추정)
    public void multiTargetNotify(List<Long> userIds, String title, String message) {
        userIds.forEach(userId -> {
                    NotificationRequest notificationRequest = NotificationRequest.builder()
                            .title("[다중]"+title)
                            .token(notificationService.getToken(userId))
                            .message(message)
                            .build();
                    notificationService.sendNotification(notificationRequest);
                });
    }

    //모든 대상 (추정)
    public void allUserNotify(String title, String message) {

        notificationService.getAllTokens()
                .forEach(token -> {
                    //System.out.println("[in notifyService] token: "+token);
                    NotificationRequest notificationRequest = NotificationRequest.builder()
                            .title("[ALL]"+title)
                            .token(token)
                            .message(message)
                            .build();
                    notificationService.sendNotification(notificationRequest);
                });
    }

}