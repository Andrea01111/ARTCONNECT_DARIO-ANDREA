USE artconnect;

INSERT INTO Artist (name, bio, website, location, profileImageUrl, createdAt) VALUES
('Clara Voss',    'Peintre expressionniste passionnée par les paysages urbains.',      'https://claravoss.art',      'Paris, France',     'https://img.artconnect.fr/artists/clara.jpg',  '2023-03-10 09:00:00'),
('Marc Lefevre',  'Musicien jazz et compositeur indépendant depuis 15 ans.',            'https://marclefevre.music',  'Lyon, France',      'https://img.artconnect.fr/artists/marc.jpg',   '2023-04-05 10:30:00'),
('Sofia Martins', 'Photographe documentaire, primée au festival Visa pour lImage.',    'https://sofiamartins.photo', 'Bordeaux, France',  'https://img.artconnect.fr/artists/sofia.jpg',  '2023-05-12 14:00:00'),
('Kenji Tanaka',  'Sculpteur céramiste, inspiré par le wabi-sabi japonais.',            'https://kenjitanaka.com',    'Toulouse, France',  'https://img.artconnect.fr/artists/kenji.jpg',  '2023-06-01 11:00:00'),
('Amina Diallo',  'Artiste textile et créatrice de fresques participatives.',            'https://aminadiallo.art',    'Marseille, France', 'https://img.artconnect.fr/artists/amina.jpg',  '2023-07-20 16:00:00'),
('Lucas Bernard', 'Illustrateur numérique et auteur de BD indépendante.',               'https://lucasbernard.io',    'Nantes, France',    'https://img.artconnect.fr/artists/lucas.jpg',  '2023-09-08 08:45:00');

INSERT INTO Artwork (artist_id, title, description, imageUrl, year, medium, createdAt) VALUES
(1, 'Nuit Blanche',       'Série de peintures nocturnes sur les boulevards parisiens.', 'https://img.artconnect.fr/works/nuit_blanche.jpg',  2022, 'Huile sur toile',        '2023-03-15 10:00:00'),
(1, 'Lumière Dorée',      'Jeu de lumière au coucher du soleil sur la Seine.',          'https://img.artconnect.fr/works/lumiere_doree.jpg', 2023, 'Acrylique sur toile',   '2023-08-01 09:00:00'),
(2, 'Improv No. 7',       'Composition jazz pour trio enregistrée en live.',            'https://img.artconnect.fr/works/improv7.jpg',       2021, 'Enregistrement audio',  '2023-04-10 11:00:00'),
(3, 'Mémoires de Marché', 'Portraits de vendeurs du marché des Capucins.',              'https://img.artconnect.fr/works/marche.jpg',        2022, 'Photographie',          '2023-05-20 14:30:00'),
(3, 'Silences Urbains',   'Série de 20 tirages sur la solitude en ville.',              'https://img.artconnect.fr/works/silences.jpg',      2023, 'Photographie',          '2023-09-05 10:00:00'),
(4, 'Bol Fendu',          'Céramique inspirée du kintsugi, réparée à lor.',             'https://img.artconnect.fr/works/bol_fendu.jpg',     2022, 'Céramique',             '2023-06-10 15:00:00'),
(4, 'Vase Asymétrique',   'Vase en grès aux formes organiques.',                        'https://img.artconnect.fr/works/vase.jpg',          2023, 'Céramique',             '2023-10-01 10:00:00'),
(5, 'Fresque Solidaire',  'Fresque murale réalisée avec les habitants du quartier.',    'https://img.artconnect.fr/works/fresque.jpg',       2023, 'Textile et peinture',   '2023-07-25 09:00:00'),
(6, 'Chroniques du Métro','BD courte sur les tranches de vie dans le métro parisien.',  'https://img.artconnect.fr/works/metro_bd.jpg',      2022, 'Illustration numérique','2023-09-15 11:00:00'),
(6, 'Pixel Dreams',       'Série illustrations oniriques générées et retravaillées.',   'https://img.artconnect.fr/works/pixel_dreams.jpg', 2023, 'Illustration numérique','2023-11-01 09:00:00');

INSERT INTO Event (artist_id, title, description, startDate, endDate, location, eventType, imageUrl, capaciteMax, createdAt) VALUES
(1, 'Vernissage Nuit Blanche',    'Exposition de la série Nuit Blanche avec présence de lartiste.', '2024-02-10 18:00:00', '2024-02-10 22:00:00', 'Galerie Lumière, Paris',        'exposition', 'https://img.artconnect.fr/events/vernissage.jpg',  40, '2024-01-05 10:00:00'),
(1, 'Atelier Peinture Urbaine',   'Atelier pratique : peindre Paris à la manière de Clara Voss.',   '2024-03-15 14:00:00', '2024-03-15 17:00:00', 'Studio Clara, Paris',           'atelier',    'https://img.artconnect.fr/events/atelier_p.jpg',   15, '2024-02-01 09:00:00'),
(2, 'Concert Jazz Improv',        'Concert live avec le trio de Marc Lefevre, entrée libre.',        '2024-02-24 20:00:00', '2024-02-24 23:00:00', 'Le Hot Club, Lyon',             'concert',    'https://img.artconnect.fr/events/jazz.jpg',        80, '2024-01-20 11:00:00'),
(2, 'Masterclass Composition',    'Masterclass pour musiciens amateurs sur la composition jazz.',    '2024-04-06 10:00:00', '2024-04-06 13:00:00', 'Conservatoire de Lyon',         'atelier',    'https://img.artconnect.fr/events/masterclass.jpg', 20, '2024-02-15 14:00:00'),
(3, 'Expo Silences Urbains',      'Exposition photographique dans une galerie bordelaise.',          '2024-03-01 10:00:00', '2024-03-20 18:00:00', 'Galerie Arc-en-Ciel, Bordeaux', 'exposition', 'https://img.artconnect.fr/events/expo_sofia.jpg',  60, '2024-01-25 09:00:00'),
(4, 'Démonstration Céramique',    'Démonstration publique de tournage et de cuisson au raku.',       '2024-04-20 14:00:00', '2024-04-20 17:00:00', 'Atelier Terre, Toulouse',       'atelier',    'https://img.artconnect.fr/events/ceramique.jpg',   25, '2024-03-01 10:00:00'),
(5, 'Fresque Participative',      'Création collective une fresque textile en plein air.',           '2024-05-11 10:00:00', '2024-05-11 16:00:00', 'Place du Marché, Marseille',    'atelier',    'https://img.artconnect.fr/events/fresque_ev.jpg',  50, '2024-03-10 09:00:00'),
(6, 'Dédicace BD & Illustration', 'Séance de dédicaces et exposition des planches originales.',      '2024-06-08 14:00:00', '2024-06-08 18:00:00', 'Librairie Bulles, Nantes',      'exposition', 'https://img.artconnect.fr/events/dedicace.jpg',    30, '2024-04-01 11:00:00');

