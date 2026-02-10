ALTER TABLE user_table
ADD CONSTRAINT fk_user_table_product_id
FOREIGN KEY (product_id)
REFERENCES product_table(id);
