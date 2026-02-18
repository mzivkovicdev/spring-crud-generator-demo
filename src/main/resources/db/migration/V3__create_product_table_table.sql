CREATE TABLE IF NOT EXISTS "product_table" (
  "id" BIGINT,
  "name" VARCHAR(100) NOT NULL,
  "price" INTEGER NOT NULL,
  "uuid" UUID NOT NULL,
  "release_date" DATE,
  "details" JSONB,
  "status" VARCHAR(255),
  "version" BIGINT DEFAULT 0 NOT NULL,
  CONSTRAINT pk_product_table PRIMARY KEY (id),
  CONSTRAINT ck_product_table_status_enum CHECK (status IN ('ACTIVE', 'INACTIVE')),
  CONSTRAINT uk_product_table_name UNIQUE ("name")
);