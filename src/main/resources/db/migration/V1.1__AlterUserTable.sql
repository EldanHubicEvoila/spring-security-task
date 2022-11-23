ALTER TABLE users
    ADD CONSTRAINT unique_username_email UNIQUE(username, email);