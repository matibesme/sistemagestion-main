package com.globallogic.sistema.gestion.model;

public enum Roles {
    ALUMNO,
    PROFESOR,
    ADMIN;

    private static final java.util.Set<String> VALIDOS = java.util.stream.Stream.of(Roles.values())
            .map(Enum::name)
            .collect(java.util.stream.Collectors.toSet());

    public static boolean isValid(String role) {
        return role != null && VALIDOS.contains(role);
    }
}
