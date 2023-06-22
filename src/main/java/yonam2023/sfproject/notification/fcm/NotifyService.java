package yonam2023.sfproject.notification.fcm;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yonam2023.sfproject.employee.EmployeeManagerRepository;
import yonam2023.sfproject.employee.domain.DepartmentType;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Transactional
public class NotifyService {
    private final FCMService fcmService;
    private final EmployeeManagerRepository employeeManagerRepository;

    Logger logger = LoggerFactory.getLogger(NotifyService.class);
    public void sendNotification(final NotificationRequest request) {
        try {
            logger.info("Message Sended");
            fcmService.send(request);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    //특정 부서의 모든 직원에게 알림을 보냄
    public void departmentNotify(DepartmentType departmentType, String title, String message) {
        List<String> tokens = employeeManagerRepository.findTokensByDepartment(departmentType);
        if (tokens != null) {
            tokens.forEach(token -> {
//                System.out.println("[in notifyService] token: "+token);
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .title("[ALL]" + title)
                        .token(token)
                        .message(message)
                        .build();
                sendNotification(notificationRequest);
            });
        }
    }

    //    //단일 대상 (HttpSession 또는 userId를 받아서 처리해야 함.)
//    public void singleTargetNotify(Long userId, String title, String message) {
//            NotificationRequest notificationRequest = NotificationRequest.builder()
//                    .title("[단일]"+title)
//                    .token(notificationService.getToken(userId))
//                    .message(message)
//                    .build();
//            sendNotification(notificationRequest);
//    }
//
//    //다중 대상 (HttpSession 또는 userId를 받아서 처리해야 함.)
//    public void multiTargetNotify(List<Long> userIds, String title, String message) {
//        userIds.forEach(userId -> {
//                    NotificationRequest notificationRequest = NotificationRequest.builder()
//                            .title("[다중]"+title)
//                            .token(notificationService.getToken(userId))
//                            .message(message)
//                            .build();
//                    sendNotification(notificationRequest);
//                });
//    }

}