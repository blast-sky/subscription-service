ALTER TABLE filter DROP CONSTRAINT FK_subscription_id;

ALTER TABLE subscription DROP CONSTRAINT UC_subscription;

ALTER TABLE subscription DROP CONSTRAINT subscription_pkey;

ALTER TABLE subscription ADD CONSTRAINT subscription_pkey PRIMARY KEY (id, subscription_type);


ALTER TABLE filter ADD subscription_type varchar(255) NOT NULL;

ALTER TABLE filter ADD CONSTRAINT FK_subscription FOREIGN KEY (subscription_id, subscription_type)
                                                    REFERENCES subscription(id, subscription_type)
                                                        ON DELETE CASCADE
                                                        ON UPDATE CASCADE