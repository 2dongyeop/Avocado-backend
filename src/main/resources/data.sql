INSERT INTO member(member_nickname, member_email, member_password, member_phonenumber)
values ('이동엽', 'ldy_1204@naver.com', 'lee1', '01011112222'),
       ('임세나', 'sena@naver.com', 'lim1', '01033334444');

INSERT INTO hospital(hosp_name, hosp_number, hosp_address, hosp_operatingtime)
values ('을지대학병원', '04211112222', '대전광역시', '연중무휴'),
       ('서울대병원', '0233334444', '서울특별시', '연중무휴');

INSERT INTO board(member_id, board_title, board_body, dept, board_create_at, board_status)
VALUES (1, '이빨이 부러진 것 같아요.', '축구하다 이빨이 부러졌는데 어디로 가야하죠?', 'DENTAL', NOW(), 'WRITE'),
       (2, '사랑니가 났어요', '사랑니를 굳이 빼야할까요?', 'DENTAL', NOW(), 'WRITE');

INSERT INTO appointment(member_id, hosp_id, appt_dept, appt_comment, appt_name, appt_phonenumber, appt_create_at)
VALUES (1, 2, 'DENTAL', '이빨 붙여주세요.', '이동엽', '01011112222', NOW()),
       (2, 1, 'DENTAL', '사랑니 상담 부탁드려요.', '임세나', '01033334444', NOW());

INSERT INTO staff(staff_name, staff_email, staff_password, staff_license_path, dept, hosp_id)
VALUES ('윤진원', 'yjw@naver.com', 'yoon1', 'temp_license1', 'DENTAL', 1),
       ('서동권', 'seo@naver.com', 'seo1', 'temp_license2', 'DENTAL', 2);

INSERT INTO board_reply(board_id, staff_id, reply, boardreply_create_at)
VALUES (1, 1, '을지대학교 치과병원으로 오세요.', NOW()),
       (2, 2, '사랑니는 발치하셔야 합니다. 서울대병원으로 오세요.', NOW());

INSERT INTO pick(member_id, hosp_id, pick_date_time)
VALUES (1, 1, NOW()),
       (2, 2, NOW());

INSERT INTO review(member_id, review_title, review_body, star_point, target_hospital, review_create_at)
VALUES (1, '을지대학교 치과병원 추천합니다.', '부러진 이빨을 감쪽같이 치료했어요.', 5, '을지대학병원', NOW()),
       (2, '서울대병원 후기', '다 좋은데 대기 시간이 길어요.', 3, '서울대병원', NOW());

INSERT INTO review_reply(member_id, review_id, reply, review_reply_create_at)
VALUES (2, 1, '저도 가봐야겠네요.', NOW()),
       (1, 2, '힘드셨겠군요..', NOW());

INSERT INTO health_info(staff_id, health_info_title, health_info_path, dept, health_info_create_at)
VALUES (1, '부러진 치아 응급처치방법', 'temp_path1', 'DENTAL', NOW()),
       (2, '건강한 치아 만들기', 'temp_path2', 'DENTAL', NOW());

INSERT INTO bus_info(bus_info_path, bus_info_area, bus_info_create_at)
VALUES ('temp_bus_path1', 'DAEJEON', NOW()),
       ('temp_bus_path2', 'SEOUL', NOW());
