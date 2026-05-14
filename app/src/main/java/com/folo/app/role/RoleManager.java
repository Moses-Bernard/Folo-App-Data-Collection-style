package com.folo.app.role;

import android.content.Context;
import android.content.SharedPreferences;

public class RoleManager {
    private static final String PREFS_NAME = "folo_role_prefs";
    private static final String KEY_ROLE = "current_role";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_PIN_PREFIX = "pin_";
    private final SharedPreferences prefs;

    public RoleManager(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setRole(Role role, String userName) {
        prefs.edit().putString(KEY_ROLE, role.name()).putString(KEY_USER_NAME, userName).apply();
    }

    public Role getCurrentRole() {
        return Role.fromString(prefs.getString(KEY_ROLE, Role.SURVEILLANCE_OFFICER.name()));
    }

    public String getCurrentUser() {
        return prefs.getString(KEY_USER_NAME, "Unknown");
    }

    public boolean hasPermission(String permission) {
        return getCurrentRole().hasPermission(permission);
    }

    public void setPin(Role role, String pin) {
        prefs.edit().putString(KEY_PIN_PREFIX + role.name(), pin).apply();
    }

    public boolean hasPin(Role role) {
        return prefs.contains(KEY_PIN_PREFIX + role.name());
    }

    public boolean verifyPin(Role role, String pin) {
        String stored = prefs.getString(KEY_PIN_PREFIX + role.name(), "");
        return stored.equals(pin);
    }

    public void clearPin(Role role) {
        prefs.edit().remove(KEY_PIN_PREFIX + role.name()).apply();
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
