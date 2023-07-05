CREATE TABLE subscription (
    id varchar(512) PRIMARY KEY,
    subscription_type varchar(255) NOT NULL,

    CONSTRAINT UC_subscription UNIQUE (id, subscription_type)
);

CREATE TABLE filter (
    id bigint PRIMARY KEY AUTO_INCREMENT,
    string varchar(255) NOT NULL,
    subscription_id varchar(512) NOT NULL,

    CONSTRAINT FK_subscription_id FOREIGN KEY (subscription_id) REFERENCES subscription(id)
                                                                ON DELETE CASCADE
                                                                ON UPDATE CASCADE
);

CREATE INDEX IX_filter_string ON filter (string);

CREATE INDEX IX_subscription_subscription_type ON subscription (subscription_type);