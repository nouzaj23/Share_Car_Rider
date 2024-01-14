CREATE TABLE IF NOT EXISTS "Category"
(
    `id`        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`      VARCHAR     NOT NULL UNIQUE,
    `name`      VARCHAR(150) NOT NULL
);


CREATE TABLE IF NOT EXISTS "Currency"
(
    `id`                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`              VARCHAR     NOT NULL UNIQUE,
    `code`              VARCHAR(5) NOT NULL,
    `conversionRatio`   NUMERIC(10, 6)   NOT NULL
);


CREATE TABLE IF NOT EXISTS "Ride"
(
    `id`                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`              VARCHAR     NOT NULL UNIQUE,
    `name`              VARCHAR(150) NOT NULL,
    `passengers`        INT   NOT NULL,
    `currencyId`        BIGINT REFERENCES "Currency"(`id`),
    `fuelExpenses`      NUMERIC   NOT NULL,
    `categoryId`        BIGINT REFERENCES "Category"(`id`),
    `from`              VARCHAR(150) NOT NULL,
    `to`                VARCHAR(150) NOT NULL,
    `distance`          INT   NOT NULL,
    `date`              DATE    NOT NULL
);

CREATE TABLE IF NOT EXISTS "Template"
(
    `id`                BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    `guid`              VARCHAR     NOT NULL UNIQUE,
    `name`              VARCHAR(150) NOT NULL,
    `passengers`        INT   NOT NULL,
    `currencyId`        BIGINT REFERENCES "Currency"(`id`),
    `categoryId`        BIGINT REFERENCES "Category"(`id`),
    `from`              VARCHAR(150) NOT NULL,
    `to`                VARCHAR(150) NOT NULL,
    `distance`          INT   NOT NULL
);
