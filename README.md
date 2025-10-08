# BankApp Notification Service

A microservice that consumes notification events from the BankApp auth service and delivers emails via supported providers (currently Resend).

## Overview

This service acts as a consumer in BankApp's event-driven notification architecture:

- **Consumes** notification messages from RabbitMQ
- **Delivers** emails via email service providers
- **Handles** queue management and error handling

## Quick Start

### Prerequisites

- Docker and Docker Compose
- A running RabbitMQ instance (from auth-service stack)
- [Resend.com](https://resend.com) account with verified domain

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/BankApp-project/notification-service.git
   cd notification-service
   ```

2. **Configure environment variables**
   ```bash
   cp .env.example .env
   ```

3. **Edit `.env` and add your credentials:**
   ```bash
   # Required: Add your Resend API key
   RESEND_API_KEY=re_YOUR_API_KEY_HERE
   
   # Required: Add your verified sender email
   SENDER_EMAIL_ADDRESS=noreply@yourdomain.com
   
   # Optional: Configure RabbitMQ connection if not using defaults
   RABBIT_MQ_HOST=rabbitmq
   RABBIT_MQ_USER=guest
   RABBIT_MQ_PASSWORD=guest
   ```

4. **Start the service**
   ```bash
   docker compose up -d
   ```

5. **Verify it's running**
   ```bash
   docker compose logs -f bankapp-notification-service
   ```

## Configuration

### Environment Variables

| Variable                     | Required | Default              | Description                           |
|------------------------------|----------|----------------------|---------------------------------------|
| `RESEND_API_KEY`             | Yes      | -                    | Your Resend.com API key               |
| `SENDER_EMAIL_ADDRESS`       | Yes      | -                    | Verified sender email address         |
| `RABBIT_MQ_HOST`             | No       | `localhost`          | RabbitMQ server hostname              |
| `RABBIT_MQ_PORT`             | No       | `5672`               | RabbitMQ server port                  |
| `RABBIT_MQ_USER`             | No       | `guest`              | RabbitMQ username                     |
| `RABBIT_MQ_PASSWORD`         | No       | `guest`              | RabbitMQ password                     |
| `APP_SEND_TEST_EMAIL`        | No       | `false`              | Send test email on startup            |
| `APP_TEST_EMAIL_ADDRESS`     | No       | -                    | Recipient for test email              |
| `LOGGING_LEVEL_BANKAPP`      | No       | `DEBUG`              | Application logging level             |

### Setting Up Resend

1. Sign up at [Resend.com](https://resend.com)
2. Verify your domain following [Resend's domain verification guide](https://resend.com/docs/dashboard/domains/introduction)
3. Generate an API key from the Resend dashboard
4. Use an email address that matches your verified domain (e.g., `noreply@yourdomain.com`)

## Integration with Auth Service

This service is designed to work with BankApp's auth service. Ensure:

1. **Network connectivity**: The notification service must be on the same Docker network as RabbitMQ
2. **Exchange configuration**: Default exchange is `notifications.commands.v1.exchange` with routing key `send.otp.email`
3. **Auth service is configured**: See the [auth service notification integration docs](https://github.com/BankApp-project/auth/wiki/Notification-Integration)

### Docker Network Setup

The notification service connects to the auth service's Docker network (`auth_bankapp-auth`):

```bash
# The network should already exist from the auth-service deployment
# If it doesn't exist, start the auth service first:
cd /path/to/auth-service
docker compose up -d
```

**Note**: The compose.yml uses the external network `auth_bankapp-auth` created by the auth service. Ensure the auth service is running before starting the notification service.

## Testing

### Test from Auth Service

1. Start the notification service
2. Access the auth service Swagger UI at `http://localhost:8080/api/`
3. Trigger an OTP email via the `/verification/complete/email` endpoint
4. Check your inbox for the notification email

### Send Test Email on Startup

Enable the test email feature in `.env`:

```bash
APP_SEND_TEST_EMAIL=true
APP_TEST_EMAIL_ADDRESS=yourtest@example.com
```

A test email will be sent when the service starts successfully.

## Troubleshooting

### Service won't start

- **Check RabbitMQ connection**: Ensure RabbitMQ is running and accessible
- **Verify network**: Confirm the service is on the correct Docker network
- **Check logs**: `docker compose logs bankapp-notification-service`

### Emails not being delivered

- **Verify Resend API key**: Ensure your API key is valid and active
- **Check sender domain**: Sender email must match a verified domain in Resend
- **Review logs**: Look for RabbitMQ connection errors or email delivery failures
- **Confirm queue binding**: Check RabbitMQ management UI to ensure queues are properly bound

### RabbitMQ connection issues

- **Check credentials**: Verify `RABBIT_MQ_USER` and `RABBIT_MQ_PASSWORD`
- **Verify hostname**: Ensure `RABBIT_MQ_HOST` points to your RabbitMQ instance
- **Network connectivity**: Confirm both services are on the same Docker network

## Message Contract

The service consumes `EmailNotificationPayload` messages with the following structure:

```json
{
  "recipientEmail": "user@example.com",
  "subject": "Your OTP Code",
  "htmlBody": "<html>...</html>"
}
```

For full message contract specifications, see the [auth service documentation](https://github.com/BankApp-project/auth/wiki/Notification-Integration).


## Development

### Local Development

```bash
# Run with local RabbitMQ
docker compose -f compose.yml up

# Or run outside Docker
./mvnw spring-boot:run
```

### Building

```bash
# Build Docker image
docker build -t bankapp-notification-service .

# Or use Maven
./mvnw clean package
```

## Production Deployment

For production environments:

1. **Use strong RabbitMQ credentials** (not guest/guest)
2. **Enable TLS/SSL** for RabbitMQ connections
3. **Configure proper logging** (set `LOGGING_LEVEL_BANKAPP` to `INFO` or `WARN`)
4. **Set up monitoring** for queue depth and message processing rates
5. **Implement dead letter queues** for failed message handling
6. **Use environment-specific configurations** (separate `.env` files)

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is under MIT license.

## Support

- **Issues**: [GitHub Issues](https://github.com/BankApp-project/notification-service/issues)
- **Auth Service Integration**: [Auth Service Notification Docs](https://github.com/BankApp-project/auth/wiki/Notification-Integration)
