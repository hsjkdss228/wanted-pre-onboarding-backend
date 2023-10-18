CREATE TABLE IF NOT EXISTS companies(
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    region VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS job_openings(
    id VARCHAR(255) NOT NULL PRIMARY KEY,
    company_id VARCHAR(255) NOT NULL,
    position_name VARCHAR(255) NOT NULL,
    rewards BIGINT NOT NULL,
    description_body VARCHAR(255) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS job_opening_tech_stacks(
    job_opening_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (job_opening_id) REFERENCES job_openings(id)
);
