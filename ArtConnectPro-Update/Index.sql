-- ============================================================
-- INDEX
-- ============================================================
USE artconnect;

CREATE INDEX idx_artwork_artist ON Artwork(artist_id);
CREATE INDEX idx_event_artist   ON Event(artist_id);
CREATE INDEX idx_reg_member     ON EventRegistration(member_id);
CREATE INDEX idx_reg_event      ON EventRegistration(event_id);
CREATE INDEX idx_follow_member  ON Follow(member_id);
CREATE INDEX idx_like_artwork   ON `Like`(artwork_id);

ALTER TABLE Artist  ADD FULLTEXT INDEX idx_ft_artist  (name, bio, location);
ALTER TABLE Artwork ADD FULLTEXT INDEX idx_ft_artwork (title, description);