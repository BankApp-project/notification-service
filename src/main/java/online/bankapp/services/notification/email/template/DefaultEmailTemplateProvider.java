package online.bankapp.services.notification.email.template;

import lombok.RequiredArgsConstructor;
import online.bankapp.services.notification.email.EmailContent;
import online.bankapp.services.notification.email.template.templates.WelcomeEmailTemplate;
import org.springframework.stereotype.Component;

/**
 * Default implementation of EmailTemplateProvider using specific template classes.
 */
@Component
@RequiredArgsConstructor
public class DefaultEmailTemplateProvider implements EmailTemplateProvider {
    
    private final WelcomeEmailTemplate welcomeTemplate;

    @Override
    public EmailContent getWelcomeEmail(String username) {
        TemplateVariables variables = TemplateVariables.builder()
            .withUsername(username)
            .build();

        return new EmailContent(
            welcomeTemplate.getSubject(),
            welcomeTemplate.generateEmail(variables)
        );
    }
}