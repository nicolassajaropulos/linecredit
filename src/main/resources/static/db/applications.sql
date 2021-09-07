CREATE TABLE linecredit.applications (
    id   INTEGER      NOT NULL AUTO_INCREMENT,
    founding_type VARCHAR(10) NOT NULL,
    recommended_credit DECIMAL(10,2) NOT NULL,
    requested_date DATETIME NOT NULL,
    PRIMARY KEY (id)
);