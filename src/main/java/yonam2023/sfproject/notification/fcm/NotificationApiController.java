package yonam2023.sfproject.notification.fcm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    //todo: 부서별로 요청을 처리할 수 있도록 path variable 사용
    @PostMapping("/register")
    public ResponseEntity register(@RequestBody String userToken) {
        System.out.println("userToken = " + userToken);
        notificationService.register(userToken);
        return ResponseEntity.ok().build();
    }

// 로그인 구현이 완료되면 UserSession으로 현재 로그인한 유저 정보가 들어오도록 구현. (인터셉터나 세션 따위를 사용)
//    @PostMapping("/register")
//    public ResponseEntity register(@RequestBody String userToken, UserSession userSession) {
//        notificationService.register(userSession.getId(), userToken);
//        return ResponseEntity.ok().build();
//    }

}
