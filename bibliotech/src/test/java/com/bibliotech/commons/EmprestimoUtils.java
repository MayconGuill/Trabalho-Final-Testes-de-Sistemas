package com.bibliotech.commons;

import com.bibliotech.model.Emprestimo;
import com.bibliotech.model.Livro;
import com.bibliotech.model.Usuario;

import java.time.LocalDate;

public class EmprestimoUtils {
    public Emprestimo novoEmprestimo() {
        var usuario = new Usuario();
        var livro = new Livro();
        var emprestimo = new Emprestimo(usuario, livro, LocalDate.now(), LocalDate.now().plusDays(1));

        return emprestimo;
    }

    public Emprestimo emprestimoAtrasado() {
        var emprestimo = new Emprestimo();
        emprestimo.setDataDevolucaoReal(null);
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().minusDays(1));

        return emprestimo;
    }

    public Emprestimo emprestimoAtrasadoMasDevolvido() {
        var emprestimo = new Emprestimo();
        emprestimo.setDataDevolucaoReal(LocalDate.now());
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().minusDays(1));

        return emprestimo;
    }

    public Emprestimo emprestimoEmDia() {
        var emprestimo = new Emprestimo();
        emprestimo.setDataDevolucaoReal(null);
        emprestimo.setDataDevolucaoPrevista(LocalDate.now().plusDays(1));

        return emprestimo;
    }
}
