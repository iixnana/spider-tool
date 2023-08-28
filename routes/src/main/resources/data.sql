INSERT INTO users
values (1, 'Kamile', 'Nanartonyte', '$2a$10$YreknxafzD4sMh3LRv8jm.61JHSTS3VNOpApJnDHff7GMIF4gGXpS', 'kamile')
ON CONFLICT (id) DO NOTHING;
