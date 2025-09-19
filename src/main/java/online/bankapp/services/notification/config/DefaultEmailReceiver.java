package online.bankapp.services.notification.config;

import bankapp.payload.notification.email.otp.EmailNotificationPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bankapp.services.notification.email.EmailService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultEmailReceiver {

    private final EmailService emailService;

    public void receive(EmailNotificationPayload payload) {
        log.debug("Msg received! Sending email to: {}", payload.recipientEmail());
        emailService.send(payload);
    }

}
