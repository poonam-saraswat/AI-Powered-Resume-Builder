CREATE TABLE IF NOT EXISTS jobs (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(255),
    company VARCHAR(255),
    location VARCHAR(255),
    description TEXT,
    match_score INTEGER,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_jobs_user ON jobs(user_id);
