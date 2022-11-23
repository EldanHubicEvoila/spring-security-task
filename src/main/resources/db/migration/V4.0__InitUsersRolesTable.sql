CREATE TABLE users_roles (
    user_id INT NOT NULL,
    role_id INT NOT NULL,
    CONSTRAINT FK_user_id FOREIGN KEY(user_id) REFERENCES users(id),
    CONSTRAINT FK_role_id FOREIGN KEY(role_id) REFERENCES roles(id)
);
