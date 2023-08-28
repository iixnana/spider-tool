INSERT INTO users
values (1, 'Kamile', 'Nanartonyte', 'the-password', 'kamile')
ON CONFLICT (id) DO NOTHING;
