CREATE TABLE IF NOT EXISTS billing (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    plan VARCHAR(32),
    status VARCHAR(32),
    ai_quota_used INTEGER DEFAULT 0,
    ai_quota_limit INTEGER DEFAULT 100,
    stripe_customer_id VARCHAR(128),
    stripe_subscription_id VARCHAR(128),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_billing_user ON billing(user_id);
