package com.project.artconnect.service.impl;

import com.project.artconnect.dao.CommunityMemberDao;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.service.AuthService;
import com.project.artconnect.util.SessionContext;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

public class DbAuthService implements AuthService {

    private final CommunityMemberDao memberDao;

    public DbAuthService(CommunityMemberDao memberDao) {
        this.memberDao = memberDao;
    }

    @Override
    public Optional<CommunityMember> login(String email, String rawPassword) {
        String hash = sha256(rawPassword);
        Optional<CommunityMember> opt = memberDao.findByEmail(email);
        if (opt.isPresent() && hash.equals(opt.get().getPasswordHash())) {
            SessionContext.login(opt.get());
            return opt;
        }
        return Optional.empty();
    }

    @Override
    public void logout() {
        SessionContext.logout();
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 non disponible", e);
        }
    }
}
