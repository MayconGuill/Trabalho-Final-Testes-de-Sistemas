package com.bibliotech.service;

import com.bibliotech.commons.LivroUtils;
import com.bibliotech.model.Emprestimo;
import com.bibliotech.model.Livro;
import com.bibliotech.repository.LivroRepository;
import org.aspectj.apache.bcel.classfile.Code;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class LivroServiceTest {
    @InjectMocks
    private LivroService service;

    @Mock
    private LivroRepository repository;

    private LivroUtils livroUtils;
    private List<Livro> listaDeLivros;

    @BeforeEach
    void init() {
        livroUtils = new LivroUtils();
        listaDeLivros = livroUtils.listaDeLivros();
    }

    @Test
    void CadastrarLivroComSucesso() {
        var novoLivro = livroUtils.novoLivro();

        when(repository.save(novoLivro)).thenReturn(novoLivro);

        var livroCadastrado = service.salvar(novoLivro);

        assertThat(livroCadastrado).isNotNull().isEqualTo(novoLivro);
    }

    @Test
    void LancaUmaExceptionCasoAlgumCampoObrigatorioNaoEstejaPreenchido() {
        var novoLivro = new Livro();

        assertThatThrownBy(() -> service.salvar(novoLivro))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void ISBNComFormatoInvalido() {
        var livro = livroUtils.isbnInvalido();

        assertThatThrownBy(() -> service.salvar(livro))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ISBN com formato inválido");
    }

    @Test
    void NaoDeveCadastrarLivroComISBNRepetido() {
        var novoLivro = livroUtils.novoLivro();
        var isbn = novoLivro.getIsbn();

        when(repository.findByIsbn(isbn)).thenReturn(Optional.of(novoLivro));

        assertThatThrownBy(() -> service.salvar(novoLivro))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("ISBN já cadastrado!");
    }

    @Test
    void LivrosComEmprestimoAtivosNaoPodemSerExcluidos() {
        var livro = livroUtils.novoLivro();
        var emprestimos = new ArrayList<Emprestimo>();
        emprestimos.add(new Emprestimo());
        livro.setEmprestimos(emprestimos);

        when(repository.findById(livro.getId())).thenReturn(Optional.of(livro));

        assertThatThrownBy(() -> service.excluir(livro.getId()))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Não é possível excluir livro com empréstimos ativos");
    }

    @Test
    void LivroExcluidoComSucesso() {
        var livro = livroUtils.novoLivro();

        when(repository.findById(livro.getId())).thenReturn(Optional.of(livro));
        doNothing().when(repository).delete(livro);

        service.excluir(livro.getId());

        verify(repository).delete(livro);
    }

    @Test
    void DeveListarTodosOsLivrosCadastrados() {
        when(repository.findAll()).thenReturn(listaDeLivros);

        var livros = service.listarTodos();

        assertThat(livros).isNotNull().isEqualTo(listaDeLivros);

        verify(repository).findAll();
    }

    @Test
    void DeveRetornarOLivroPeloID() {
        var first = listaDeLivros.get(0);

        when(repository.findById(first.getId())).thenReturn(Optional.of(first));

        var livro = service.buscarPorId(first.getId()).orElse(null);

        assertThat(livro).isNotNull();
        assertThat(livro.getId()).isEqualTo(first.getId());
    }

    @Test
    void DeveRetornarOLivroPeloISBN() {
        var first = listaDeLivros.get(0);

        when(repository.findByIsbn(first.getIsbn())).thenReturn(Optional.of(first));

        var livro = service.buscarPorIsbn(first.getIsbn()).orElse(null);

        assertThat(livro).isNotNull();
        assertThat(livro.getIsbn()).isEqualTo(first.getIsbn());
    }

    @Test
    void DeveRetornarOLivroPeloTitulo_caso1() {
        var first = listaDeLivros.get(0);
        var livros = singletonList(first);
        var titulo = "Clean Code";

        when(repository.findByTituloContainingIgnoreCase(titulo)).thenReturn(livros);

        var expectativaDeLivros = service.buscarPorTitulo(titulo);

        assertThat(expectativaDeLivros).isNotNull();
        assertThat(expectativaDeLivros).isEqualTo(livros);
    }

    @Test
    void DeveRetornarOLivroPeloTitulo_caso2() {
        var first = listaDeLivros.get(0);
        var livros = singletonList(first);
        var titulo = "clean code";

        when(repository.findByTituloContainingIgnoreCase(titulo)).thenReturn(livros);

        var expectativaDeLivros = service.buscarPorTitulo(titulo);

        assertThat(expectativaDeLivros).isNotNull();
        assertThat(expectativaDeLivros).isEqualTo(livros);
    }

    @Test
    void DeveRetornarOLivroPeloAutor_caso1() {
        var first = listaDeLivros.get(0);
        var livros = singletonList(first);
        var autor = "Robert C. Martin";

        when(repository.findByAutorContainingIgnoreCase(autor)).thenReturn(livros);

        var expectativaDeLivros = service.buscarPorAutor(autor);

        assertThat(expectativaDeLivros).isNotNull();
        assertThat(expectativaDeLivros).isEqualTo(livros);
    }

    @Test
    void DeveRetornarOLivroPeloAutor_caso2() {
        var first = listaDeLivros.get(0);
        var livros = singletonList(first);
        var autor = "robert c. martin";

        when(repository.findByAutorContainingIgnoreCase(autor)).thenReturn(livros);

        var expectativaDeLivros = service.buscarPorAutor(autor);

        assertThat(expectativaDeLivros).isNotNull();
        assertThat(expectativaDeLivros).isEqualTo(livros);
    }

    @Test
    void DeveRetornarOsLivrosComQuantidadeDisponível() {
        var livros = listaDeLivros;

        when(repository.findByQuantidadeDisponivelGreaterThan(0)).thenReturn(livros);

        var expectativaDeLivros = service.listarDisponiveis();

        assertThat(expectativaDeLivros).isNotNull();
        assertThat(expectativaDeLivros).isEqualTo(livros);
    }

}