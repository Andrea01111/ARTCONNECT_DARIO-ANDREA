package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtworkService;

import java.util.List;
import java.util.Optional;

public class DbArtworkService implements ArtworkService {

    private final ArtworkDao artworkDao;

    // Le constructeur demande le DAO des oeuvres
    public DbArtworkService(ArtworkDao artworkDao) {
        this.artworkDao = artworkDao;
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return artworkDao.findAll(); // Appel direct à la base de données
    }

    @Override
    public Optional<Artwork> getArtworkByTitle(String title) {
        // Comme pour les artistes, on filtre via les streams Java 
        // (très efficace pour retrouver une oeuvre par son titre)
        return artworkDao.findAll().stream()
                .filter(a -> a.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        if (artist == null || artist.getName() == null) {
            return List.of(); // Retourne une liste vide si l'artiste n'est pas valide
        }
        // On utilise la méthode spécifique de notre JdbcArtworkDao
        return artworkDao.findByArtistName(artist.getName());
    }

    @Override
    public void createArtwork(Artwork artwork) {
        artworkDao.save(artwork);
    }

    @Override
    public void updateArtwork(Artwork artwork) {
        artworkDao.update(artwork);
    }

    @Override
    public void deleteArtwork(String title) {
        artworkDao.delete(title);
    }
}