package com.bibliotech.service;

import com.bibliotech.commons.EmprestimoUtils;
import com.bibliotech.commons.LivroUtils;
import com.bibliotech.commons.UserUtils;
import com.bibliotech.model.Emprestimo;
import com.bibliotech.model.Livro;
import com.bibliotech.model.Usuario;
import com.bibliotech.repository.EmprestimoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoServiceTest {
    @InjectMocks
    @Spy
    private EmprestimoService service;

    @Mock
    private EmprestimoRepository repository;

    @Mock
    private LivroService livroService;

    private EmprestimoUtils emprestimoUtils;
    private UserUtils userUtils;
    private LivroUtils livroUtils;

    @BeforeEach
    void init() {
        emprestimoUtils = new EmprestimoUtils();
        userUtils = new UserUtils();
        livroUtils = new LivroUtils();
    }

    @Test
    void PrazoDeDevolucaoDeveSer14DiasCorridosAPartirDoEmprestimo() {
        var dataAtual = LocalDate.now();
        var expectativaDeData = dataAtual.plusDays(14);

        var dataExperadaDe14Dias = service.calcularDataDevolucao(dataAtual);

        assertEquals(expectativaDeData, dataExperadaDe14Dias);
    }

    @Test
    void DeveOcorrerUmaMultaDe2ReaisPorDiaDeAtrasoDoEmprestimoComLivroNaoDevolvido() {
        var emprestimo = emprestimoUtils.emprestimoAtrasado();

        var multa = service.calcularMulta(emprestimo);

        assertThat(multa).isEqualTo(2.0);
    }

    @Test
    void Retorna0ReaisQuandoNaoDevolvidoMasNoPrazo() {
        var emprestimo = emprestimoUtils.emprestimoEmDia();

        var multa = service.calcularMulta(emprestimo);

        assertThat(multa).isEqualTo(0.0);
    }

    @Test
    void DeveOcorrerUmaMultaDe2ReaisPorDiaDeAtrasoDoEmprestimoComLivroDevolvido() {
        var emprestimo = emprestimoUtils.emprestimoAtrasadoMasDevolvido();

        var multa = service.calcularMulta(emprestimo);

        assertThat(multa).isEqualTo(2.0);
    }

    @Test
    void naoDevePermitirEmprestimoQuandoLivroNaoPossuiDisponibilidade() {
        var usuario = new Usuario();
        var livro = new Livro();
        livro.setQuantidadeDisponivel(0);

        assertThrows(RuntimeException.class, () ->
                service.realizarEmprestimo(usuario, livro));

        verify(repository, never()).save(any(Emprestimo.class));
    }

    @Test
    void EmprestimoRealizadoComSucesso() {
        var usuario = new Usuario();
        var livro = new Livro();
        livro.setQuantidadeDisponivel(1);

        when(repository.save(any(Emprestimo.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var emprestimo = service.realizarEmprestimo(usuario, livro);

        assertThat(emprestimo).isNotNull();
        assertThat(emprestimo.getLivro()).isEqualTo(livro);
        assertThat(emprestimo.getUsuario()).isEqualTo(usuario);
        assertThat(emprestimo.getDataEmprestimo()).isEqualTo(LocalDate.now());
        assertThat(emprestimo.getDataDevolucaoPrevista()).isEqualTo(LocalDate.now().plusDays(14));

        verify(repository, times(1)).save(any(Emprestimo.class));
        verify(livroService, times(1)).decrementarDisponibilidade(livro);
    }

    @Test
    void NaoDevePermitirDevolucaoMaisDeUmaVezParaOMesmoEmprestimo() {
        var emprestimo = emprestimoUtils.novoEmprestimo();
        emprestimo.setDataDevolucaoReal(LocalDate.now());

        when(repository.findById(emprestimo.getId())).thenReturn(Optional.of(emprestimo));

        assertThatThrownBy(() -> service.registrarDevolucao(emprestimo.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Empréstimo já foi devolvido");

        verify(repository, never()).save(emprestimo);
    }

    @Test
    void DevolucaoRealizadaComSucesso() {
        var emprestimo = emprestimoUtils.novoEmprestimo();
        emprestimo.setId(1L);

        when(repository.findById(emprestimo.getId())).thenReturn(Optional.of(emprestimo));
        when(repository.save(any(Emprestimo.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var emprestimoDevolvido = service.registrarDevolucao(emprestimo.getId());

        assertThat(emprestimoDevolvido).isNotNull();
        assertThat(emprestimoDevolvido.getDataDevolucaoReal()).isEqualTo(LocalDate.now());
        assertThat(emprestimoDevolvido.getAtivo()).isFalse();
        assertThat(emprestimoDevolvido.getMulta()).isZero();

        verify(livroService, times(1)).incrementarDisponibilidade(emprestimo.getLivro());
        verify(repository, times(1)).save(any(Emprestimo.class));
    }
}