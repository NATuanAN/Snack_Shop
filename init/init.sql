-- ===========================
-- TẠO BẢNG USER
-- ===========================
CREATE TABLE IF NOT EXISTS "user" (
    user_id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(120) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'buyer', -- buyer, seller, admin
    status VARCHAR(20) NOT NULL DEFAULT 'active'
);

-- ===========================
-- TẠO BẢNG SHOP
-- ===========================
CREATE TABLE IF NOT EXISTS shop (
    shop_id SERIAL PRIMARY KEY,
    shop_name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    description TEXT,
    owner_id INT NOT NULL,
    CONSTRAINT fk_shop_user FOREIGN KEY (owner_id)
        REFERENCES "user"(user_id) ON DELETE CASCADE
);

-- ===========================
-- CATEGORY
-- ===========================
CREATE TABLE IF NOT EXISTS category (
    category_id SERIAL PRIMARY KEY,
    category_name VARCHAR(100) UNIQUE NOT NULL
);

-- ===========================
-- PRODUCT
-- ===========================
CREATE TABLE IF NOT EXISTS product (
    product_id SERIAL PRIMARY KEY,
    product_name VARCHAR(120) NOT NULL,
    description TEXT,
    price NUMERIC(12,2) NOT NULL,
    image_url TEXT,
    stock INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'active',
    category_id INT,
    shop_id INT,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id)
        REFERENCES category(category_id) ON DELETE SET NULL,
    CONSTRAINT fk_product_shop FOREIGN KEY (shop_id)
        REFERENCES shop(shop_id) ON DELETE CASCADE
);

-- ===========================
-- ORDER
-- ===========================
CREATE TABLE IF NOT EXISTS "order" (
    order_id SERIAL PRIMARY KEY,
    buyer_id INT NOT NULL,
    order_date TIMESTAMP DEFAULT NOW(),
    total_amount NUMERIC(12,2),
    status VARCHAR(20) DEFAULT 'pending',
    payment_method VARCHAR(50),
    CONSTRAINT fk_order_user FOREIGN KEY (buyer_id)
        REFERENCES "user"(user_id) ON DELETE CASCADE
);

-- ===========================
-- ORDER DETAIL
-- ===========================
CREATE TABLE IF NOT EXISTS order_detail (
    order_detail_id SERIAL PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price NUMERIC(12,2) NOT NULL,
    subtotal NUMERIC(12,2) NOT NULL,
    CONSTRAINT fk_detail_order FOREIGN KEY (order_id)
        REFERENCES "order"(order_id) ON DELETE CASCADE,
    CONSTRAINT fk_detail_product FOREIGN KEY (product_id)
        REFERENCES product(product_id) ON DELETE CASCADE
);

-- ===========================
-- PROMOTION
-- ===========================
CREATE TABLE IF NOT EXISTS promotion (
    promo_id SERIAL PRIMARY KEY,
    promo_code VARCHAR(50) UNIQUE NOT NULL,
    description TEXT,
    discount_percent INT CHECK(discount_percent BETWEEN 0 AND 100),
    start_date DATE,
    end_date DATE,
    created_by INT,
    CONSTRAINT fk_promotion_user FOREIGN KEY (created_by)
        REFERENCES "user"(user_id) ON DELETE SET NULL
);

-- ===========================
-- REVIEW
-- ===========================
CREATE TABLE IF NOT EXISTS review (
    review_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_review_user FOREIGN KEY (user_id)
        REFERENCES "user"(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_review_product FOREIGN KEY (product_id)
        REFERENCES product(product_id) ON DELETE CASCADE
);

-- ===========================
-- REPORT
-- ===========================
CREATE TABLE IF NOT EXISTS report (
    report_id SERIAL PRIMARY KEY,
    sender_id INT NOT NULL,
    content TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_report_user FOREIGN KEY (sender_id)
        REFERENCES "user"(user_id) ON DELETE CASCADE
);

-- ===========================
-- WISHLIST
-- ===========================
CREATE TABLE IF NOT EXISTS wishlist (
    wishlist_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_wishlist_user FOREIGN KEY (user_id)
        REFERENCES "user"(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_wishlist_product FOREIGN KEY (product_id)
        REFERENCES product(product_id) ON DELETE CASCADE
);

-- ===========================
-- THÊM DỮ LIỆU MẪU CATEGORY
-- ===========================
INSERT INTO category (category_name)
SELECT unnest(ARRAY['Đồ chiên', 'Bánh', 'Đồ uống', 'Ăn mặn'])
WHERE NOT EXISTS (SELECT 1 FROM category);
