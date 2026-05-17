CREATE TABLE IF NOT EXISTS templates (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(64),
    preview_url VARCHAR(1024),
    html_template TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_templates_user ON templates(user_id);
