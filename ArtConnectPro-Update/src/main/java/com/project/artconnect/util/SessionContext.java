package com.project.artconnect.util;

import com.project.artconnect.model.CommunityMember;

public class SessionContext {
    private static CommunityMember currentUser = null;

    public static void login(CommunityMember user) { currentUser = user; }
    public static void logout() { currentUser = null; }
    public static CommunityMember getCurrentUser() { return currentUser; }
    public static boolean isLoggedIn() { return currentUser != null; }
    public static boolean hasRole(String role) {
        return currentUser != null && role.equals(currentUser.getRole());
    }
}
