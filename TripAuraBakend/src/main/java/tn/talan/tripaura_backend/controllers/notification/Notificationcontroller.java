package tn.talan.tripaura_backend.controllers.notification;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.talan.tripaura_backend.entities.notification.NotificationRequest;
import tn.talan.tripaura_backend.entities.notification.NotificationResponse;
import tn.talan.tripaura_backend.services.notification.FCMService;

import java.util.concurrent.ExecutionException;

@RestController
@AllArgsConstructor
public class Notificationcontroller {
    @Autowired
    private FCMService fcmService;

    @PostMapping("/notification")
    public ResponseEntity sendNotification(@RequestBody NotificationRequest request)
            throws ExecutionException, InterruptedException {
        fcmService.sendMessage(request);
        return new ResponseEntity<>
                (new NotificationResponse(HttpStatus.OK.value(), "Notification has been sent."), HttpStatus.OK);
    }


}
