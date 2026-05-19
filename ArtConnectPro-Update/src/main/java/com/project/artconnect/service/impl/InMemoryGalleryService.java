package com.project.artconnect.service.impl;

import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.service.GalleryService;
import com.project.artconnect.service.ArtworkService;
import java.util.*;

public class InMemoryGalleryService implements GalleryService {
    private final Map<String, Gallery> galleries = new LinkedHashMap<>();

    public InMemoryGalleryService() {
        // On initialise juste quelques galeries "en dur" pour l'UI 
        // sans chercher à les lier aux artworks pour l'instant
        addGallery("Galerie Lumière", "Paris", 4.8);
        addGallery("Le Hot Club", "Lyon", 4.5);
        addGallery("Atelier Terre", "Toulouse", 4.7);
    }

    // On vide cette méthode pour éviter le NullPointerException
    public void initData(ArtworkService artworkService) {
        // Laisser vide pour la version BDD
    }

    private void addGallery(String name, String address, double rating) {
        galleries.put(name, new Gallery(name, address, rating));
    }

    @Override
    public List<Gallery> getAllGalleries() {
        return new ArrayList<>(galleries.values());
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return Optional.ofNullable(galleries.get(name));
    }

    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        throw new UnsupportedOperationException("Unimplemented method 'getExhibitionsByGallery'");
    }
}