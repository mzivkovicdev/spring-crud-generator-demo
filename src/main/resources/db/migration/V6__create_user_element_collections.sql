CREATE TABLE IF NOT EXISTS "user_table_roles" (
  "user_id" BIGINT NOT NULL,
  "roles" VARCHAR(255) NOT NULL
  , "order_index" INTEGER NOT NULL,
  CONSTRAINT fk_user_table_roles_user_id
    FOREIGN KEY ("user_id")
    REFERENCES "user_table" ("user_id")
);
CREATE TABLE IF NOT EXISTS "user_table_permissions" (
  "user_id" BIGINT NOT NULL,
  "permissions" VARCHAR(255) NOT NULL
  , "order_index" INTEGER NOT NULL,
  CONSTRAINT fk_user_table_permissions_user_id
    FOREIGN KEY ("user_id")
    REFERENCES "user_table" ("user_id")
);
