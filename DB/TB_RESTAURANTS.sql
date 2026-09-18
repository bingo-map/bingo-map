-- ====================================================================
-- [1단계: 기존 테이블 및 시퀀스 초기화]
-- - 기존에 만들어진 테이블이나 번호표 생성기가 있다면 충돌 방지를 위해 삭제합니다.
-- ====================================================================
DROP TABLE RESTAURANT CASCADE CONSTRAINTS;
DROP SEQUENCE SEQ_RESTAURANT;


-- ====================================================================
-- [2단계: 식당 고유번호(PK) 자동 생성기(시퀀스) 생성]
-- - 맛집 데이터가 새로 들어올 때마다 번호(1, 2, 3...)를 자동으로 1씩 올려줍니다.
-- ====================================================================
CREATE SEQUENCE SEQ_RESTAURANT
    START WITH 1          -- 1번부터 시작
    INCREMENT BY 1        -- 1씩 증가
    NOCACHE               -- 캐시 메모리 미사용 (서버 종료 시 번호 건너뜀 방지)
    NOCYCLE;              -- 번호 순환 없음


-- ====================================================================
-- [3단계: 맛집(RESTAURANT) 테이블 생성]
-- - 공지사항 컬럼을 제외하고, 기본 정보와 대표 메뉴 컬럼만 포함합니다.
-- ====================================================================
CREATE TABLE RESTAURANT (
    RESTAURANT_ID      NUMBER(19) PRIMARY KEY, -- 식당 고유 식별 번호 (기본키, PK)
    NAME               VARCHAR2(100) NOT NULL, -- 식당 이름 (필수 입력)
    CATEGORY           VARCHAR2(50),           -- 음식 종류 (타코야끼, 야끼소바 등 목록 필터용)
    TAGS               VARCHAR2(200),          -- 검색 및 뱃지용 태그 (쉼표로 구분)
    RATING             NUMBER(3,1),            -- 별점 평점 (예: 4.6, 소수점 1자리)
    REVIEW_COUNT       NUMBER(10),             -- 등록된 리뷰 총 개수
    DESCRIPTION        VARCHAR2(1000),         -- 식당 한 줄 소개 및 설명글
    ADDRESS            VARCHAR2(300),          -- 식당 실제 도로명/지번 주소
    LATITUDE           NUMBER(10,7),           -- 지도 위치 위도 좌표
    LONGITUDE          NUMBER(10,7),           -- 지도 위치 경도 좌표
    OPENING_HOURS      VARCHAR2(100),          -- 매장 영업 시간 (예: 09:00 - 21:00)
    PHONE              VARCHAR2(50),           -- 매장 전화번호 (전화걸기 버튼 연동)
    PRICE_RANGE        VARCHAR2(50),           -- 1인당 예상 가격대 (예: ¥500 - ¥1,500)
    WEBSITE_URL        VARCHAR2(300),          -- 공식 웹사이트 또는 인스타그램 URL
    SEAT_INFO          VARCHAR2(100),          -- 좌석 형태 (예: 20석, 테이크아웃 전문 등)
    RESERVATION_INFO   VARCHAR2(100),          -- 예약 가능 여부 (예: 예약 불가, 예약 필수)
    PAYMENT_METHODS    VARCHAR2(200),          -- 결제 수단 (예: 현금, 신용카드, PayPay)
    LANGUAGES          VARCHAR2(200),          -- 제공 메뉴판 언어 (예: 일본어, 한국어)
    MAIN_IMAGE_URL     VARCHAR2(500),          -- 대표 사진 경로
    
    -- [대표 메뉴 관련 정보 컬럼]
    MENU_NAME          VARCHAR2(100),          -- 대표 메뉴 이름
    MENU_DESCRIPTION   VARCHAR2(500),          -- 대표 메뉴 간단 설명
    MENU_PRICE         VARCHAR2(50),           -- 대표 메뉴 가격 (예: ¥850)
    MENU_IMAGE_URL     VARCHAR2(500)           -- 대표 메뉴 사진 경로
);


-- ====================================================================
-- [4단계: 테스트용 샘플 맛집 데이터 등록 (총 5건)]
-- - 공지사항 항목을 제외하고 대표 메뉴 정보가 포함되도록 등록합니다.
-- ====================================================================

