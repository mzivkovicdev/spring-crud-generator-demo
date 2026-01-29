CREATE TABLE IF NOT EXISTS user_table (
  user_id BIGINT DEFAULT nextval('user_table_id_seq'),
  username VARCHAR(255),
  email VARCHAR(255),
  password VARCHAR(255),
  details JSONB,
  version BIGINT DEFAULT 0 NOT NULL,
  product_id BIGINT,
  CONSTRAINT pk_user_table PRIMARY KEY (user_id)
);
