INSERT INTO "Currency" ("guid", "code", "conversionRatio") SELECT '00000000-0000-0000-0000-000000000000', 'USD', '0.045' WHERE NOT EXISTS (     SELECT 1 FROM "Currency" WHERE "code" = 'USD' ); INSERT INTO "Currency" ("guid", "code", "conversionRatio") SELECT '00000000-0000-0000-0000-000000000001', 'CZK', '1' WHERE NOT EXISTS (     SELECT 1 FROM "Currency" WHERE "code" = 'CZK' ); INSERT INTO "Currency" ("guid", "code", "conversionRatio") SELECT '00000000-0000-0000-0000-000000000002', 'EUR', '0.041' WHERE NOT EXISTS (     SELECT 1 FROM "Currency" WHERE "code" = 'EUR' );
INSERT INTO "Category" ("guid", "name")
SELECT '00000000-0000-0000-0000-000000000000', 'Business trip'
    WHERE NOT EXISTS (
    SELECT 1 FROM "Category" WHERE "name" = 'Business trip'
);
INSERT INTO "Category" ("guid", "name")
SELECT '00000000-0000-0000-0000-000000000001', 'Personal trip'
    WHERE NOT EXISTS (
    SELECT 1 FROM "Category" WHERE "name" = 'Personal trip'
);
