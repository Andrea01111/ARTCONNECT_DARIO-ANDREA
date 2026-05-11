-- ============================================================
-- VUES
-- ============================================================
USE artconnect;

CREATE OR REPLACE VIEW vue_evenements_complets AS
SELECT
    e.id, e.title, e.eventType, e.startDate, e.location, e.capaciteMax,
    COALESCE(SUM(er.nbTickets), 0)                 AS nbInscrits,
    e.capaciteMax - COALESCE(SUM(er.nbTickets), 0) AS placesRestantes,
    a.name                                          AS artisteName
FROM Event e
JOIN Artist a ON a.id = e.artist_id
LEFT JOIN EventRegistration er ON er.event_id = e.id AND er.status = 'confirmed'
GROUP BY e.id, e.title, e.eventType, e.startDate, e.location, e.capaciteMax, a.name;


CREATE OR REPLACE VIEW vue_artistes_stats AS
SELECT
    a.id, a.name, a.location,
    COUNT(DISTINCT aw.id) AS nbOeuvres,
    COUNT(DISTINCT f.id)  AS nbFollowers
FROM Artist a
LEFT JOIN Artwork aw ON aw.artist_id = a.id
LEFT JOIN Follow  f  ON f.artist_id  = a.id
GROUP BY a.id;



CREATE OR REPLACE VIEW vue_inscriptions_membres AS
SELECT
    m.id AS member_id, m.name AS memberName,
    e.id AS event_id, e.title, e.startDate, e.location,
    er.status, er.nbTickets
FROM EventRegistration er
JOIN Member m ON m.id = er.member_id
JOIN Event  e ON e.id = er.event_id
WHERE er.status = 'confirmed';


