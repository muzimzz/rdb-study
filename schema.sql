-- =============================================
-- Shopping Mall Schema
-- =============================================

-- 테이블 삭제 (재실행 시 순서 중요 - FK 역순으로 삭제)
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS member_coupons;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS carts;
DROP TABLE IF EXISTS coupons;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS members;


-- =============================================
-- 1. members
-- =============================================
CREATE TABLE members
(
    member_id BIGINT      AUTO_INCREMENT PRIMARY KEY,
    name      VARCHAR(50) NOT NULL,
    email     VARCHAR(100) NOT NULL UNIQUE,
    password  VARCHAR(255) NOT NULL,
    address   VARCHAR(255),
    status    VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',   -- ACTIVE | INACTIVE
    role      VARCHAR(20) NOT NULL DEFAULT 'USER',     -- USER | ADMIN
    join_date DATETIME             DEFAULT NOW(),
    CONSTRAINT chk_member_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_member_role   CHECK (role   IN ('USER', 'ADMIN'))
);


-- =============================================
-- 2. products
-- =============================================
CREATE TABLE products
(
    product_id     BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(100) NOT NULL,
    category       VARCHAR(20)  NOT NULL,               -- FOOD | ANIMAL | ETC
    price          INT          NOT NULL CHECK (price >= 0),
    stock_quantity INT          NOT NULL DEFAULT 0 CHECK (stock_quantity >= 0),
    description    TEXT,
    status         VARCHAR(20)  NOT NULL DEFAULT 'ON_SALE', -- ON_SALE | SOLD_OUT | DISCONTINUED
    created_at     DATETIME              DEFAULT NOW(),
    CONSTRAINT chk_product_category CHECK (category IN ('FOOD', 'ANIMAL', 'ETC')),
    CONSTRAINT chk_product_status   CHECK (status   IN ('ON_SALE', 'SOLD_OUT', 'DISCONTINUED'))
);


-- =============================================
-- 3. coupons
-- =============================================
CREATE TABLE coupons
(
    coupon_id           BIGINT       AUTO_INCREMENT PRIMARY KEY,
    name                VARCHAR(100) NOT NULL,
    code                VARCHAR(50)  UNIQUE,                 -- NULL이면 관리자 직접 발급 전용, 값이 있으면 코드 입력 방식
    discount_rate       INT          NOT NULL CHECK (discount_rate BETWEEN 1 AND 100),
    min_order_amount    INT          NOT NULL DEFAULT 0,     -- 최소 주문 금액 (0이면 제한 없음)
    max_discount_amount INT,                                 -- 최대 할인 금액 (NULL이면 제한 없음)
    max_issue_count     INT,                                 -- 발급 한도 (NULL이면 무제한)
    issued_count        INT          NOT NULL DEFAULT 0,     -- 현재 발급된 수 (동시성 제어 핵심 컬럼)
    expired_at          DATETIME     NOT NULL,
    created_at          DATETIME              DEFAULT NOW()
);


-- =============================================
-- 4. carts  (회원 가입 시 자동 생성, 1인 1장바구니)
-- =============================================
CREATE TABLE carts
(
    cart_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id BIGINT NOT NULL UNIQUE,
    FOREIGN KEY (member_id) REFERENCES members (member_id)
);


-- =============================================
-- 5. cart_items
-- =============================================
CREATE TABLE cart_items
(
    cart_item_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id      BIGINT NOT NULL,
    product_id   BIGINT NOT NULL,
    quantity     INT    NOT NULL DEFAULT 1 CHECK (quantity > 0),
    FOREIGN KEY (cart_id)    REFERENCES carts (cart_id)    ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);


-- =============================================
-- 6. member_coupons  (발급된 쿠폰 내역)
-- =============================================
CREATE TABLE member_coupons
(
    member_coupon_id BIGINT   AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT   NOT NULL,
    coupon_id        BIGINT   NOT NULL,
    is_used          BOOLEAN  NOT NULL DEFAULT FALSE,
    used_at          DATETIME,          -- 사용 시점 (NULL이면 미사용)
    issued_at        DATETIME          DEFAULT NOW(),
    FOREIGN KEY (member_id) REFERENCES members (member_id),
    FOREIGN KEY (coupon_id) REFERENCES coupons (coupon_id),
    UNIQUE (member_id, coupon_id)       -- 같은 쿠폰 중복 발급 방지
);


-- =============================================
-- 7. orders
-- =============================================
CREATE TABLE orders
(
    order_id         BIGINT  AUTO_INCREMENT PRIMARY KEY,
    member_id        BIGINT  NOT NULL,
    member_coupon_id BIGINT,                                -- NULL이면 쿠폰 미사용
    discount_amount  INT     NOT NULL DEFAULT 0,
    order_date       DATETIME         DEFAULT NOW(),
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING', -- PENDING | SHIPPED | DELIVERED | CANCELLED
    FOREIGN KEY (member_id)        REFERENCES members (member_id),
    FOREIGN KEY (member_coupon_id) REFERENCES member_coupons (member_coupon_id),
    CONSTRAINT chk_order_status CHECK (status IN ('PENDING', 'SHIPPED', 'DELIVERED', 'CANCELLED'))
);


-- =============================================
-- 8. order_items
-- =============================================
CREATE TABLE order_items
(
    order_id       BIGINT NOT NULL,
    product_id     BIGINT NOT NULL,
    quantity       INT    NOT NULL CHECK (quantity > 0),
    price_at_order INT    NOT NULL,   -- 주문 시점 가격 (나중에 가격 변경돼도 이력 보존)
    PRIMARY KEY (order_id, product_id),
    FOREIGN KEY (order_id)   REFERENCES orders (order_id)   ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products (product_id)
);


-- =============================================
-- 9. reviews
-- =============================================
CREATE TABLE reviews
(
    review_id  BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id  BIGINT   NOT NULL,
    product_id BIGINT   NOT NULL,
    rating     INT      NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content    TEXT     NOT NULL,
    created_at DATETIME          DEFAULT NOW(),
    updated_at DATETIME,
    FOREIGN KEY (member_id)  REFERENCES members (member_id),
    FOREIGN KEY (product_id) REFERENCES products (product_id),
    UNIQUE (member_id, product_id)   -- 상품당 리뷰 1개 제한
);


-- =============================================
-- 테스트용 기본 데이터 (선택사항)
-- =============================================

-- 관리자 계정 (비밀번호: admin1234 → BCrypt 인코딩 필요, 아래는 예시값)
-- INSERT INTO members (name, email, password, address, status, role)
-- VALUES ('관리자', 'admin@test.com', '$2a$10$...', '서울시', 'ACTIVE', 'ADMIN');

-- 테스트 상품
-- INSERT INTO products (name, category, price, stock_quantity, description, status)
-- VALUES ('테스트 상품1', 'FOOD', 10000, 100, '설명', 'ON_SALE');
