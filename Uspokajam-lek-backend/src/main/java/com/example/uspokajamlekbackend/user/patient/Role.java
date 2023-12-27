package com.example.uspokajamlekbackend.user.patient;

public enum Role {
    PATIENT("patient"),
    DOCTOR("doctor");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getName() {
        return roleName;
    }
}
