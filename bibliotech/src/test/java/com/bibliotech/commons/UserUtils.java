package com.bibliotech.commons;

import com.bibliotech.model.Usuario;

public class UserUtils {
    public Usuario userCriado() {
        Usuario admin = new Usuario("Administrador", "admin@bibliotech.com",
                "000.000.000-00", "admin123", Usuario.TipoUsuario.FUNCIONARIO);

        return admin;
    }

    public Usuario userCriado2() {
        Usuario admin = new Usuario("Administrador 2", "admin@bibliotech.com",
                "100.000.000-00", "123admin", Usuario.TipoUsuario.FUNCIONARIO);

        return admin;
    }

    public Usuario usuarioComCpfInvalido() {
        Usuario admin = new Usuario("Administrador 2", "admin@bibliotech.com",
                "000.000.000-000", "admin123", Usuario.TipoUsuario.FUNCIONARIO);

        return admin;
    }

    public Usuario usuarioComCpfNaoFormatado() {
        Usuario admin = new Usuario("Administrador 2", "admin@bibliotech.com",
                "00000000000", "admin123", Usuario.TipoUsuario.FUNCIONARIO);

        return admin;
    }
}
