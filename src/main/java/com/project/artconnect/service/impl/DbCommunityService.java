package com.project.artconnect.service.impl;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.service.CommunityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DbCommunityService implements CommunityService {

    private final CommunityMemberDao communityMemberDao;


    public DbCommunityService(CommunityMemberDao communityMemberDao) {
        this.communityMemberDao = communityMemberDao;
    }

    @Override
    public List<CommunityMember> getAllMembers() {
        return communityMemberDao.findAll(); // Appel direct à la BDD
    }

    @Override
    public Optional<CommunityMember> getMemberByName(String name) {
        // Recherche du membre par son nom via les streams Java
        return communityMemberDao.findAll().stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Review> getReviewsByMember(CommunityMember member) {

        return new ArrayList<>(); 
    }
}