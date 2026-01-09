CREATE TABLE membership_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    plan_type VARCHAR(20) NOT NULL UNIQUE,
    max_accounts INTEGER,
    max_transactions_per_month INTEGER,
    has_advanced_reports BOOLEAN NOT NULL DEFAULT false,
    has_export_capabilities BOOLEAN NOT NULL DEFAULT false,
    has_historical_data BOOLEAN NOT NULL DEFAULT false,
    historical_data_years INTEGER,
    has_auto_categorization BOOLEAN NOT NULL DEFAULT false,
    has_custom_categories BOOLEAN NOT NULL DEFAULT false,
    has_multiple_users BOOLEAN NOT NULL DEFAULT false,
    has_api_access BOOLEAN NOT NULL DEFAULT false
);

CREATE INDEX idx_plan_type ON membership_plans(plan_type);

CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    membership_plan_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (membership_plan_id) REFERENCES membership_plans(id)
);

CREATE INDEX idx_user_email ON users(email);
CREATE INDEX idx_user_active ON users(active);

CREATE TABLE subscriptions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    membership_plan_id UUID NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (membership_plan_id) REFERENCES membership_plans(id)
);

CREATE INDEX idx_subscription_user_id ON subscriptions(user_id);
CREATE INDEX idx_subscription_status ON subscriptions(status);

CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    account_type VARCHAR(20) NOT NULL,
    initial_balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
    current_balance NUMERIC(19, 2) NOT NULL DEFAULT 0,
    currency VARCHAR(3) NOT NULL DEFAULT 'COP',
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_account_user_id ON accounts(user_id);
CREATE INDEX idx_account_active ON accounts(active);

CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50),
    type VARCHAR(20) NOT NULL,
    is_system BOOLEAN NOT NULL DEFAULT false,
    parent_category_id UUID,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_category_id) REFERENCES categories(id)
);

CREATE INDEX idx_category_user_id ON categories(user_id);
CREATE INDEX idx_category_system ON categories(is_system);
CREATE INDEX idx_category_type ON categories(type);

CREATE TABLE transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    account_id UUID NOT NULL,
    user_id UUID NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    transaction_type VARCHAR(20) NOT NULL,
    category_id UUID,
    description VARCHAR(500),
    transaction_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE INDEX idx_transaction_user_id ON transactions(user_id);
CREATE INDEX idx_transaction_account_id ON transactions(account_id);
CREATE INDEX idx_transaction_date ON transactions(transaction_date);
CREATE INDEX idx_transaction_user_date ON transactions(user_id, transaction_date);

CREATE TABLE transaction_tags (
    transaction_id UUID NOT NULL,
    tag VARCHAR(100) NOT NULL,
    PRIMARY KEY (transaction_id, tag),
    FOREIGN KEY (transaction_id) REFERENCES transactions(id) ON DELETE CASCADE
);

CREATE TABLE transaction_limits (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    year INTEGER NOT NULL,
    month INTEGER NOT NULL,
    transaction_count INTEGER NOT NULL DEFAULT 0,
    max_allowed INTEGER NOT NULL,
    reset_date TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE(user_id, year, month)
);

CREATE INDEX idx_limit_user_year_month ON transaction_limits(user_id, year, month);


