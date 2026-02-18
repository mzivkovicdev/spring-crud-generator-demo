CREATE TABLE IF NOT EXISTS "product_table_id_gen" (
    "gen_name" VARCHAR(255) PRIMARY KEY,
    "gen_value" BIGINT
);

INSERT INTO "product_table_id_gen"("gen_name", "gen_value")
    VALUES ('product_table', 1)
    ON CONFLICT DO NOTHING;
