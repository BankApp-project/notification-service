package online.bankapp.services.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bankapp.services.notification.email.EmailService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Receiver {

    private final EmailService emailService;

    public void receiveWelcomeMessage(String email) {
        log.info("Msg received! Sending email to: {}", email);
       emailService.sendWelcomeEmail(email);
    }

}
