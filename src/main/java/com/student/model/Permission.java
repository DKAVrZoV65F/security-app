package com.student.model;

public class Permission {
    private String resourceName;
    private Role role;
    private boolean canRead;
    private boolean canEdit;
    private boolean canDelete;

    public Permission(String resourceName, Role role, boolean canRead, boolean canEdit, boolean canDelete) {
        this.resourceName = resourceName;
        this.role = role;
        this.canRead = canRead;
        this.canEdit = canEdit;
        this.canDelete = canDelete;
    }

    public String getResourceName() { return resourceName; }
    public Role getRole() { return role; }
    public boolean canRead() { return canRead; }
    public boolean canEdit() { return canEdit; }
    public boolean canDelete() { return canDelete; }

    public void setCanRead(boolean canRead) { this.canRead = canRead; }
    public void setCanEdit(boolean canEdit) { this.canEdit = canEdit; }
    public void setCanDelete(boolean canDelete) { this.canDelete = canDelete; }
}
