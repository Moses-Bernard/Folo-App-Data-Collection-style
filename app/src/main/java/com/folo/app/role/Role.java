package com.folo.app.role;

public enum Role {
    ADMIN("Admin", new String[]{"REGISTER", "EDIT", "DELETE", "VIEW_ALL", "SYNC", "SETTINGS", "EXPORT", "QUESTIONS"}),
    SURVEILLANCE_OFFICER("Surveillance Officer", new String[]{"REGISTER", "EDIT_OWN", "VIEW_OWN", "GPS_CAPTURE", "HEALTH_CHECK", "QUESTIONS"}),
    SUPERVISOR("Supervisor", new String[]{"VIEW_ALL", "EDIT", "APPROVE", "ALERTS", "REPORTS", "EXPORT"});

    private final String displayName;
    private final String[] permissions;

    Role(String displayName, String[] permissions) {
        this.displayName = displayName;
        this.permissions = permissions;
    }

    public String getDisplayName() { return displayName; }
    public String[] getPermissions() { return permissions; }

    public boolean hasPermission(String permission) {
        for (String p : permissions) {
            if (p.equals(permission) || p.equals("ALL")) return true;
        }
        return false;
    }

    public static Role fromString(String name) {
        for (Role r : values()) {
            if (r.displayName.equalsIgnoreCase(name) || r.name().equalsIgnoreCase(name)) return r;
        }
        return SURVEILLANCE_OFFICER;
    }
}