INSERT INTO Member (name, email, passwordHash, role, bio, joinDate) VALUES
('Alice Dupont',  'alice.dupont@mail.com',  '$2b$12$AAA', 'member',    'Passionnée dart contemporain et de photographie.',  '2023-10-01 08:00:00'),
('Bruno Garcia',  'bruno.garcia@mail.com',  '$2b$12$BBB', 'member',    'Amateur de jazz et de musique du monde.',           '2023-10-15 09:00:00'),
('Chloé Martin',  'chloe.martin@mail.com',  '$2b$12$CCC', 'member',    'Artiste amateur, aime les ateliers pratiques.',     '2023-11-01 10:00:00'),
('David Nguyen',  'david.nguyen@mail.com',  '$2b$12$DDD', 'member',    'Photographe amateur, collectionneur de tirages.',   '2023-11-20 11:00:00'),
('Emma Rousseau', 'emma.rousseau@mail.com', '$2b$12$EEE', 'member',    'Céramiste débutante, très intéressée par le raku.', '2024-01-05 14:00:00'),
('Félix Lambert', 'felix.lambert@mail.com', '$2b$12$FFF', 'member',    'Fan de BD et illustration numérique.',              '2024-01-20 16:00:00'),
('Grace Petit',   'grace.petit@mail.com',   '$2b$12$GGG', 'organizer', 'Organisatrice culturelle indépendante.',            '2023-09-01 09:00:00'),
('Hugo Bernard',  'hugo.bernard@mail.com',  '$2b$12$HHH', 'member',    'Etudiant en histoire de lart.',                    '2024-02-10 10:00:00');

INSERT INTO EventRegistration (member_id, event_id, registrationDate, status, nbTickets) VALUES
(1, 1, '2024-01-20 10:00:00', 'confirmed', 1),
(1, 5, '2024-02-01 11:00:00', 'confirmed', 1),
(2, 3, '2024-01-25 09:00:00', 'confirmed', 2),
(2, 4, '2024-02-20 14:00:00', 'confirmed', 1),
(3, 2, '2024-02-05 10:00:00', 'confirmed', 1),
(3, 6, '2024-03-05 11:00:00', 'confirmed', 1),
(4, 5, '2024-02-02 09:00:00', 'confirmed', 1),
(4, 1, '2024-01-22 10:00:00', 'confirmed', 1),
(5, 6, '2024-03-10 15:00:00', 'confirmed', 1),
(5, 7, '2024-03-15 09:00:00', 'confirmed', 2),
(6, 8, '2024-04-05 10:00:00', 'confirmed', 1),
(7, 1, '2024-01-10 08:00:00', 'confirmed', 1),
(8, 3, '2024-01-28 11:00:00', 'confirmed', 1),
(8, 2, '2024-02-08 14:00:00', 'confirmed', 1);

INSERT INTO Follow (member_id, artist_id, followedAt) VALUES
(1, 1, '2023-10-05 10:00:00'),
(1, 3, '2023-10-10 11:00:00'),
(2, 2, '2023-10-20 09:00:00'),
(3, 1, '2023-11-05 14:00:00'),
(3, 4, '2023-11-10 15:00:00'),
(4, 3, '2023-11-25 10:00:00'),
(5, 4, '2024-01-10 11:00:00'),
(6, 6, '2024-01-25 09:00:00'),
(7, 5, '2023-09-10 10:00:00'),
(8, 2, '2024-02-12 11:00:00'),
(8, 6, '2024-02-15 14:00:00');

INSERT INTO `Like` (member_id, artwork_id, likedAt) VALUES
(1, 1,  '2023-10-06 10:00:00'),
(1, 4,  '2023-10-12 11:00:00'),
(2, 3,  '2023-10-22 09:00:00'),
(3, 1,  '2023-11-06 14:00:00'),
(3, 6,  '2023-11-12 15:00:00'),
(4, 4,  '2023-11-26 10:00:00'),
(4, 5,  '2023-12-01 11:00:00'),
(5, 6,  '2024-01-11 11:00:00'),
(5, 7,  '2024-01-15 10:00:00'),
(6, 9,  '2024-01-26 09:00:00'),
(6, 10, '2024-01-28 14:00:00'),
(7, 8,  '2023-09-11 10:00:00'),
(8, 3,  '2024-02-13 11:00:00'),
(8, 2,  '2024-02-16 14:00:00');