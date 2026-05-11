-- ============================================================
-- TRANSACTIONS
-- ============================================================
USE artconnect;

-- Transaction 1 : inscription multiple atomique
START TRANSACTION;
INSERT INTO EventRegistration(member_id, event_id, nbTickets, status) VALUES (6, 3, 1, 'confirmed');
INSERT INTO EventRegistration(member_id, event_id, nbTickets, status) VALUES (6, 5, 1, 'confirmed');
INSERT INTO EventRegistration(member_id, event_id, nbTickets, status) VALUES (6, 6, 1, 'confirmed');
COMMIT;

-- Transaction 2 : transfert d'inscription
START TRANSACTION;
UPDATE EventRegistration SET status = 'cancelled' WHERE member_id = 2 AND event_id = 3;
INSERT INTO EventRegistration(member_id, event_id, nbTickets, status)
VALUES (2, 7, 1, 'confirmed')
ON DUPLICATE KEY UPDATE status = 'confirmed', nbTickets = 1;
COMMIT;

-- Verification finale
SELECT * FROM vue_evenements_complets;