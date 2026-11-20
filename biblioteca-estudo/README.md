# Biblioteca — Java 25 e Gradle 9.9.1

Projeto JavaFX organizado em `app/`, com as camadas `control`, `model`, `infraestructure` e `view`.

## Executar no Windows

Abra o CMD na pasta `biblioteca-estudo` e execute:

```cmd
.\gradlew.bat clean :app:compileJava
.\gradlew.bat :app:run
```

A primeira execução do Gradle pode precisar baixar JavaFX e o driver do MariaDB.

## Banco

O script SQL está em `app/database/banco.sql`. Use MariaDB com o banco `biblioteca` configurado conforme esse script. O código lê as variáveis de ambiente `BIBLIOTECA_DB_URL`, `BIBLIOTECA_DB_USER` e `BIBLIOTECA_DB_PASSWORD`. Nenhuma senha é armazenada neste ZIP.

## Eclipse

Importe a pasta raiz como **Gradle > Existing Gradle Project**. Não importe apenas a pasta `app`.

As pastas `.gradle`, `build` e `bin` são geradas automaticamente e não precisam ser entregues. Não foi adicionada nenhuma biblioteca além do JavaFX e do driver MariaDB que já faziam parte da configuração.
