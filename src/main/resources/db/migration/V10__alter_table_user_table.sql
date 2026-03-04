ALTER TABLE "user_table"
  ADD COLUMN "deleted" BOOLEAN DEFAULT FALSE NOT NULL;
CREATE INDEX ix_user_table_deleted ON "user_table" ("deleted");
