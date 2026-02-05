-- Hotel Vending System Database Schema

CREATE DATABASE IF NOT EXISTS hotel_vending DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE hotel_vending;

-- User Table
CREATE TABLE `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'User ID',
    `username` VARCHAR(50) NOT NULL COMMENT 'Username',
    `password` VARCHAR(100) NOT NULL COMMENT 'Password (encrypted)',
    `nickname` VARCHAR(50) COMMENT 'Nickname',
    `phone` VARCHAR(20) COMMENT 'Phone number',
    `email` VARCHAR(100) COMMENT 'Email',
    `avatar` VARCHAR(255) COMMENT 'Avatar URL',
    `role` VARCHAR(20) NOT NULL DEFAULT 'customer' COMMENT 'Role: customer, employee, admin',
    `employee_discount` DECIMAL(3,2) DEFAULT 1.00 COMMENT 'Employee discount rate (0.80 = 20% off)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0=disabled, 1=enabled',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete: 0=active, 1=deleted',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User table';

-- Category Table
CREATE TABLE `category` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
    `name` VARCHAR(50) NOT NULL COMMENT 'Category name',
    `parent_id` BIGINT DEFAULT 0 COMMENT 'Parent category ID',
    `sort` INT DEFAULT 0 COMMENT 'Sort order',
    `icon` VARCHAR(255) COMMENT 'Icon URL',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0=disabled, 1=enabled',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product category table';

-- Product Table
CREATE TABLE `product` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Product ID',
    `category_id` BIGINT NOT NULL COMMENT 'Category ID',
    `name` VARCHAR(100) NOT NULL COMMENT 'Product name',
    `description` TEXT COMMENT 'Product description',
    `image` VARCHAR(500) COMMENT 'Product images (comma separated)',
    `price` DECIMAL(10,2) NOT NULL COMMENT 'Original price',
    `employee_price` DECIMAL(10,2) COMMENT 'Employee special price (null = use discount)',
    `stock` INT NOT NULL DEFAULT 0 COMMENT 'Stock quantity',
    `sales` INT NOT NULL DEFAULT 0 COMMENT 'Sales count',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0=off shelf, 1=on shelf',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Product table';

-- Coupon Table
CREATE TABLE `coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Coupon ID',
    `name` VARCHAR(100) NOT NULL COMMENT 'Coupon name',
    `type` VARCHAR(20) NOT NULL COMMENT 'Type: full_reduction, discount, fixed',
    `condition_amount` DECIMAL(10,2) DEFAULT 0 COMMENT 'Minimum order amount',
    `discount_amount` DECIMAL(10,2) COMMENT 'Discount amount (for full_reduction and fixed)',
    `discount_rate` DECIMAL(3,2) COMMENT 'Discount rate (for discount type, 0.80 = 20% off)',
    `total_count` INT NOT NULL COMMENT 'Total quantity',
    `received_count` INT NOT NULL DEFAULT 0 COMMENT 'Received quantity',
    `start_time` DATETIME NOT NULL COMMENT 'Start time',
    `end_time` DATETIME NOT NULL COMMENT 'End time',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT 'Status: 0=disabled, 1=enabled',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Coupon table';

-- User Coupon Table
CREATE TABLE `user_coupon` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'User coupon ID',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `coupon_id` BIGINT NOT NULL COMMENT 'Coupon ID',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Status: 0=unused, 1=used, 2=expired',
    `use_time` DATETIME COMMENT 'Use time',
    `order_id` BIGINT COMMENT 'Order ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_coupon_id` (`coupon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='User coupon table';

-- Shopping Cart Table
CREATE TABLE `cart` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Cart ID',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `product_id` BIGINT NOT NULL COMMENT 'Product ID',
    `quantity` INT NOT NULL DEFAULT 1 COMMENT 'Quantity',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Shopping cart table';

-- Order Table
CREATE TABLE `order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Order ID',
    `order_no` VARCHAR(50) NOT NULL COMMENT 'Order number',
    `user_id` BIGINT NOT NULL COMMENT 'User ID',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT 'Total amount',
    `discount_amount` DECIMAL(10,2) DEFAULT 0 COMMENT 'Discount amount',
    `coupon_amount` DECIMAL(10,2) DEFAULT 0 COMMENT 'Coupon discount',
    `pay_amount` DECIMAL(10,2) NOT NULL COMMENT 'Actual payment amount',
    `pay_type` VARCHAR(20) COMMENT 'Payment type: wechat, alipay',
    `pay_time` DATETIME COMMENT 'Payment time',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT 'Status: 0=pending, 1=paid, 2=shipped, 3=completed, 4=cancelled',
    `user_coupon_id` BIGINT COMMENT 'User coupon ID used',
    `remark` VARCHAR(500) COMMENT 'Remark',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_order_no` (`order_no`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order table';

-- Order Item Table
CREATE TABLE `order_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Order item ID',
    `order_id` BIGINT NOT NULL COMMENT 'Order ID',
    `product_id` BIGINT NOT NULL COMMENT 'Product ID',
    `product_name` VARCHAR(100) NOT NULL COMMENT 'Product name',
    `product_image` VARCHAR(255) COMMENT 'Product image',
    `price` DECIMAL(10,2) NOT NULL COMMENT 'Unit price',
    `quantity` INT NOT NULL COMMENT 'Quantity',
    `total_amount` DECIMAL(10,2) NOT NULL COMMENT 'Total amount',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT 'Soft delete',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Create time',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Update time',
    PRIMARY KEY (`id`),
    KEY `idx_order_id` (`order_id`),
    KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Order item table';

-- Insert initial data
INSERT INTO `user` (`username`, `password`, `nickname`, `role`, `employee_discount`) VALUES
('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Administrator', 'admin', 1.00),
('employee1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Employee', 'employee', 0.80),
('customer1', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Customer', 'customer', 1.00);

INSERT INTO `category` (`name`, `parent_id`, `sort`) VALUES
('Beverages', 0, 1),
('Snacks', 0, 2),
('Daily Necessities', 0, 3),
('Instant Food', 0, 4);

INSERT INTO `product` (`category_id`, `name`, `description`, `price`, `employee_price`, `stock`) VALUES
(1, 'Mineral Water', 'Pure mineral water 500ml', 2.00, 1.50, 100),
(1, 'Cola', 'Coca-Cola 330ml', 3.50, 2.80, 80),
(1, 'Orange Juice', 'Fresh orange juice 300ml', 5.00, 4.00, 60),
(2, 'Potato Chips', 'Original flavor 100g', 6.00, 4.80, 50),
(2, 'Chocolate', 'Dark chocolate 50g', 8.00, 6.40, 40),
(3, 'Toothbrush', 'Soft bristle toothbrush', 10.00, 8.00, 30),
(3, 'Towel', 'Cotton towel', 15.00, 12.00, 25),
(4, 'Instant Noodles', 'Beef flavor instant noodles', 4.50, 3.60, 70);

INSERT INTO `coupon` (`name`, `type`, `condition_amount`, `discount_amount`, `total_count`, `start_time`, `end_time`) VALUES
('New User Coupon', 'fixed', 0, 5.00, 1000, '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('Full 50 Minus 10', 'full_reduction', 50.00, 10.00, 500, '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
('20% Off Coupon', 'discount', 30.00, NULL, 800, '2024-01-01 00:00:00', '2024-12-31 23:59:59');
