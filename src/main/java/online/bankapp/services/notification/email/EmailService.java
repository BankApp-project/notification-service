package online.bankapp.services.notification.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.bankapp.services.notification.email.exception.EmailSendingException;
import online.bankapp.services.notification.email.template.EmailTemplateProvider;
import org.springframework.stereotype.Service;

/**
 * Service for sending various types of emails.
 * Uses EmailSender for delivery and EmailTemplateProvider for content generation.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailSender emailSender;
    private final EmailTemplateProvider templateProvider;

    /**
     * Sends welcome email to new user.
     * @param email recipient's email address
     */
    public void sendWelcomeEmail(String email) {
        String username = getUsernameFromEmail(email);
        try {
            EmailContent content = templateProvider.getWelcomeEmail(username);
            emailSender.send(email, content.subject(), content.htmlContent());
        } catch (RuntimeException e) {
            log.error("Error sending welcome email to {}", email, e);
            throw new EmailSendingException("Error sending welcome email", e);
        }
    }

    private String getUsernameFromEmail(String email) {
        int indexOfAt = email.indexOf("@");
        return email.substring(0, indexOfAt);
    }
}