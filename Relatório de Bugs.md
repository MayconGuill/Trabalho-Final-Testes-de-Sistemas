```
BUG-001: Sistema permite cadastro de livros com ISBN duplicado

Severidade: ALTA
Módulo: Gerenciamento de Livros
Requisito violado: RN-02 (ISBN Único)
Caso de teste que falhou: TS-005

Descrição:
Ao tentar cadastrar um livro com um ISBN já existente no sistema, 
esperava-se que uma mensagem de erro fosse exibida e o cadastro 
impedido. No entanto, o sistema permite o cadastro, resultando 
em dois livros com o mesmo ISBN.

Passos para reproduzir:
1. Acessar /livros/novo
2. Cadastrar livro com ISBN "978-0132350884"
3. Acessar /livros/novo novamente
4. Cadastrar outro livro com o mesmo ISBN "978-0132350884"
5. Sistema permite cadastro (ERRO!)

Resultado esperado: 
Mensagem "ISBN já cadastrado!" e impedimento do cadastro

Resultado obtido:
Mensagem "Livro cadastrado com sucesso!" e livro duplicado salvo

Evidência: Ver screenshot deveExibirMensagemDeErroQuandoIsbnForDuplicado.png


--

BUG-002: Sistema não realiza login com email e senha.

Severidade: ALTA
Módulo: Login
Requisito violado: RF-15 (Autenticação)
Caso de teste que falhou: TS-001

Descrição:
Ao tentar realizar o login com email e senha cadastrado, o sistema não redireciona ao menu de dashboard, apresentando uma mensagem de "Erro ao realizar o login".

Passos para reproduzir:
1. Acessar /login
2. Preencher email e senha
3. Clicar em "Entrar"

Resultado esperado: 
Usuário logado, redirecionado ao menu principal dashboard

Resultado obtido:
Erro ao realizar o login

Evidência: Ver screenshot deveFazerLoginComSucesso.png

--

BUG-003: Sistema não apresenta mensagem de erro quando CPF já existe.

Severidade: ALTA
Módulo: Usuarios
Requisito violado: RF-02 (Cadastro de Usuários)
Caso de teste que falhou: TS-004

Descrição:
Ao cadastrar um novo usuário com um CPF que já existe cadastrado, o sistema não apresenta uma mensagem de erro ao tentar "Salvar", o mesmo apenas continua na tela de preenchimento sem nenhum tipo de aviso.

Passos para reproduzir:
1. Acessar /usuários/novo
2. Cadastrar um usuário com o CPF 123.456.789-00
3. Clicar em "Salvar"
4. Acessar novamente /usuários/novo
5. Cadastrar novamente um novo usuário com o CPF 123.456.789-00
6. Clicar em "Salvar"

Resultado esperado: 
Mensagem de erro clara, informando que o CPF já existe

Resultado obtido:
Sistema não realiza o cadastro, porém não informa nenhum tipo de erro ao usuário.

Evidência: Ver screenshot deveExibirMensagemDeErroQuandoCpfJaExistir.png

--

BUG-004: Sistema não apresenta mensagem de erro quando Email já existe.

Severidade: ALTA
Módulo: Usuarios
Requisito violado: RF-02 (Cadastro de Usuários)
Caso de teste que falhou: TS-003

Descrição:
Ao cadastrar um novo usuário com um Email já existente, o sistema não apresenta uma mensagem de erro ao tentar "Salvar", o mesmo apenas continua na tela de preenchimento sem nenhum tipo de aviso.

Passos para reproduzir:
1. Acessar /usuários/novo
2. Cadastrar um usuário com o Email admin2@bibliotech.com
3. Clicar em "Salvar"
4. Acessar novamente /usuários/novo
5. Cadastrar novamente um novo usuário com o Email admin2@bibliotech.com
6. Clicar em "Salvar"

Resultado esperado: 
Mensagem de erro clara, informando que o Email já existe

Resultado obtido:
Sistema não realiza o cadastro, porém não informa nenhum tipo de erro ao usuário.

Evidência: Ver screenshot deveExibirMensagemDeErroQuandoEmailJaExistir.png

-- 

BUG-005: Sistema não apresenta mensagem de erro quando CPF estiver com formato inválido.

Severidade: MÉDIA
Módulo: Usuarios
Requisito violado: RN-07 (Validação de CPF)
Caso de teste que falhou: TS-005

Descrição:
Ao cadastrar um novo usuário com um CPF sem formatação, o sistema não apresenta uma mensagem de erro ao tentar "Salvar", o mesmo apenas continua na tela de preenchimento sem nenhum tipo de aviso.

Passos para reproduzir:
1. Acessar /usuários/novo
2. Cadastrar um usuário com o CPF 12345678901
3. Clicar em "Salvar"

Resultado esperado: 
Mensagem de erro clara, informando que precisa de um CPF válido

Resultado obtido:
Sistema não realiza o cadastro, porém não informa nenhum tipo de erro ao usuário.

Evidência: Ver screenshot deveExibirMensagemDeErroQuandoCpfEstiverEmFormatoInvalido.png
```
