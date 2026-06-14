-- ==========================================
-- 电商平台数据库初始化脚本
-- 数据库: SQL Server
-- ==========================================

-- 创建数据库
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'ecommerce')
BEGIN
    CREATE DATABASE ecommerce;
END
GO

USE ecommerce;
GO

-- ==========================================
-- 1. 用户表
-- ==========================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
BEGIN
    CREATE TABLE users (
        user_id INT IDENTITY(1,1) PRIMARY KEY,
        username VARCHAR(50) NOT NULL UNIQUE,
        password VARCHAR(255) NOT NULL,
        phone VARCHAR(20)
    );
END
GO

-- ==========================================
-- 2. 商品表
-- ==========================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='products' AND xtype='U')
BEGIN
    CREATE TABLE products (
        product_id INT IDENTITY(1,1) PRIMARY KEY,
        name VARCHAR(100) NOT NULL,
        price DECIMAL(10,2) NOT NULL,
        stock INT NOT NULL DEFAULT 0
    );
END
GO

-- ==========================================
-- 3. 购物车表
-- ==========================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='cart' AND xtype='U')
BEGIN
    CREATE TABLE cart (
        cart_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        product_id INT NOT NULL,
        quantity INT NOT NULL DEFAULT 1,
        CONSTRAINT FK_cart_user FOREIGN KEY (user_id) REFERENCES users(user_id),
        CONSTRAINT FK_cart_product FOREIGN KEY (product_id) REFERENCES products(product_id)
    );
END
GO

-- ==========================================
-- 4. 订单表
-- ==========================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='orders' AND xtype='U')
BEGIN
    CREATE TABLE orders (
        order_id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
        status INT NOT NULL DEFAULT 0,
        create_time DATETIME NOT NULL DEFAULT GETDATE(),
        CONSTRAINT FK_order_user FOREIGN KEY (user_id) REFERENCES users(user_id)
    );
END
GO

-- ==========================================
-- 5. 订单明细表
-- ==========================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='order_items' AND xtype='U')
BEGIN
    CREATE TABLE order_items (
        item_id INT IDENTITY(1,1) PRIMARY KEY,
        order_id INT NOT NULL,
        product_id INT NOT NULL,
        quantity INT NOT NULL,
        price DECIMAL(10,2) NOT NULL,
        CONSTRAINT FK_item_order FOREIGN KEY (order_id) REFERENCES orders(order_id)
    );
END
GO

-- ==========================================
-- 测试数据：插入示例商品
-- ==========================================
INSERT INTO products (name, price, stock) VALUES
    ('iPhone 15', 6999.00, 100),
    ('MacBook Pro', 14999.00, 50),
    ('AirPods Pro', 1799.00, 200),
    ('iPad Air', 4999.00, 80),
    ('Apple Watch', 2999.00, 150);
GO