ALTER TABLE users ADD COLUMN role VARCHAR(20) DEFAULT 'USER';
UPDATE users SET role = 'ADMIN' WHERE email = 'admin@fashionstore.com';
