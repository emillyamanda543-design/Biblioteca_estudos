# Verificação do pacote

- Fontes principais e testes compilados com BellSoft Liberica JDK 25 Full.
- Gradle 9.9.1: `:app:verificarControles` concluiu com BUILD SUCCESSFUL.
- Quatro controles verificados com DAOs de teste: cadastro, consulta, alteração,
  exclusão, limpeza, mensagens individuais e rejeição de datas futuras.
- Dígitos verificadores de CPF e CNPJ conferidos com entradas válidas e inválidas.

## Conferência necessária no computador da dupla
Não foi executado um servidor MariaDB nem uma sessão visual JavaFX neste ambiente.
1. Faça backup do banco e execute app/database/banco.sql no MariaDB.
2. Configure BIBLIOTECA_DB_PASSWORD e execute `gradle :app:run`.
3. Em cada tela, teste os seis campos, dados inválidos e mensagens individuais.
4. Cadastre, pesquise, edite e exclua registros de teste na coluna Ações.
5. Feche e reabra o sistema para conferir que os dados continuam no banco.
6. Teste o script novamente para conferir atualização sem perda dos registros.
7. Grave os dois minutos de demonstração e inclua o link real no README.

Os testes automatizados não equivalem a uma aprovação completa da integração
com o banco e da interface gráfica. O vídeo ainda é uma pendência da entrega.
