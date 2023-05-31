insert into `users` (`email`, `login_type`, `user_status`)
values
    ('test1@test.com', 'KAKAO',  'VERIFIED')
;

insert into `user_roles`(`user_user_id`, `roles`)
values
    (1, 'USER')
;