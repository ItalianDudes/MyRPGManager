CREATE TABLE IF NOT EXISTS items (
    id INTEGER NOT NULL PRIMARY KEY,
    base64image TEXT,
    image_extension TEXT CHECK (image_extension = 'png' OR image_extension = 'jpg' OR image_extension = 'jpeg'),
    name TEXT NOT NULL UNIQUE,
    cost_copper INTEGER NOT NULL DEFAULT 0,
    description TEXT,
    rarity INTEGER NOT NULL DEFAULT 0,
    weight REAL NOT NULL DEFAULT 0,
    category INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS equipments (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    type INTEGER NOT NULL,
    life_effect INTEGER NOT NULL DEFAULT 0,
    life_percentage_effect REAL NOT NULL DEFAULT 0,
    load_effect INTEGER NOT NULL DEFAULT 0,
    load_percentage_effect REAL NOT NULL DEFAULT 0,
    other_effects TEXT
);