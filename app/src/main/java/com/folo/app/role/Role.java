package com.folo.app.role;

public enum Role {
    ADMIN("Admin", new String[]{"register", "edit", "delete", "view_all", "sync", "settings", "export", "approve", "alerts", "reports"}),
    SURVEILLANCE_OFFICER("Surveillance Officer", new String[]{"register", "edit_own", "gps_capture", "health_check", "export", "view_list", "sync", "questions"}),
    SUPERVISOR("Supervisor", new String[]{"view_all", "edit", "approve", "alerts", "reports", "export"});

    private final String displayName;
    private final String[] permissions;

    Role(String displayName, String[] permissions) {
        this.displayName = displayName;
        this.permissions = permissions;
    }

    public String getDisplayName() { return displayName; }

    public boolean hasPermission(String permission) {
        for (String p : permissions) {
            if (p.equals(permission)) return true;
        }
        return false;
    }

    public static Role fromString(String name) {
        for (Role r : values()) {
            if (r.getDisplayName().equalsIgnoreCase(name) || r.name().equalsIgnoreCase(name)) {
                return r;
            }
        }
        return SURVEILLANCE_OFFICER;
    }
}
