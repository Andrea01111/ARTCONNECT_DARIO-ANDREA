package com.project.artconnect.service.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.service.WorkshopService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbWorkshopService implements WorkshopService {

    private final WorkshopDao workshopDao;

    public DbWorkshopService(WorkshopDao workshopDao) {
        this.workshopDao = workshopDao;
    }

    @Override
    public List<Workshop> getAllWorkshops() {
        return workshopDao.findAll(); // Récupération depuis MySQL
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        // L'idéal ici serait d'appeler notre DAO pour exécuter sp_inscrire_membre().
        // Mais pour garder la compatibilité stricte avec l'UI fournie par l'école,
        // on maintient l'objet Booking en mémoire pour la session en cours.
        if (workshop != null && member != null) {
            Booking b = new Booking(workshop, member);
            member.addBooking(b);
        }
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        if (member == null) return new ArrayList<>();
        return member.getBookings();
    }
}