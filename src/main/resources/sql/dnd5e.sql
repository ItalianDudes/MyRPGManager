CREATE TABLE IF NOT EXISTS languages (
    id INTEGER NOT NULL PRIMARY KEY,
    name INTEGER NOT NULL UNIQUE,
    alphabet TEXT NOT NULL,
    lore TEXT
);

CREATE TABLE IF NOT EXISTS races (
    id INTEGER NOT NULL PRIMARY KEY,
    name INTEGER NOT NULL UNIQUE,
    medium_height REAL,
    medium_weight REAL,
    size TEXT,
    life_expectancy_years REAL,
    adulthood_age_year REAL,
    description TEXT,
    speed REAL,
    free_choosable_languages INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS race_traits (
    id INTEGER NOT NULL PRIMARY KEY,
    race_id INTEGER NOT NULL REFERENCES races(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE (race_id, name)
);

CREATE TABLE IF NOT EXISTS race_languages (
    id INTEGER NOT NULL PRIMARY KEY,
    race_id INTEGER NOT NULL REFERENCES races(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    language_id INTEGER NOT NULL REFERENCES races(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE (race_id, language_id)
);

CREATE TABLE IF NOT EXISTS classes (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    life_dice INTEGER NOT NULL,
    fixed_life_gain_leveling INTEGER,
    main_stats TEXT NOT NULL,
    description TEXT,
    armors_proficiency TEXT,
    weapons_proficiency TEXT,
    tools_proficiency TEXT,
    saving_throws TEXT NOT NULL,
    caster_stat TEXT,
    abilities TEXT,
    starting_equipment TEXT,
    starting_wealth_gold TEXT
);

CREATE TABLE IF NOT EXISTS class_table_rows (
    id INTEGER NOT NULL PRIMARY KEY,
    class_id INTEGER NOT NULL REFERENCES classes(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    level INTEGER NOT NULL CHECK (level > 0),
    proficiency_bonus INT NOT NULL,
    privileges TEXT,
    UNIQUE (class_id, level)
);

CREATE TABLE IF NOT EXISTS class_magic_table_rows (
    id INTEGER NOT NULL PRIMARY KEY,
    class_id INTEGER NOT NULL REFERENCES classes(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    level INTEGER NOT NULL CHECK (level > 0),
    spells_known INTEGER NOT NULL DEFAULT 0,
    cantrips_known INTEGER NOT NULL DEFAULT 0,
    first_spell_slots INTEGER NOT NULL DEFAULT 0,
    second_spell_slots INTEGER NOT NULL DEFAULT 0,
    third_spell_slots INTEGER NOT NULL DEFAULT 0,
    fourth_spell_slots INTEGER NOT NULL DEFAULT 0,
    fifth_spell_slots INTEGER NOT NULL DEFAULT 0,
    sixth_spell_slots INTEGER NOT NULL DEFAULT 0,
    seventh_spell_slots INTEGER NOT NULL DEFAULT 0,
    eighth_spell_slots INTEGER NOT NULL DEFAULT 0,
    ninth_spell_slots INTEGER NOT NULL DEFAULT 0,
    UNIQUE (class_id, level)
);

CREATE TABLE IF NOT EXISTS class_privileges (
    id INTEGER NOT NULL PRIMARY KEY,
    class_id INTEGER NOT NULL REFERENCES classes(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    name TEXT NOT NULL,
    description TEXT,
    UNIQUE (class_id, name)
);

CREATE TABLE IF NOT EXISTS backgrounds (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS spell_components (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    short_form TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS equipment_packs (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    rarity TEXT NOT NULL DEFAULT 0,
    cost_copper INTEGER NOT NULL DEFAULT 0,
    content TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id INTEGER NOT NULL PRIMARY KEY,
    base64image TEXT,
    name TEXT NOT NULL UNIQUE,
    cost_copper INTEGER NOT NULL DEFAULT 0,
    description TEXT,
    rarity INTEGER NOT NULL DEFAULT 0,
    weight REAL
);

CREATE TABLE IF NOT EXISTS item_tags (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL REFERENCES items(id),
    tag TEXT NOT NULL,
    UNIQUE (item_id, tag)
);

CREATE TABLE IF NOT EXISTS spell (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL UNIQUE REFERENCES items(id),
    min_level INTEGER DEFAULT 0,
    spell_type TEXT,
    cast_time TEXT,
    spell_range REAL,
    components TEXT,
    duration TEXT,
    damage TEXT
);

CREATE TABLE IF NOT EXISTS weapons (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL UNIQUE REFERENCES items(id),
    category TEXT NOT NULL,
    damage TEXT,
    properties TEXT
);

CREATE TABLE IF NOT EXISTS properties (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS armors (
    id INTEGER NOT NULL PRIMARY KEY,
    item_id INTEGER NOT NULL UNIQUE,
    category TEXT,
    ac TEXT,
    strength_required INTEGER,
    stealth TEXT
);

CREATE TABLE IF NOT EXISTS talents (
    id INTEGER NOT NULL PRIMARY KEY,
    name TEXT NOT NULL UNIQUE,
    requirements TEXT,
    description TEXT,
    privileges TEXT
);