-- [데이터 1] 쿠쿠루 도톤보리 본점 (타코야끼)
INSERT INTO RESTAURANT (
    RESTAURANT_ID, NAME, CATEGORY, TAGS, RATING, REVIEW_COUNT, 
    DESCRIPTION, ADDRESS, LATITUDE, LONGITUDE, OPENING_HOURS, 
    PHONE, PRICE_RANGE, WEBSITE_URL, SEAT_INFO, RESERVATION_INFO, 
    PAYMENT_METHODS, LANGUAGES, MAIN_IMAGE_URL,
    MENU_NAME, MENU_DESCRIPTION, MENU_PRICE, MENU_IMAGE_URL
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
    '¥500 - ¥1,500',
    'https://www.kukurutei.com',
    '20석',
    '예약 불가 (현장 대기)',
    '현금, 신용카드, 전자화폐',
    '일본어, 한국어 메뉴판',
    '/images/food-takoyaki.png',
    '명물 타코야끼 (8개)',
    '쿠쿠루만의 특제 육즙이 가득한 시그니처 메뉴',
    '¥850',
    'https://via.placeholder.com/64?text=Takoyaki'
);

-- [데이터 2] 야끼소바 산페이 (야끼소바)
INSERT INTO RESTAURANT (
    RESTAURANT_ID, NAME, CATEGORY, TAGS, RATING, REVIEW_COUNT, 
    DESCRIPTION, ADDRESS, LATITUDE, LONGITUDE, OPENING_HOURS, 
    PHONE, PRICE_RANGE, WEBSITE_URL, SEAT_INFO, RESERVATION_INFO, 
    PAYMENT_METHODS, LANGUAGES, MAIN_IMAGE_URL,
    MENU_NAME, MENU_DESCRIPTION, MENU_PRICE, MENU_IMAGE_URL
) VALUES (
    SEQ_RESTAURANT.NEXTVAL,
    '야끼소바 산페이',
    '야끼소바',
    '야끼소바,철판요리,감칠맛',
    4.4,
    892,
    '특제 소스의 깊은 감칠맛이 살아있는 전통 철판 야끼소바 전문점',
    '1-7-14 Dotonbori, Chuo-ku, Osaka',
    34.668900,
    135.502100,
    '10:30 - 20:30',
    '+81-6-6211-1234',
    '¥700 - ¥1,200',
    'https://www.sanpei-osaka.jp',
    '15석 (카운터석)',
    '예약 불가',
    '현금, 신용카드',
    '일본어, 영어',
    '/images/food-yakisoba.png',
    '특제 소스 야끼소바',
    '진한 특제 소스와 쫄깃한 면발의 조화',
    '¥900',
    'https://via.placeholder.com/64?text=Yakisoba'
);

-- [데이터 3] 오코노미야끼 치보 도톤보리빌딩점 (오코노미야끼)
INSERT INTO RESTAURANT (
    RESTAURANT_ID, NAME, CATEGORY, TAGS, RATING, REVIEW_COUNT, 
    DESCRIPTION, ADDRESS, LATITUDE, LONGITUDE, OPENING_HOURS, 
    PHONE, PRICE_RANGE, WEBSITE_URL, SEAT_INFO, RESERVATION_INFO, 
    PAYMENT_METHODS, LANGUAGES, MAIN_IMAGE_URL,
    MENU_NAME, MENU_DESCRIPTION, MENU_PRICE, MENU_IMAGE_URL
) VALUES (
    SEQ_RESTAURANT.NEXTVAL,
    '오코노미야끼 치보 도톤보리빌딩점',
    '오코노미야끼',
    '오코노미야끼,철판구이,웨이팅맛집',
    4.5,
    1102,
    '풍미 가득한 일본 정통 부침개! 눈앞에서 구워주는 인기 오코노미야끼 매장',
    '1-5-5 Dotonbori, Chuo-ku, Osaka',
    34.668500,
    135.503200,
    '11:00 - 21:30',
    '+81-6-6212-2211',
    '¥1,000 - ¥2,500',
    'https://www.chibo.com',
    '50석 (테이블 및 다찌석)',
    '전화 예약 가능',
    '현금, 신용카드, 모바일페이',
    '한국어 지원 (다국어 키오스크)',
    '/images/food-okonomiyaki.png',
    '치보 믹스 오코노미야끼',
    '새우, 오징어, 돼지고기가 모두 들어간 베스트 메뉴',
    '¥1,580',
    'https://via.placeholder.com/64?text=Okonomiyaki'
);

