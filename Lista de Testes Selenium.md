| ID | Descrição do Teste | Fluxo a Testar | Requisito |
| TS-001 | Deve realizar login com credenciais válidas | Login → Dashboard | RF-15 |
| TS-002 | Deve cadastrar novo usuário com sucesso | Usuarios → Novo → Preencher → Salvar | RF-02 |
| TS-003 | Deve exibir mensagem de erro quando email já existir | Usuarios → Novo → Preencher → Salvar | RN-10 |
| TS-004 | Deve exibir mensagem de erro quando cpf já existir | Usuarios → Novo → Preencher → Salvar | RN-11 |
| TS-005 | Deve exibir mensagem de erro quando cpf estiver com formato inválido | Usuarios → Novo → Preencher → Salvar | RF-04 |
| TS-006 | Deve exibir erro ao tentar login com senha incorreta | Login com senha errada | RF-15 |
| TS-007 | Deve cadastrar novo livro com sucesso | Livros → Novo → Preencher → Salvar | RF-01 |
| TS-008 | Deve filtrar os livros pelo titulo de forma case-insensitive | Livros → Buscar | RF-05 |
| TS-009 | Deve filtrar os livros pelo autor de forma case-insensitive | Livros → Buscar | RF-05 |
| TS-010 | Deve editar o livro com sucesso | Livros → Editar → Preencher → Salvar | RF-07 |
| TS-011 | Deve impedir o cadastro do livro se o ano for menor que 1000 ou maior que 2100 | Livros → Novo → Preencher → Salvar | RF-01 |
| TS-012 | Deve exibir mensagem de erro quando ISBN for duplicado | Livros → Novo → Preencher → Salvar | RF-01 |

