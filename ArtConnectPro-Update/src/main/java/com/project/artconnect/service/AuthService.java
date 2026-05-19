package com.project.artconnect.service;

import com.project.artconnect.model.CommunityMember;
import java.util.Optional;

public interface AuthService {
    Optional<CommunityMember> login(String email, String rawPassword);
    void logout();
}
