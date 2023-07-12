ALTER TABLE subscription ADD created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now();

ALTER TABLE filter DROP CONSTRAINT FK_subscription;
ALTER TABLE subscription DROP CONSTRAINT subscription_pkey;

ALTER TABLE subscription RENAME COLUMN id TO user_id;

ALTER TABLE subscription ADD CONSTRAINT subscription_pkey PRIMARY KEY (user_id, subscription_type);
ALTER TABLE filter ADD CONSTRAINT FK_subscription FOREIGN KEY (subscription_id, subscription_type)
                                                    REFERENCES subscription(user_id, subscription_type)
                                                        ON DELETE CASCADE
                                                        ON UPDATE CASCADE;
