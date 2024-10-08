CREATE TABLE categories (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(255) NOT NULL
);

CREATE TABLE problems (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    difficulty VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE problem_category (
    problem_id INTEGER NOT NULL REFERENCES problems(id),
    category_id INTEGER NOT NULL REFERENCES categories(id),
    PRIMARY KEY (problem_id, category_id)
);

CREATE TABLE solutions (
	id SERIAL PRIMARY KEY,
	solution_code TEXT NOT NULL,
    problem_id INTEGER NOT NULL REFERENCES problems(id),
	status VARCHAR(50) NOT NULL,
	submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at TIMESTAMP
);

CREATE TABLE evaluations (
    id SERIAL PRIMARY KEY,
    solution_id INTEGER NOT NULL,
    is_success BOOLEAN NOT NULL,
    evaluated_at TIMESTAMP NOT NULL
);

INSERT INTO categories (category_name) VALUES
('ARRAYS'),
('STRINGS'),
('LINKED_LISTS'),
('STACKS'),
('QUEUES'),
('HASH_TABLES'),
('HEAPS'),
('TREES'),
('GRAPHS'),
('SORTING'),
('SEARCHING'),
('DYNAMIC_PROGRAMMING'),
('GREEDY_ALGORITHMS'),
('DIVIDE_AND_CONQUER'),
('BACKTRACKING'),
('BIT_MANIPULATION'),
('MATH'),
('GEOMETRY'),
('RECURSION'),
('SLIDING_WINDOW'),
('TWO_POINTERS'),
('BINARY_SEARCH'),
('INTERVALS'),
('UNION_FIND'),
('TOPOLOGICAL_SORT'),
('TRIE'),
('DFS'),
('BFS'),
('MATRIX'),
('PERMUTATIONS'),
('COMBINATIONS'),
('SUBSETS'),
('PALINDROME'),
('MERGE_INTERVALS');

