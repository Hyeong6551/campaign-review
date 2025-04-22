package edu.du.gatewayservice.entity;

public enum Role {
    USER("사용자"),
    BUSINESS("사업자"),
    ADMIN("관리자");

    private final String role_name;
    Role(String role_name) {
        this.role_name = role_name;
    }

    public String value(){
        return role_name;
    }
}
