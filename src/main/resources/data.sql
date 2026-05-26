CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- =========================================
-- POSITIONS
-- =========================================

INSERT INTO positions (id,
                       name,
                       department_id,
                       hierarchy_level,
                       created_at)
VALUES ('11111111-1111-1111-1111-111111111111',
        'ADMIN',
        NULL,
        100,
        NOW()),
       ('22222222-2222-2222-2222-222222222222',
        'USER',
        NULL,
        1,
        NOW())
ON CONFLICT (id) DO NOTHING;


-- =========================================
-- ADMIN USER
-- password: Admin123!
-- =========================================

INSERT INTO users (id,
                   email,
                   username,
                   phone_number,
                   password_hash,
                   validated_at,
                   created_at,
                   updated_at,
                   deleted_at)
VALUES ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        'admin@citruschat.com',
        'admin',
        '+59899999999',
        '$2a$10$REym006Yo6TmEpL7yLAqluHiChR3NrkGkMpUCIJKw83bDhsYVY3GW', -- Admin123!
        NOW(),
        NOW(),
        NOW(),
        NULL)
ON CONFLICT (id) DO UPDATE SET
                               email = EXCLUDED.email,
                               username = EXCLUDED.username,
                               phone_number = EXCLUDED.phone_number,
                               password_hash = EXCLUDED.password_hash,
                               validated_at = EXCLUDED.validated_at,
                               updated_at = NOW(),
                               deleted_at = NULL;

-- =========================================
-- USER ORGANIZATION
-- =========================================

INSERT INTO user_organization (id,
                               user_id,
                               position_id,
                               manager_id,
                               assigned_at)
VALUES ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        '11111111-1111-1111-1111-111111111111',
        NULL,
        NOW())
ON CONFLICT (id) DO NOTHING;

-- =========================================
-- TEST USERS
-- password: User123!
-- =========================================

INSERT INTO users (id,
                   email,
                   username,
                   phone_number,
                   password_hash,
                   validated_at,
                   created_at,
                   updated_at,
                   deleted_at)
VALUES

       ('dddddddd-dddd-dddd-dddd-dddddddddddd',
        'jane@citruschat.com',
        'jane_doe',
        '+59892222222',
        '$2a$10$6s7K9G7VYkM0F0K3GQ1b3eV7Q5D6W7X8Y9Z0aBcDeFgHiJkLmNoP2',
        NOW(),
        NOW(),
        NOW(),
        NULL),

       ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        'mike@citruschat.com',
        'mike_smith',
        '+59893333333',
        '$2a$10$6s7K9G7VYkM0F0K3GQ1b3eV7Q5D6W7X8Y9Z0aBcDeFgHiJkLmNoP2',
        NOW(),
        NOW(),
        NOW(),
        NULL)

    ON CONFLICT (id) DO UPDATE SET
    email = EXCLUDED.email,
                            username = EXCLUDED.username,
                            phone_number = EXCLUDED.phone_number,
                            password_hash = EXCLUDED.password_hash,
                            validated_at = EXCLUDED.validated_at,
                            updated_at = NOW(),
                            deleted_at = NULL;

-- =========================================
-- USER ORGANIZATION
-- =========================================

INSERT INTO user_organization (id,
                               user_id,
                               position_id,
                               manager_id,
                               assigned_at)
VALUES ('f1111111-1111-1111-1111-111111111111',
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        '22222222-2222-2222-2222-222222222222',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        NOW()),

       ('f2222222-2222-2222-2222-222222222222',
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        '22222222-2222-2222-2222-222222222222',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        NOW()),

       ('f3333333-3333-3333-3333-333333333333',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        '22222222-2222-2222-2222-222222222222',
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        NOW())

    ON CONFLICT (id) DO NOTHING;

-- =========================================
-- CHAT PERMISSIONS
-- =========================================

INSERT INTO chat_permissions (id, code, description)
VALUES
-- Message based permissions
(gen_random_uuid(), 'CAN_SEND_MESSAGE', 'Allows sending messages'),
(gen_random_uuid(), 'CAN_DELETE_MESSAGE', 'Allows deleting messages'),
(gen_random_uuid(), 'CAN_EDIT_MESSAGE', 'Allows editing messages'),
(gen_random_uuid(), 'CAN_VIEW_MESSAGE', 'Allows viewing messages'),
(gen_random_uuid(), 'CAN_ATTACH_FILE', 'Allows attaching multimedia files and audio messages'),
(gen_random_uuid(), 'CAN_START_CALL', 'Allows starting audio and video calls'),
(gen_random_uuid(), 'CAN_PING_MESSAGE', 'Allows pinging messages'),

-- Role based permissions
(gen_random_uuid(), 'CAN_CREATE_ROLE', 'Allows creating chat roles'),
(gen_random_uuid(), 'CAN_MODIFY_ROLE', 'Allows modifying chat roles'),
(gen_random_uuid(), 'CAN_DELETE_ROLE', 'Allows deleting chat roles'),

-- ChatParticipant based permissions
(gen_random_uuid(), 'CAN_MODIFY_CHAT_PARTICIPANT', 'Allows modifying chat participants'),
(gen_random_uuid(), 'CAN_REMOVE_CHAT_PARTICIPANT', 'Allows removing chat participants'),
(gen_random_uuid(), 'CAN_ADD_CHAT_PARTICIPANT', 'Allows adding chat participants'),

-- Chat based permissions
(gen_random_uuid(), 'CAN_DELETE_CHAT', 'Allows deleting chats'),
(gen_random_uuid(), 'CAN_MODIFY_CHAT', 'Allows modifying chats')

    ON CONFLICT (code) DO NOTHING;