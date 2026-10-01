-- Run once against an existing Oracle database to enable in-app notifications.
-- The application uses ddl-auto: none, so Hibernate will not create this table.
DECLARE
    v_count NUMBER;
    v_next_id NUMBER;
BEGIN
    SELECT COUNT(*) INTO v_count
      FROM USER_TABLES
     WHERE TABLE_NAME = 'NOTIFICATIONS';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE q'[
            CREATE TABLE NOTIFICATIONS (
                ID          NUMBER(19)      NOT NULL,
                USER_ID     NUMBER(19)      NOT NULL,
                NOTI_TYPE   VARCHAR2(20)    NOT NULL,
                MESSAGE     VARCHAR2(300)   NOT NULL,
                LINK_URL    VARCHAR2(300),
                IS_READ     CHAR(1)         DEFAULT 'N' NOT NULL,
                CREATED_AT  TIMESTAMP       DEFAULT SYSTIMESTAMP NOT NULL,
                CONSTRAINT PK_NOTIFICATIONS PRIMARY KEY (ID),
                CONSTRAINT FK_NOTIFICATIONS_USER FOREIGN KEY (USER_ID) REFERENCES USERS (ID),
                CONSTRAINT CK_NOTIFICATIONS_READ CHECK (IS_READ IN ('Y', 'N'))
            )
        ]';
    END IF;

    SELECT COUNT(*) INTO v_count
      FROM USER_SEQUENCES
     WHERE SEQUENCE_NAME = 'NOTIFICATIONS_SEQ';

    IF v_count = 0 THEN
        EXECUTE IMMEDIATE 'SELECT NVL(MAX(ID), 0) + 1 FROM NOTIFICATIONS' INTO v_next_id;
        EXECUTE IMMEDIATE 'CREATE SEQUENCE NOTIFICATIONS_SEQ START WITH '
            || TO_CHAR(v_next_id) || ' INCREMENT BY 1 NOCACHE NOCYCLE';
    END IF;
END;
/
