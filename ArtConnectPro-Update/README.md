# ArtConnect Pro

Plateforme de gestion de communautés artistiques locales, développée en Java/JavaFX avec une base de données MySQL.

## Lancer l'application

Prérequis : Java 17+ et Maven installés.

```bash
mvn clean javafx:run
```

## Fonctionnalités ajoutées

1. **Recherche FULLTEXT** — Recherche avancée des artistes et œuvres par mots-clés via un index FULLTEXT MySQL (MATCH ... AGAINST en mode booléen), avec tri des résultats par pertinence.

2. **Authentification avec rôles** — Connexion sécurisée par email et mot de passe (hashé en SHA-256), gestion des rôles utilisateur via une procédure stockée MySQL (`sp_authenticate`) et un contexte de session (`SessionContext`).

3. **Notifications J-3** — Génération automatique de notifications pour les membres ayant un événement dans 3 jours, via un événement planifié MySQL (`evt_notif_j3`). Les notifications non lues s'affichent dans l'interface principale.
