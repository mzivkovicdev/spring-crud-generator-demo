CREATE TABLE IF NOT EXISTS product_table (
  id BIGINT,
  name VARCHAR(10000) NOT NULL UNIQUE,
  price VARCHAR(255) NOT NULL,
  uuid UUID NOT NULL,
  birth_date DATE,
  status VARCHAR(255),
  version BIGINT DEFAULT 0 NOT NULL,
  CONSTRAINT pk_product_table PRIMARY KEY (id) ,
  CONSTRAINT ck_product_table_status_enum CHECK (status IN ('ACTIVE', 'INACTIVE'))
);
