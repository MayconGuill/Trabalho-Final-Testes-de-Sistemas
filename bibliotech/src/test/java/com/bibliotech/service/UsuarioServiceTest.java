package com.bibliotech.service;

import com.bibliotech.commons.UserUtils;
import com.bibliotech.model.Emprestimo;
import com.bibliotech.model.Usuario;
import com.bibliotech.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository repository;

    private UserUtils userUtils;

    @BeforeEach
    void init() {
        userUtils = new UserUtils();
    }

    @Test
    void DeveCadastrarUmUsuarioComSucesso() {
        var usuario = userUtils.userCriado();

        when(repository.save(usuario)).thenReturn(usuario);

        var usuarioSalvo = service.salvar(usuario);

        assertThat(usuarioSalvo).isNotNull().isEqualTo(usuario);
    }

    @Test
    void OCampoNomeEObrigatorioParaCadastro() {
        var usuario = new Usuario();
        usuario.setCpf("000.000.000-00");
        usuario.setEmail("email@email.com");
        usuario.setTipo(Usuario.TipoUsuario.ALUNO);
        usuario.setSenha("teste123");

        assertThatThrownBy(() -> service.salvar(usuario))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void LancaUmaExceptionComCPFInvalido() {
        var cpfInvalido = userUtils.usuarioComCpfInvalido();

        assertThatThrownBy(() -> service.salvar(cpfInvalido))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("CPF inválido");

        verify(repository, never()).save(cpfInvalido);
    }

    @Test
    void LancaUmaExceptionComCPFSemFormato() {
        var cpfInvalido = userUtils.usuarioComCpfNaoFormatado();

        assertThatThrownBy(() -> service.salvar(cpfInvalido))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("CPF inválido");

        verify(repository, never()).save(cpfInvalido);
    }

    @Test
    void LancaUmaExceptionComEmailJaCadastrado() {
        var usuario = userUtils.userCriado();
        var email = "admin@bibliotech.com";

        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> service.salvar(usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Email já cadastrado");

        verify(repository, never()).save(usuario);
    }

    @Test
    void LancaUmaExceptionComCpfJaCadastrado() {
        var usuario = userUtils.userCriado();
        var cpf = "000.000.000-00";

        when(repository.findByEmail(cpf)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> service.salvar(usuario))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("CPF já cadastrado");

        verify(repository, never()).save(usuario);
    }

    @Test
    void UsuarioCadastradoAutenticadoDuranteLogin_ComSucesso() {
        var usuario = userUtils.userCriado();
        var email = usuario.getEmail();
        var senha = usuario.getSenha();

        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        var usuarioSalvo = service.autenticar(email, senha);

        assertThat(usuarioSalvo).isNotNull().isEqualTo(usuario);
    }

    @Test
    void UsuarioNaoLocalizadoDuranteLogin_DeveRetornarEmpty() {
        var email = "admin@bibliotech.com";
        var senha = "admin123";

        when(repository.findByEmail(email)).thenReturn(Optional.empty());

        var usuarioSalvo = service.autenticar(email, senha);

        assertThat(usuarioSalvo).isEmpty();
    }

    @Test
    void UsuariosComEmprestimoAtivoNaoPodemSerExcluidos() {
        var usuario = userUtils.userCriado();
        usuario.setEmprestimos(List.of(new Emprestimo()));

        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> service.excluir(usuario.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Não é possível excluir usuário com empréstimos ativos");
    }

    @Test
    void UsuarioSemEmprestimoPodeSerExcluido() {
        var usuario = userUtils.userCriado();

        when(repository.findById(usuario.getId())).thenReturn(Optional.of(usuario));
        doNothing().when(repository).delete(usuario);

        service.excluir(usuario.getId());

        verify(repository).delete(usuario);
    }

    @Test
    void DeveRetornarTrueSeOFormatoDoCpfForValido() {
        var cpf = "000.000.000-00";

        var cpfValido = service.validarCPF(cpf);

        assertThat(cpfValido).isTrue();
    }

    @Test
    void RetornaFalseSeOFormatoDoCpfForInvalido() {
        var cpf = "000.000.00000";

        var cpfValido = service.validarCPF(cpf);

        assertThat(cpfValido).isFalse();
    }

    @Test
    void ApenasUsuariosCadastradosComStatusTruePodemFazerLogin() {
        var usuario = userUtils.userCriado();
        usuario.setAtivo(false);
        var email = usuario.getEmail();
        var senha = usuario.getSenha();

        when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> service.autenticar(email, senha))
                .isInstanceOf(RuntimeException.class);
    }


}