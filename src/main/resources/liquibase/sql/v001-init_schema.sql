CREATE TABLE subscription (
    id bigint PRIMARY KEY,
    subscription_type_id bigint NOT NULL,

    CONSTRAINT FK_subscription_type_id FOREIGN KEY (subscription_type_id) REFERENCES subscription_type(id),
    CONSTRAINT UC_subscription UNIQUE (id, subscription_type_id)
);

CREATE TABLE filter (
    id bigint PRIMARY KEY,
    string varchar(255) NOT NULL,
    subscription_id bigint NOT NULL,

    CONSTRAINT FK_subscription_id FOREIGN KEY (subscription_id) REFERENCES subscription(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE subscription_type (
    id bigint PRIMARY KEY,
    type varchar(255) NOT NULL UNIQUE
);

CREATE INDEX IX_filter_string ON filter (string);

CREATE INDEX IX_subscription_subscription_type_id ON subscription (subscription_type_id);