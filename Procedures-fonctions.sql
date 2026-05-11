-- ============================================================
-- FONCTIONS
-- ============================================================
USE artconnect;

DELIMITER $$

CREATE FUNCTION fn_nb_participants(p_event_id INT)
RETURNS INT DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE total INT;
    SELECT COALESCE(SUM(nbTickets), 0) INTO total
      FROM EventRegistration
     WHERE event_id = p_event_id AND status = 'confirmed';
    RETURN total;
END$$

CREATE FUNCTION fn_event_est_complet(p_event_id INT)
RETURNS BOOLEAN DETERMINISTIC READS SQL DATA
BEGIN
    DECLARE inscrits INT;
    DECLARE maxCap INT;
    SET inscrits = fn_nb_participants(p_event_id);
    SELECT capaciteMax INTO maxCap FROM Event WHERE id = p_event_id;
    RETURN inscrits >= maxCap;
END$$


-- ============================================================
-- PROCEDURES
-- ============================================================


CREATE PROCEDURE sp_inscrire_membre(
    IN p_member_id INT,
    IN p_event_id  INT,
    IN p_nbTickets INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN 
		ROLLBACK; 
		RESIGNAL; 
    END;
    START TRANSACTION;
    INSERT INTO EventRegistration(member_id, event_id, nbTickets, status)
    VALUES (p_member_id, p_event_id, p_nbTickets, 'confirmed');
    COMMIT;
END$$



CREATE PROCEDURE sp_like_et_suivre_artiste(
    IN p_member_id INT,
    IN p_artwork_id INT
)
BEGIN
    DECLARE v_artist_id INT;
    
    -- Gestionnaire d'erreur pour annuler la transaction en cas de probleme
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN 
        ROLLBACK; 
        RESIGNAL; 
    END;
    
    -- 1. Trouver l'artiste qui a cree l'oeuvre
    SELECT artist_id INTO v_artist_id FROM Artwork WHERE id = p_artwork_id;
    START TRANSACTION;
    -- 2. Ajouter le "Like"
    INSERT IGNORE INTO `Like`(member_id, artwork_id) 
    VALUES (p_member_id, p_artwork_id);
    -- 3. Abonner le membre a l'artiste (INSERT IGNORE evite les erreurs si deja abonne)
    INSERT IGNORE INTO Follow(member_id, artist_id) 
    VALUES (p_member_id, v_artist_id);
    
    COMMIT;
END$$


CREATE PROCEDURE sp_annuler_inscriptions_membre(
    IN p_member_id INT
)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN 
		ROLLBACK; 
		RESIGNAL; 
    END;
    
    START TRANSACTION;
    UPDATE EventRegistration er
    JOIN   Event e ON e.id = er.event_id
    SET    er.status = 'cancelled'
    WHERE  er.member_id = p_member_id
      AND  e.startDate  > NOW()
      AND  er.status    = 'confirmed';
    COMMIT;
END$$

DELIMITER ;