-- example user
--
-- credentials: example@user.de | 123456

INSERT INTO auth.users (instance_id, id, aud, role, email, encrypted_password, email_confirmed_at, invited_at,
                        confirmation_token, confirmation_sent_at, recovery_token, recovery_sent_at,
                        email_change_token_new, email_change, email_change_sent_at, last_sign_in_at, raw_app_meta_data,
                        raw_user_meta_data, is_super_admin, created_at, updated_at, phone, phone_confirmed_at,
                        phone_change, phone_change_token, phone_change_sent_at,
                        email_change_token_current, email_change_confirm_status, banned_until, reauthentication_token,
                        reauthentication_sent_at, is_sso_user, deleted_at, is_anonymous)
VALUES ('00000000-0000-0000-0000-000000000000', '596eedbb-6c17-47b6-9878-2959c11b6851', 'authenticated',
        'authenticated', 'example@user.de', crypt('123456', gen_salt('bf')),
        '2025-05-23 08:54:29.279736 +00:00', null, '', null, '', null, '', '', null,
        '2025-05-23 08:54:32.169756 +00:00', '{
    "provider": "email",
    "providers": [
      "email"
    ]
  }', '{
    "email_verified": true
  }', null, '2025-05-23 08:54:29.273771 +00:00', '2025-05-23 08:54:32.172622 +00:00', null, null, '', '', null,
        '', 0, null, '', null, false, null, false);

INSERT INTO auth.identities (provider_id, user_id, identity_data, provider, last_sign_in_at, created_at, updated_at, id)
VALUES ('596eedbb-6c17-47b6-9878-2959c11b6851', '596eedbb-6c17-47b6-9878-2959c11b6851', '{
  "sub": "596eedbb-6c17-47b6-9878-2959c11b6851",
  "email": "example@user.de",
  "email_verified": false,
  "phone_verified": false
}', 'email', '2025-05-23 08:54:29.277271 +00:00', '2025-05-23 08:54:29.277295 +00:00',
        '2025-05-23 08:54:29.277295 +00:00', '7b1f772f-09dc-4f34-a2c9-cad656704ff7');

