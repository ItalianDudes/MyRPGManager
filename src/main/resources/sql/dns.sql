-- TABLES DECLARATION
CREATE TABLE IF NOT EXISTS races (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    size INTEGER NOT NULL DEFAULT 0,
    age INTEGER NOT NULL,
    languages TEXT NOT NULL,
    traits_json TEXT,
    flaws_json TEXT,
    subraces_json TEXT
);

CREATE TABLE IF NOT EXISTS classes (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    description TEXT,
    starting_equipment_json TEXT,
    starting_spells_json TEXT,
    abilities_json TEXT NOT NULL,
    level INTEGER NOT NULL,
    vitality INTEGER NOT NULL,
    mind INTEGER NOT NULL,
    endurance INTEGER NOT NULL,
    strength INTEGER NOT NULL,
    dexterity INTEGER NOT NULL,
    charisma INTEGER NOT NULL,
    intelligence INTEGER NOT NULL,
    wisdom INTEGER NOT NULL,
    faith INTEGER NOT NULL,
    luck INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS elements (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    category INTEGER NOT NULL,
    rarity INTEGER NOT NULL,
    imageBase64 TEXT,
    imageExtension TEXT,
    costCopper INTEGER,
    weight INTEGER,
    description TEXT,
    quantity INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS items (
    id INTEGER NOT NULL PRIMARY KEY,
    element_id INTEGER NOT NULL REFERENCES elements(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    type INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS common_items (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS materials (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS key_items (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS consumables (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    is_unlimited INTEGER NOT NULL DEFAULT 0,
    scaling_strength INTEGER NOT NULL DEFAULT 0,
    scaling_dexterity INTEGER NOT NULL DEFAULT 0,
    scaling_intelligence INTEGER NOT NULL DEFAULT 0,
    scaling_faith INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS ammunitions (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    type INTEGER NOT NULL DEFAULT 0,
    atk_physical INTEGER NOT NULL DEFAULT 0,
    atk_magic INTEGER NOT NULL DEFAULT 0,
    atk_fire INTEGER NOT NULL DEFAULT 0,
    atk_lightning INTEGER NOT NULL DEFAULT 0,
    atk_dark INTEGER NOT NULL DEFAULT 0
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
    energy_effect INTEGER NOT NULL DEFAULT 0,
    energy_percentage_effect REAL NOT NULL DEFAULT 0,
    mana_effect INTEGER NOT NULL DEFAULT 0,
    mana_percentage_effect REAL NOT NULL DEFAULT 0,
    poise_effect INTEGER NOT NULL DEFAULT 0,
    other_effects TEXT
);

CREATE TABLE IF NOT EXISTS armors (
    id INTEGER NOT NULL PRIMARY KEY,
    equipment_id INTEGER NOT NULL REFERENCES equipments(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    slot INTEGER NOT NULL,
    weight_category INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS shields (
    id INTEGER NOT NULL PRIMARY KEY,
    equipment_id INTEGER NOT NULL REFERENCES equipments(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS addons (
    id INTEGER NOT NULL PRIMARY KEY,
    equipment_id INTEGER NOT NULL UNIQUE REFERENCES equipments(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE,
    slot INTEGER NOT NULL
);

CREATE TABLE IF NOT EXISTS weapons (
    id INTEGER NOT NULL PRIMARY KEY,
    equipment_id INTEGER NOT NULL REFERENCES equipments(id)
    ON UPDATE CASCADE
    ON DELETE CASCADE
);