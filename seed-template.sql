INSERT INTO notification.template(
  id, body_html, body_text, is_active, subject, type, created_at
) VALUES (
  1, 
  '<!DOCTYPE html>
  <html>
  <head>
    <meta charset="UTF-8" />
    <title>Welcome to [YourApp]</title>
  </head>
  <body style="font-family: Arial, sans-serif; line-height: 1.6;">
    <h2>Welcome, {{firstName}}!</h2>
    <p>
      Thank you for registering with <strong>[YourApp]</strong>. We''re excited to have you on board.
    </p>
    <p>
      You can now log in and start exploring your dashboard. If you have any questions, feel free to reach out to our support team.
    </p>
    <p>— The [YourApp] Team</p>
  </body>
  </html>',
  'Welcome, {{firstName}}!\n\nThank you for registering with [YourApp]. We''re excited to have you on board.\n\nLog in to explore your dashboard.\n\n— The [YourApp] Team',
  true, 
  'Welcome to [YourApp], {{firstName}}', 
  'USER_REGISTERED',
  now()
);
INSERT INTO notification.template(
  id, body_html, body_text, is_active, subject, type, created_at
) VALUES (
  2, 
  '<!DOCTYPE html>
  <html>
    <head><meta charset="UTF-8" /></head>
    <body style="font-family: Arial, sans-serif; line-height: 1.6;">
      <h2>Order Confirmation - [YourApp]</h2>
      <p>Hi {{firstName}},</p>
      <p>Thank you for your order with <strong>[YourApp]</strong>!</p>
      <p><strong>Order Number:</strong> {{orderId}}</p>
      <p><strong>Order Date:</strong> {{orderDate}}</p>
      <p><strong>Items:</strong></p>
      <ul>
        {{items}}
      </ul>
      <p><strong>Total:</strong> ${{total}}</p>
      <p>We''re processing your order and will notify you once it ships.</p>
      <p>If you have any questions, reply to this email or contact our support team.</p>
      <p>Best regards,<br />The [YourApp] Team</p>
    </body>
  </html>',
  'Hi {{firstName}},

  Thank you for your order with [YourApp]!

  Order Number: {{orderId}}
  Order Date: {{orderDate}}

  Items:
  {{items}}

  Total: ${{total}}

  We''re processing your order and will notify you once it ships.

  If you have any questions, feel free to reply or contact our support team.

  Best regards,
  The [YourApp] Team',
  true,
  'Your Order #{{orderId}} is Confirmed!',
  'ORDER_CONFIRMED',
  now()
);

