CREATE DATABASE IF NOT EXISTS artconnect;
USE artconnect;

CREATE TABLE IF NOT EXISTS Artist (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    bio           TEXT,
    website       VARCHAR(255),
    location      VARCHAR(100),
    profileImageUrl VARCHAR(255),
    createdAt     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Artwork (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    artist_id   INT NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    imageUrl    VARCHAR(255),
    year        INT,
    medium      VARCHAR(50),
    createdAt   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_artwork_artist FOREIGN KEY (artist_id) REFERENCES Artist(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Event (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    artist_id   INT NOT NULL,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    startDate   DATETIME NOT NULL,
    endDate     DATETIME NOT NULL,
    location    VARCHAR(150) NOT NULL,
    eventType   VARCHAR(50),
    imageUrl    VARCHAR(255),
    capaciteMax INT NOT NULL DEFAULT 50,
    createdAt   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_event_artist FOREIGN KEY (artist_id) REFERENCES Artist(id) ON DELETE CASCADE,
    CONSTRAINT chk_dates CHECK (endDate > startDate)
);

CREATE TABLE IF NOT EXISTS Member (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    name         VARCHAR(100) NOT NULL,
    email        VARCHAR(100) NOT NULL UNIQUE,
    passwordHash VARCHAR(255) NOT NULL,
    role         VARCHAR(20)  DEFAULT 'member',
    bio          TEXT,
    joinDate     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS EventRegistration (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    member_id        INT NOT NULL,
    event_id         INT NOT NULL,
    registrationDate DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status           VARCHAR(20) DEFAULT 'confirmed',
    nbTickets        INT NOT NULL DEFAULT 1,
    CONSTRAINT fk_reg_member FOREIGN KEY (member_id) REFERENCES Member(id) ON DELETE CASCADE,
    CONSTRAINT fk_reg_event  FOREIGN KEY (event_id)  REFERENCES Event(id)  ON DELETE CASCADE,
    CONSTRAINT uq_reg UNIQUE (member_id, event_id)
);

CREATE TABLE IF NOT EXISTS Follow (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    member_id  INT NOT NULL,
    artist_id  INT NOT NULL,
    followedAt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_follow_member FOREIGN KEY (member_id) REFERENCES Member(id) ON DELETE CASCADE,
    CONSTRAINT fk_follow_artist FOREIGN KEY (artist_id) REFERENCES Artist(id) ON DELETE CASCADE,
    CONSTRAINT uq_follow UNIQUE (member_id, artist_id)
);

CREATE TABLE IF NOT EXISTS `Like` (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    member_id  INT NOT NULL,
    artwork_id INT NOT NULL,
    likedAt    DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_like_member  FOREIGN KEY (member_id)  REFERENCES Member(id)  ON DELETE CASCADE,
    CONSTRAINT fk_like_artwork FOREIGN KEY (artwork_id) REFERENCES Artwork(id) ON DELETE CASCADE,
    CONSTRAINT uq_like UNIQUE (member_id, artwork_id)
);
