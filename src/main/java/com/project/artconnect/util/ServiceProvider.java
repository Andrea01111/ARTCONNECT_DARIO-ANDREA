package com.project.artconnect.util;

import com.project.artconnect.service.*;
import com.project.artconnect.service.impl.*;
import com.project.artconnect.persistence.*;

/**
 * Service Provider to manage singleton instances of services and handle their
 * initialization.
 */
public class ServiceProvider {
    
    // =========================================================================
    // NOUVELLE ARCHITECTURE : CONNEXION À LA BASE DE DONNÉES MYSQL
    // =========================================================================
    private static final ArtistService artistService       = new DbArtistService(new JdbcArtistDao());
    private static final ArtworkService artworkService     = new DbArtworkService(new JdbcArtworkDao());
    private static final WorkshopService workshopService   = new DbWorkshopService(new JdbcWorkshopDao());
    private static final CommunityService communityService = new DbCommunityService(new JdbcCommunityMemberDao());

    // On conserve le service en mémoire uniquement pour les Galeries (optimisation MCD)
    private static final InMemoryGalleryService galleryService = new InMemoryGalleryService();

    static {

       // On ne fait plus rien ici, les services Db sont autonomes
    }

    public static ArtistService getArtistService() {
        return artistService;
    }

    public static ArtworkService getArtworkService() {
        return artworkService;
    }

    public static GalleryService getGalleryService() {
        return galleryService;
    }

    public static WorkshopService getWorkshopService() {
        return workshopService;
    }

    public static CommunityService getCommunityService() {
        return communityService;
    }
}