# Minha Biblioteca

## Integrantes
- Emilly Amanda Brito do Nascimento.
- Miguel Santos Ferreira.

## Tema e problema resolvido
Sistema corporativo de cadastro para uma biblioteca. Centraliza livros, leitores,
funcionários e editoras, evitando cadastros inconsistentes e permitindo consultar,
alterar e excluir registros persistidos no MariaDB.

## Entidades implementadas
Cada entidade possui ID e seis campos de negócio:
- Livro: título, autor, código, editora, data de cadastro e gênero.
- Leitor: nome, CPF, telefone, e-mail, data de nascimento e endereço.
- Funcionário: nome, CPF, telefone, cargo, data de admissão e salário.
- Editora: nome, telefone, e-mail, data de fundação, CNPJ e cidade.

Os quatro CRUDs oferecem cadastro, pesquisa, edição e exclusão com confirmação.
O nome da editora continua sendo um atributo textual do livro.

## Camadas e funcionamento
- Fronteira (`edu.curso.view`): JavaFX sem FXML, formulário com bindings
  bidirecionais e TableView com coluna Ações (Editar e Excluir).
- Controle (`edu.curso.control`): propriedades JavaFX, regras de validação e
  mensagens específicas vinculadas a cada campo. Interfaces DAO no mesmo pacote.
- Entidade (`edu.curso.model`): atributos privados, getters/setters e toString.
- Persistência (`edu.curso.infraestructure`): implementações DAO com JDBC,
  PreparedStatement e fechamento automático dos recursos.

## Requisitos e execução no Windows
1. Instale **BellSoft Liberica JDK 25 Full**, incluindo JavaFX. Um JDK comum sem
   JavaFX não atende a esta configuração. Configure JAVA_HOME e PATH.
2. Instale **Gradle 9.9.1** manualmente e coloque sua pasta bin no PATH.
3. Instale e inicie MariaDB. No cliente MariaDB, execute:
   `SOURCE C:/opt/biblioteca-estudo/app/database/banco.sql;`
   Ajuste o caminho conforme o local onde extraiu o projeto.
4. Abra o CMD na pasta que contém settings.gradle e configure:

```cmd
set "BIBLIOTECA_DB_USER=root"
set "BIBLIOTECA_DB_PASSWORD=SUA_SENHA_LOCAL"
gradle :app:run
```

A URL padrão é jdbc:mariadb://localhost:3306/biblioteca. Para outro servidor,
configure BIBLIOTECA_DB_URL. A senha não é publicada no repositório.
Use `gradle` para usar a instalação manual. `gradlew` pode baixar a distribuição
caso ela ainda não esteja em cache. As dependências JDBC e de logging precisam
estar disponíveis em cache ou ser obtidas na primeira execução.

No Eclipse, importe a pasta raiz como Existing Gradle Project, configure o JDK
25 Full e selecione a instalação local do Gradle 9.9.1. Execute :app:run.

## Atualização de banco existente
O banco.sql preserva tabelas e registros existentes. Acrescenta os novos campos
às tabelas plurais anteriores por ALTER TABLE ADD COLUMN IF NOT EXISTS.
Cadastros antigos ficam com novos campos nulos: complete-os usando Editar.
As tabelas antigas singulares (livro, leitor, funcionario, emprestimo) não são
excluídas nem migradas automaticamente. Faça backup antes de atualizar.

## Validações
Campos obrigatórios; limites de texto; CPF e CNPJ numéricos com dígitos
verificadores; telefone numérico com DDD (10 ou 11 dígitos); e-mail; datas não
futuras; salário positivo com até duas casas decimais. Erros aparecem ao lado
dos respectivos campos. Código/CPF/CNPJ e telefones duplicados são conferidos
nos respectivos cadastros; nomes duplicados de leitor e funcionário também.

## Divisão de responsabilidades
- Emilly Amanda Brito do Nascimento: CRUDs de Livro e Editora, incluindo entidade,
  tela, controle, DAO, persistência e validações.
- Miguel Santos Ferreira: CRUDs de Leitor e Funcionário, incluindo entidade,
  tela, controle, DAO, persistência e validações.
- Em conjunto: integração, testes, documentação e apresentação.

## Vídeo de demonstração — 2 minutos
**PENDENTE: inserir aqui o link real do YouTube (público ou não listado).**
O projeto ainda não está pronto para entrega enquanto este link estiver pendente.
Roteiro sugerido em ROTEIRO_VIDEO.md.

## Verificações
Execute `gradle :app:verificarControles` para conferir os quatro controles com
DAOs de teste, sem alterar o banco real. Esse teste não substitui a verificação
visual nem um teste de integração com o seu MariaDB.
