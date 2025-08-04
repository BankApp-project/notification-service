package online.bankapp.services.notification.email.template.templates;

import online.bankapp.services.notification.email.template.EmailTemplate;
import online.bankapp.services.notification.email.template.TemplateVariables;
import org.springframework.stereotype.Component;

/**
 * Welcome email template sent to new users.
 * Contains personalized greeting and next steps information.
 */
@Component
public class WelcomeEmailTemplate extends EmailTemplate {

    @Override
    public String getSubject() {
        return "Welcome to BankApp!";
    }

    @Override
    protected String getContent(TemplateVariables variables) {
        return String.format("""
            <h1 class="header">Welcome to BankApp! 🎉</h1>
            
            <p>Dear %s,</p>
            
            <p>We're excited to have you join BankApp! Your username is <strong>%s</strong>. We're here to help you manage your finances effectively.</p>
            
            <h2 class="header" style="font-size: 1.5em;">What's next?</h2>
            <ul>
                <li>Log in to your account</li>
                <li>Check your ballance - we got something nice for you</li>
                <li>Set up your profile</li>
            </ul>

            <p>Our customer support is available 24/7 if you need any assistance.</p>
            
            <div class="info-box">
                <p style="margin: 0;"><strong>Security Tip:</strong> Never share your password with anyone.</p>
            </div>
            
            <p style="margin-top: 30px;">Best regards,<br>The BankApp Team</p>
            """,
            variables.get("userName"),
            variables.get("username")
        );
    }
}