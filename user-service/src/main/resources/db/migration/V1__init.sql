CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    display_name VARCHAR(255),
    headline VARCHAR(255),
    bio TEXT,
    location VARCHAR(255),
    website_url VARCHAR(1024),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_users_user ON users(user_id);
