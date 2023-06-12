package yonam2023.sfproject.notification.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class FCMInitializer {

    private static final Logger logger = LoggerFactory.getLogger(FCMInitializer.class);
    // .json 파일 위치
    // 루트는 resources 폴더이므로 resources/aa.json일 경우 "aa.json"만 적으면 됨.
    // json 파일에는 type,project_id,private_key_id,private_key,client_email,client_id,auth_uri,token_uri,auth_provider_x509_cert_url,client_x509_cert_url 따위가 들어있음.
    private static final String FIREBASE_CONFIG_PATH = "temp-webpush-firebase-adminsdk-z5v8e-af93b4aa58.json";

    @PostConstruct
    public void initialize() {
        try {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(FIREBASE_CONFIG_PATH).getInputStream())).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application has been initialized");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

}

