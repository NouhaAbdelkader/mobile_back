package tn.talan.tripaura_backend.services.notification;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.jsonwebtoken.io.IOException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.InputStream;

@Service

public class FCMInitializer implements INotificationTripaura  {

    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;

    private static final Logger logger = LoggerFactory.getLogger(FCMInitializer.class);

    @PostConstruct

    public void initialize() {

        try {
            InputStream firebaseFilePath = new FileInputStream(firebaseConfigPath);
            GoogleCredentials firebaseCredentials = GoogleCredentials.fromStream(firebaseFilePath);
            FirebaseOptions options = new FirebaseOptions
                    .Builder()
                    .setCredentials(firebaseCredentials)
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                logger.info("Firebase application initialized");
            }
        } catch (IOException | java.io.IOException e) {
            logger.error(e.getMessage());
        }

    }
}
