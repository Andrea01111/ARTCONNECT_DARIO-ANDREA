-- ============================================================
-- DEMO
-- ============================================================
USE artconnect;

-- Voir tous les evenements avec places restantes
SELECT * FROM vue_evenements_complets;

-- Voir les stats de chaque artiste
SELECT * FROM vue_artistes_stats;

-- Voir les inscriptions actives de tous les membres
SELECT * FROM vue_inscriptions_membres;

-- Nombre de participants a l'evenement 1
SELECT fn_nb_participants(1) AS nb_participants;

-- L'evenement 2 est-il complet ?
SELECT fn_event_est_complet(2) AS est_complet;

-- Inscrire le membre 5 a l'evenement 3
CALL sp_inscrire_membre(5, 3, 1);

-- TEST NOUVELLE PROCEDURE : Le membre 4 (David) "like" l'oeuvre 1 (de Clara Voss) 
-- et s'abonne automatiquement a elle
CALL sp_like_et_suivre_artiste(4, 1);

-- Verifier que le like a ete ajoute et que le membre suit l'artiste 1
SELECT * FROM `Like` WHERE member_id = 4 AND artwork_id = 1;
SELECT * FROM Follow WHERE member_id = 4 AND artist_id = 1;

-- Test trigger dates invalides — doit retourner une erreur
INSERT INTO Event(artist_id, title, description, startDate, endDate, location, capaciteMax)
VALUES (1, 'Test dates invalides', 'test', '2024-08-10 20:00:00', '2024-08-10 18:00:00', 'Paris', 50);

-- TEST NOUVEAU TRIGGER LIMIT : tenter d'acheter plus de 4 billets (doit echouer)
INSERT INTO EventRegistration(member_id, event_id, nbTickets, status) VALUES (2, 1, 5, 'confirmed');

-- Test trigger capacite — remplir l'atelier (capacite=15) puis tenter une inscription de trop
-- (l'atelier Peinture Urbaine id=2 a 15 places, deja 2 inscrits)
INSERT INTO EventRegistration(member_id, event_id, nbTickets, status) VALUES (3, 2, 13, 'confirmed');
-- Cette insertion doit echouer car capacite max atteinte
INSERT INTO EventRegistration(member_id, event_id, nbTickets, status) VALUES (4, 2, 1, 'confirmed');

-- Annuler toutes les inscriptions futures du membre 3
CALL sp_annuler_inscriptions_membre(3);
SELECT * FROM vue_inscriptions_membres WHERE member_id = 3;