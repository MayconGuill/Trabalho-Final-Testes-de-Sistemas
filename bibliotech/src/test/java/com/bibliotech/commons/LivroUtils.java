package com.bibliotech.commons;

import com.bibliotech.model.Livro;

import java.util.ArrayList;
import java.util.List;

public class LivroUtils {

    public Livro novoLivro() {
        var livro = new Livro("Clean Code", "Robert C. Martin",
                "978-0132350884", "Prentice Hall", 2008, 3);

        return livro;
    }

    public Livro isbnInvalido() {
        var livro = new Livro("Clean Code", "Robert C. Martin",
                "978", "Prentice Hall", 2008, 3);

        return livro;
    }

    public List<Livro> listaDeLivros() {
        var livro = new Livro("Clean Code", "Robert C. Martin",
                "978-0132350884", "Prentice Hall", 2008, 3);
        Livro livro2 = new Livro("Design Patterns", "Gang of Four",
                "978-0201633610", "Addison-Wesley", 1994, 2);
        return new ArrayList<>(List.of(livro, livro2));
    }
}
