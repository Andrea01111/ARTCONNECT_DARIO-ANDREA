package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.ArtistService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbArtistService implements ArtistService {

    // Le Service utilise le DAO pour communiquer avec la base
    private final ArtistDao artistDao;

    // Le constructeur demande le DAO
    public DbArtistService(ArtistDao artistDao) {
        this.artistDao = artistDao;
    }

    @Override
    public List<Artist> getAllArtists() {
        return artistDao.findAll(); // Appel direct à la base de données
    }

    @Override
    public Optional<Artist> getArtistByName(String name) {
        // On récupère tous les artistes et on cherche celui qui correspond
        return artistDao.findAll().stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public void createArtist(Artist artist) {
        artistDao.save(artist); // Sauvegarde en base
    }

    @Override
    public void updateArtist(Artist artist) {
        artistDao.update(artist); // Mise à jour en base
    }

    @Override
    public void deleteArtist(String name) {
        artistDao.delete(name); // Suppression en base
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        // Comme nous avons supprimé la table Discipline (Normalisation 1FN), 
        // on retourne une liste fixe pour ne pas faire planter les menus de l'interface graphique.
        List<Discipline> disciplines = new ArrayList<>();
        disciplines.add(new Discipline("Painting"));
        disciplines.add(new Discipline("Sculpture"));
        disciplines.add(new Discipline("Photography"));
        disciplines.add(new Discipline("Digital Art"));
        disciplines.add(new Discipline("Music"));
        return disciplines;
    }

    @Override
    public List<Artist> searchArtists(String query, String disciplineName, String city) {
        if (query != null && !query.trim().isEmpty()) {
            return artistDao.searchFullText(query.trim());
        }
        if (city != null && !city.isEmpty()) {
            return artistDao.findByCity(city);
        }
        return artistDao.findAll();
    }
}