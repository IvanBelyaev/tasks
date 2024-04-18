CREATE TABLE IF NOT EXISTS task
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR,
    description VARCHAR,
    due_date    DATE,
    completed   BOOL
);