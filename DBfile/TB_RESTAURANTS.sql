drop table RESTAURANT;


-- 1. 시퀀스 생성 (엔티티의 SEQ_RESTAURANT와 일치)
CREATE SEQUENCE SEQ_RESTAURANT
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

-- 2. RESTAURANT 테이블 생성
CREATE TABLE RESTAURANT (
    RESTAURANT_ID    NUMBER(19) PRIMARY KEY,
    NAME             VARCHAR2(100) NOT NULL,
    CATEGORY         VARCHAR2(50),
    TAGS             VARCHAR2(200),
    RATING           NUMBER(3,1),
    REVIEW_COUNT     NUMBER(10),
    DESCRIPTION      VARCHAR2(1000),
    ADDRESS          VARCHAR2(300),
    LATITUDE         NUMBER(10,7),
    LONGITUDE        NUMBER(10,7),
    OPENING_HOURS    VARCHAR2(100),
    PHONE            VARCHAR2(50),
    PRICE_RANGE      VARCHAR2(50),
    WEBSITE_URL      VARCHAR2(300),
    SEAT_INFO        VARCHAR2(100),
    RESERVATION_INFO VARCHAR2(100),
    PAYMENT_METHODS  VARCHAR2(200),
    LANGUAGES        VARCHAR2(200),
    MAIN_IMAGE_URL   VARCHAR2(500)
);

-- 3. 테스트용 샘플 데이터 1건 넣기
INSERT INTO RESTAURANT (
    RESTAURANT_ID, NAME, CATEGORY, TAGS, RATING, REVIEW_COUNT, 
    DESCRIPTION, ADDRESS, LATITUDE, LONGITUDE, OPENING_HOURS, 
    PHONE, PRICE_RANGE, WEBSITE_URL, SEAT_INFO, RESERVATION_INFO, 
    PAYMENT_METHODS, LANGUAGES, MAIN_IMAGE_URL
) VALUES (
    SEQ_RESTAURANT.NEXTVAL,
    '쿠쿠루 도톤보리 본점',
    '타코야끼',
    '도톤보리,타코야끼,오사카맛집',
    4.6,
    1248,
    '겉은 바삭, 속은 촉촉! 도톤보리 대표 타코야끼 맛집',
    '1-10-5 Dotonbori, Chuo-ku, Osaka',
    34.668729,
    135.501294,
    '09:00 - 21:00',
    '+81-6-6212-7381',
    '?500 - ?1,500',
    'https://www.kukurutei.com',
    '20석',
    '예약 불가 (현장 대기)',
    '현금, 신용카드, 전자화폐',
    '일본어, 한국어 메뉴판',
    '/images/food-takoyaki.png'
);

-- 4. 반드시 커밋(반영) 수행!
COMMIT;

SELECT * FROM RESTAURANT;