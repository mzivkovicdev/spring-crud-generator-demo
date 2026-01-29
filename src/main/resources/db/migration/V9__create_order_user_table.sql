CREATE TABLE IF NOT EXISTS order_user_table (
  order_id BIGINT NOT NULL,
  user_id BIGINT NOT NULL,
  CONSTRAINT pk_order_user_table PRIMARY KEY (order_id, user_id),
  CONSTRAINT fk_order_user_table_order_id FOREIGN KEY (order_id) REFERENCES order_table(order_id),
  CONSTRAINT fk_order_user_table_user_id FOREIGN KEY (user_id) REFERENCES user_table(user_id)
);
