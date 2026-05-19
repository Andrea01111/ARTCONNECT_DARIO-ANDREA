-- ============================================================
-- TRIGGERS
-- ============================================================
USE artconnect;

DELIMITER $$

CREATE TRIGGER trg_check_capacite
BEFORE INSERT ON EventRegistration FOR EACH ROW
BEGIN
    DECLARE totalInscrits INT;
    DECLARE maxCap INT;
    SELECT COALESCE(SUM(nbTickets),0) INTO totalInscrits
      FROM EventRegistration
     WHERE event_id = NEW.event_id AND status = 'confirmed';
    SELECT capaciteMax INTO maxCap FROM Event WHERE id = NEW.event_id;
    IF (totalInscrits + NEW.nbTickets) > maxCap THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Capacite maximale atteinte.';
    END IF;
END$$

CREATE TRIGGER trg_check_dates_event
BEFORE INSERT ON Event FOR EACH ROW
BEGIN
    IF NEW.endDate <= NEW.startDate THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'La date de fin doit etre posterieure a la date de debut.';
    END IF;
END$$


CREATE TRIGGER trg_limit_tickets
BEFORE INSERT ON EventRegistration FOR EACH ROW
BEGIN
    IF NEW.nbTickets > 4 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Achat limite a 4 tickets maximum par personne.';
    END IF;
END$$

CREATE TRIGGER trg_check_nbtickets
BEFORE INSERT ON EventRegistration FOR EACH ROW
BEGIN
    IF NEW.nbTickets <= 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Le nombre de tickets doit etre superieur a 0.';
    END IF;
END$$

DELIMITER ;