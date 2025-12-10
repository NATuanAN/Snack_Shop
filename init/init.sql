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

INSERT INTO product(product_name,price,image_url)
VALUES
('Snack vị bò nướng Texas Lays 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/226184/bhx/snack-khoai-tay-vi-than-bo-nuong-texas-lays-wavy-goi-56g_202508211052036366.jpg'),
('Snack vị tự nhiên Lays 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/193606/bhx/snack-poca-khoai-tay-tu-nhien-goi-52g_202510150939206616.jpg'),
('Snack vị sườn BBQ Lays 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/193603/bhx/snack-poca-khoai-tay-suon-nuong-52g_202509241622313039.jpg'),
('Snack vị tảo biển Lays 50g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/193602/bhx/thumb-193602_202411261120181132.jpg'),
('Snack vị tảo biển OStar 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/79812/bhx/snack-ostar-rong-bien-48g_202511181412004013.jpg'),
('Snack vị kim chi OStar 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/79804/bhx/snack-khoai-tay-ostar-vi-kim-chi-han-quoc-48g_202511181404222381.jpg'),
('Snack vị bít tết Swing 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/79773/bhx/snack-swing-nysteak-48g_202511251647001007.jpg'),
('Snack vị bò lúc lắc Poca 65g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/283009/bhx/thumb-283009_202411261130399401.jpg'),
('Snack vị tảo biển Orion 56g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/305485/bhx/thumb-305485_202411261132325426.jpg'),
('Snack vị bò Wagyu Lays Max 42g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/313108/bhx/thumb-313108_202411261134209919.jpg'),
('Snack sốt Mayo kiểu Hàn OStar gói 50g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/327381/bhx/thumb-327381-1_202411261351494616.jpg'),
('Snack tôm nướng ngũ vị Lays 42g', 22000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/341877/bhx/snack-khoai-tay-tom-hum-nuong-ngu-vi-lays-stax-lon-42g_202508041450197713.jpg'),
('Snack vị tự nhiên Lays Stax 42g', 22000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/341873/bhx/snack-khoai-tay-vi-tu-nhien-lays-stax-lon-42g_202508041439104475.jpg'),
('Snack vị kem chua & hành Lays 42g', 22000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/341658/bhx/snack-khoai-tay-vi-kem-chua-hanh-lays-stax-lon-42g_202508041427115966.jpg'),
('Snack vị phô mai Cheddar Lays 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/339156/bhx/snack-khoai-tay-vi-pho-mai-cheddar-lays-wavy-goi-54g_202510150943599559.jpg'),
('Snack vị tôm càng đút phô mai Lays 53g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/339146/bhx/snack-khoai-tay-vi-tom-cang-dut-lo-pho-mai-tan-chay-lays-goi-54g_202510150952082071.jpg'),
('Snack mũ pháp sư vị phô mai Poca 58g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/339143/bhx/snack-hinh-mu-phap-su-vi-pho-mai-poca-goi-58g_202506041018517919.jpg'),
('Snack mực lăn muối ớt Poca 60g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/339136/bhx/snack-muc-lan-muoi-ot-poca-goi-60g_202506031456283223.jpg'),
('Snack hải sản vị cay ngọt Bento 22g', 29000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/338657/bhx/snack-hai-san-cay-ngot-bento-goi-22g_202505270844320036.jpg'),
('Snack hải sản vị cay nồng Bento 22g', 29000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/338656/bhx/snack-hai-san-vi-cay-nong-bento-goi-22g_202505270857071401.jpg'),
('Snack bắp vị phô mai cay Koikeya Karamucho 60g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/338644/bhx/snack-bap-vi-pho-mai-cay-koikeya-karamucho-goi-60g_202505261441423139.jpg'),
('Snack bắp Koikeya Polinky 54g', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/338642/bhx/snack-bap-koikeya-polinky-goi-54g_202505261422442891.jpg'),
('Bánh snack vị phô mai Nabati 80g', 24500, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/338613/bhx/banh-snack-vi-pho-mai-nabati-siip-hop-80g-4g-x-20-goi_202505260954160507.jpg'),
('Bánh snack vị bắp rang Nabati 80g', 24500, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/338611/bhx/banh-snack-vi-bap-rang-nabati-siip-hop-80g-4g-x-20-goi_202505260943443524.jpg'),
('Snack bắp vị socola Mplus 260g', 36000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/334868/bhx/snack-bap-vi-socola-mplus-chocolate-popcorn-chai-260g_202503281625537261.jpg'),
('Snack hải sản Namprik Thái Bento 22g', 29000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/333971/bhx/thumb-333577-1_202501151003246540.jpg'),
('Snack tôm xốt tương mật ong Oishi 70g', 12300, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/333564/bhx/thumb-333564-1_202412251002083319.jpg'),
('Snack đậu xanh nước dừa Oishi 65g', 12300, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/333559/bhx/snack-dau-xanh-nuoc-dua-oishi-goi-70g_202506131528394847.jpg'),
('Snack mực vị tương ớt Sriracha Talaethong 100g', 32000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/332876/bhx/snack-muc-vi-tuong-ot-sriracha-talaethong-goi-100g_202507281349450007.jpg'),
('Snack bắp vị kem Mplus 260g', 36000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/327619/bhx/snack-bap-vi-kem-mplus-creamy-popcorn-chai-260g_202503281629438389.jpg'),
('Snack bắp vị caramen Mplus 260g', 36000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/327616/bhx/thumb-327616_202411261403075655.jpg'),
('Snack tôm vị cay nồng Oishi', 12000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/305937/bhx/snack-tom-vi-cay-nong-oishi-goi-70g_202505270833459443.jpg'),
('Snack socola vị hazelnut Oishi Pillows 85g', 12300, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/305932/bhx/thumb-305932_202411261132580586.jpg'),
('Snack kem Tiramisu Akiko Oishi 140g', 24000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/305735/bhx/thumb-305735_202411261132415506.jpg'),
('Snack kem Mixed Berries Akiko Oishi 140g', 24000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/305733/bhx/thumb-305733_202411261132362500.jpg'),
('Snack mực rong biển Talaethong 100g', 31000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/282757/bhx/snack-muc-vi-rong-bien-talaethong-goi-100g_202507281356252761.jpg'),
('Snack mực Talaethong 100g', 31000, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/282450/bhx/snack-muc-vi-truyen-thong-talaethong-goi-100g_202507281356290014.jpg'),
('Snack rong biển truyền thống Kimmy 6g', 15400, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/248296/bhx/thumb-248296_202411261128147817.jpg'),
('Snack rong biển phô mai Kimmy 6g', 15400, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/248295/bhx/thumb-248295_202411261128107297.jpg'),
('Snack mì ớt cấp độ 1 Enaak 28g', 6200, 'https://cdnv2.tgdd.vn/bhx-static/bhx/Products/Images/3364/226028/bhx/thumb-226028_202411261127124889.jpg');