package online.bankapp.services.notification.email.template;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds variables for email template personalization.
 * Uses builder pattern for convenient variable setting.
 */
public class TemplateVariables {
    private final Map<String, String> variables;

    private TemplateVariables(Builder builder) {
        this.variables = builder.variables;
    }

    /**
     * Gets the value of a variable.
     * @param key variable name
     * @return variable value or null if not found
     */
    public String get(String key) {
        return variables.get(key);
    }

    /**
     * Builder for TemplateVariables.
     */
    public static class Builder {
        private final Map<String, String> variables = new HashMap<>();

        /**
         * Adds a variable.
         * @param key variable name
         * @param value variable value
         * @return builder instance
         */
        public Builder with(String key, String value) {
            variables.put(key, value);
            return this;
        }

        /**
         * Common variable: Username
         */
        public Builder withUsername(String username) {
            return with("username", username);
        }

        /**
         * Builds the TemplateVariables instance.
         * @return new TemplateVariables instance
         */
        public TemplateVariables build() {
            return new TemplateVariables(this);
        }
    }

    /**
     * Creates a new builder instance.
     * @return new Builder instance
     */
    public static Builder builder() {
        return new Builder();
    }
}