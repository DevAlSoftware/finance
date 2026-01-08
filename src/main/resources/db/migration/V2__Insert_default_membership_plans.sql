INSERT INTO membership_plans (plan_type, max_accounts, max_transactions_per_month, has_advanced_reports, 
                             has_export_capabilities, has_historical_data, historical_data_years, 
                             has_auto_categorization, has_custom_categories, has_multiple_users, has_api_access)
VALUES 
    ('FREE', 1, 50, false, false, true, 1, false, false, false, false),
    ('PREMIUM', NULL, NULL, true, true, true, NULL, true, true, false, false),
    ('BUSINESS', NULL, NULL, true, true, true, NULL, true, true, true, true);

