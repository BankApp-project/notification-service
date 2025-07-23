package online.bankapp.services.notification.email.template;


import online.bankapp.services.notification.email.EmailContent;

/**
 * Interface for email template management.
 */
public interface EmailTemplateProvider {
    /**
     * Gets welcome email template.
     * @param username user's username for personalization
     * @return email content and subject
     */
    EmailContent getWelcomeEmail(String username);
}