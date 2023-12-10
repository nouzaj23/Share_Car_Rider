--  Currencies
INSERT INTO "Currency" ("guid", "code", "conversionRatio")
VALUES ('00000000-0000-0000-0000-000000000000', 'USD', '0.045'),
       ('00000000-0000-0000-0000-000000000001', 'CZK', '1'),
       ('00000000-0000-0000-0000-000000000002', 'EUR', '0.041')
;

--  Categories
INSERT INTO "Category" ("guid", "name")
VALUES ('00000000-0000-0000-0000-000000000000', 'Business trip'),
       ('00000000-0000-0000-0000-000000000001', 'Personal trip')
;