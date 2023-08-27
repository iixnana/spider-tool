INSERT INTO users
values (0, 'kamile')
ON CONFLICT (id) DO NOTHING;
