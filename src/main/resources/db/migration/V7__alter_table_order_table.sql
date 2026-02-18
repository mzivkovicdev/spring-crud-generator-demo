ALTER TABLE "order_table"
ADD CONSTRAINT fk_order_table_product_id
FOREIGN KEY ("product_id")
REFERENCES "product_table"("id");
