| ID | Descrição do Teste | Classe Testada | Método Testado | Requisito/RN |
|----|-------------------|----------------|----------------|--------------|
| TU-001 | Deve cadastrar um usuário com sucesso | UsuarioService | salvar() | RF-02 |
| TU-002 | Deve impedir cadastro de usuario com email duplicado | UsuarioService | salvar() | RN-10 |
| TU-003 | Deve impedir cadastro de usuario com cpf duplicado | UsuarioService | salvar() | RN-11 |
| TU-004 | Deve validar formato de CPF corretamente | UsuarioService | validarCPF() | RN-07 |
| TU-005 | Apenas usuários ativos (status = true) podem fazer login no sistema | UsuarioService | autenticar() | RN-08 |
| TU-006 | Usuários com empréstimos ativos não podem ser excluídos do sistema | UsuarioService | excluir() | RN-06 |
| TU-007 | O sistema deve permitir excluir usuários desde que não possuam empréstimos ativos | UsuarioService | excluir() | RF-09 |
| TU-008 | O sistema deve autenticar usuários através de email e senha | UsuarioService | autenticar() | RF-15 |
| TU-009 | O sistema deve permitir o cadastro de livros contendo título, autor, ISBN, editora, ano de publicação e quantidade de exemplares | LivroService | salvar() | RF-01 |
| TU-010 | Não deve ser possível cadastrar dois livros com o mesmo ISBN | LivroService | salvar() | RN-02 |
| TU-011 | Deve calcular multa por atraso de R$ 2,00 (dois reais) por dia de atraso | EmprestimoService | calcularMulta() | RN-03 |
| TU-012 | Só é possível emprestar um livro se houver exemplares disponíveis (quantidade disponível > 0) | EmprestimoService | realizarEmprestimo() | RN-04 |
| TU-013 | O prazo padrão de devolução é de 14 dias corridos a partir da data do empréstimo | EmprestimoService | calcularDataDevolucao() | RN-01 |
| TU-014 | O sistema deve permitir registrar a devolução de um empréstimo | EmprestimoService | registrarDevolucao() | RF-12 |
| TU-015 | O sistema deve validar o formato do ISBN antes de salvar um livro | LivroService | salvar() | RF-03 |
| TU-016 | O sistema deve permitir excluir livros desde que não possuam empréstimos ativos | LivroService | excluir() | RF-08 |
