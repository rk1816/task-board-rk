-- Create task_lists table
CREATE TABLE IF NOT EXISTS task_lists (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create tasks table with foreign key
CREATE TABLE IF NOT EXISTS tasks (
    id SERIAL PRIMARY KEY,
    list_id INTEGER NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    FOREIGN KEY (list_id) REFERENCES task_lists(id) ON DELETE CASCADE
);

-- Optional: Insert initial data
INSERT INTO task_lists (name) VALUES
    ('Personal Tasks'),
    ('Work Tasks');

INSERT INTO tasks (list_id, name, description) VALUES
    (1, 'Exercise', 'Go for a 30-minute run'),
    (2, 'Finish Report', 'Complete Q1 sales report');