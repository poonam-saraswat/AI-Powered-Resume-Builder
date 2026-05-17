CREATE TABLE IF NOT EXISTS ai (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    resume_id VARCHAR(64),
    kind VARCHAR(32),
    prompt TEXT,
    response TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_ai_user ON ai(user_id);