-- [데이터 4] 도톤보리 타이야끼 (붕어빵/디저트)
INSERT INTO RESTAURANT (
    RESTAURANT_ID, NAME, CATEGORY, TAGS, RATING, REVIEW_COUNT, 
    DESCRIPTION, ADDRESS, LATITUDE, LONGITUDE, OPENING_HOURS, 
    PHONE, PRICE_RANGE, WEBSITE_URL, SEAT_INFO, RESERVATION_INFO, 
    PAYMENT_METHODS, LANGUAGES, MAIN_IMAGE_URL,
    MENU_NAME, MENU_DESCRIPTION, MENU_PRICE, MENU_IMAGE_URL
) VALUES (
    SEQ_RESTAURANT.NEXTVAL,
    '도톤보리 타이야끼',
    '붕어빵',
    '길거리간식,붕어빵,디저트',
    4.3,
    567,
    '국산 팥과 바삭한 크러스트의 조화! 따끈하게 즐기는 테이크아웃 붕어빵',
    '1-8-22 Dotonbori, Chuo-ku, Osaka',
    34.668350,
    135.500800,
    '10:00 - 19:00',
    '+81-6-6213-9876',
    '¥300 - ¥600',
    NULL,
    '없음 (테이크아웃 전용)',
    '예약 불가',
    '현금 전용',
    '일본어 메뉴',
    '/images/food-taiyaki.png',
    '통단팥 붕어빵',
    '달콤하고 부드러운 팥이 꽉 찬 인기 간식',
    '¥300',
    'https://via.placeholder.com/64?text=Taiyaki'
);

-- [데이터 5] 카라아게 타로 (닭튀김)
INSERT INTO RESTAURANT (
    RESTAURANT_ID, NAME, CATEGORY, TAGS, RATING, REVIEW_COUNT, 
    DESCRIPTION, ADDRESS, LATITUDE, LONGITUDE, OPENING_HOURS, 
    PHONE, PRICE_RANGE, WEBSITE_URL, SEAT_INFO, RESERVATION_INFO, 
    PAYMENT_METHODS, LANGUAGES, MAIN_IMAGE_URL,
    MENU_NAME, MENU_DESCRIPTION, MENU_PRICE, MENU_IMAGE_URL
) VALUES (
    SEQ_RESTAURANT.NEXTVAL,
    '카라아게 타로',
    '닭튀김',
    '치킨,가라아게,맥주안주',
    4.4,
    734,
    '바삭한 튀김옷 속 육즙이 가득! 특제 마늘 간장 소스로 버무린 정통 일본식 닭튀김',
    '2-2-1 Nanba, Chuo-ku, Osaka',
    34.667800,
    135.500200,
    '11:00 - 21:00',
    '+81-6-6631-5544',
    '¥600 - ¥1,000',
    'https://www.karaage-taro.jp',
    '10석',
    '예약 불가',
    '현금, 신용카드',
    '일본어, 영어',
    '/images/food-karaage.png',
    '카라아게 타로 (오리지널)',
    '겉은 바삭하고 속은 촉촉한 대표 닭튀김',
    '¥680',
    'https://via.placeholder.com/64?text=Karaage'
);


-- ====================================================================
-- [5단계: 변경 사항 영구 반영(COMMIT) 및 데이터 조회 검증]
-- ====================================================================
COMMIT;

-- 등록된 데이터 중 식당 이름과 대표 메뉴가 잘 들어갔는지 확인하는 조회 쿼리
SELECT RESTAURANT_ID, NAME, MENU_NAME, MENU_PRICE FROM RESTAURANT;

-- 총 등록된 맛집 개수 확인 
SELECT COUNT(*) AS TOTAL_COUNT FROM RESTAURANT;