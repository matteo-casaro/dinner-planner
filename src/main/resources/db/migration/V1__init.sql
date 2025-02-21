
-- Tables
CREATE TABLE users (
    id UUID PRIMARY KEY,
    email VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    temp_token VARCHAR,
    temp_token_exp TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ingredients (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR NOT NULL,
    type VARCHAR NOT NULL,
    unit VARCHAR,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE recipes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    name VARCHAR NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE steps (
    id UUID PRIMARY KEY,
    recipe_id UUID NOT NULL,
    order_number INTEGER NOT NULL,
    title VARCHAR NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    UNIQUE (recipe_id, order_number)
);

CREATE TABLE step_ingredients (
    id UUID PRIMARY KEY,
    step_id UUID NOT NULL,
    ingredient_id UUID NOT NULL,
    quantity DECIMAL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (step_id) REFERENCES steps (id) ON DELETE CASCADE,
    FOREIGN KEY (ingredient_id) REFERENCES ingredients (id),
    UNIQUE (step_id, ingredient_id)
);

CREATE TABLE meal_schedule (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    recipe_id UUID NOT NULL,
    selected_day VARCHAR NOT NULL,
    meal_type VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (recipe_id) REFERENCES recipes (id) ON DELETE CASCADE,
    UNIQUE (user_id, selected_day, meal_type)
);