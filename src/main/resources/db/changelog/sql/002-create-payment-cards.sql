-- changeset evgeny:002
CREATE TABLE payment_cards (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    number VARCHAR(20) NOT NULL,
    holder VARCHAR(255),
    expiration_date DATE,
    active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    CONSTRAINT fk_payment_cards_user FOREIGN KEY(user_id) REFERENCES users(id)
);


CREATE INDEX idx_payment_cards_user_id ON payment_cards(user_id);