USE artconnect;

SET GLOBAL event_scheduler = ON;

DELIMITER $$

CREATE EVENT IF NOT EXISTS evt_notif_j3
ON SCHEDULE EVERY 1 DAY
STARTS CURRENT_TIMESTAMP
DO
BEGIN
    INSERT IGNORE INTO Notification (member_id, event_id, message)
    SELECT er.member_id,
           e.id,
           CONCAT('Rappel : "', e.title, '" a lieu dans 3 jours !')
    FROM Event e
    JOIN EventRegistration er ON er.event_id = e.id AND er.status = 'confirmed'
    WHERE DATE(e.startDate) = DATE(NOW() + INTERVAL 3 DAY);
END$$

DELIMITER ;
