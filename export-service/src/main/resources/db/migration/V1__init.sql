CREATE TABLE IF NOT EXISTS export (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    resume_id VARCHAR(64),
    format VARCHAR(16),
    status VARCHAR(32),
    file_url VARCHAR(1024),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_export_user ON export(user_id);
