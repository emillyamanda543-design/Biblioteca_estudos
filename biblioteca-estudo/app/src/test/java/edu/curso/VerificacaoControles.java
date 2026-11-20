package edu.curso;
import edu.curso.control.*;
import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.util.*;
/** Verificação dos controles com DAO de teste: não altera dados reais. */
public class VerificacaoControles {
    private static void exigir(boolean condicao, String mensagem) {
        if (!condicao) throw new AssertionError(mensagem);
    }
    private static void invalido(Runnable operacao) {
        try { operacao.run(); throw new AssertionError("Deveria rejeitar o cadastro"); }
        catch (IllegalArgumentException esperado) { exigir(!esperado.getMessage().isBlank(), "Mensagem ausente"); }
    }
    @SuppressWarnings("unchecked")
    private static <T> T dao(Class<T> contrato) {
        List<Object> registros = new ArrayList<>();
        return (T) Proxy.newProxyInstance(contrato.getClassLoader(), new Class<?>[]{contrato}, (proxy, metodo, args) -> {
            switch (metodo.getName()) {
                case "findAll": return new ArrayList<>(registros);
                case "searchByName": return new ArrayList<>(registros);
                case "searchById": return registros.stream().filter(item -> {
                    try { return item.getClass().getMethod("getId").invoke(item).equals(args[0]); }
                    catch (ReflectiveOperationException ex) { throw new IllegalStateException(ex); }
                }).findFirst().orElse(null);
                case "save": args[0].getClass().getMethod("setId", long.class).invoke(args[0], 1L); registros.add(args[0]); return null;
                case "update": registros.clear(); registros.add(args[1]); return null;
                case "delete": registros.remove(args[0]); return null;
                default: throw new UnsupportedOperationException(metodo.getName());
            }
        });
    }
    public static void main(String[] args) {
        exigir(Validacoes.cpf("52998224725"), "CPF válido");
        exigir(!Validacoes.cpf("11111111111"), "CPF repetido");
        exigir(!Validacoes.cpf("52998224726"), "CPF inválido");
        exigir(Validacoes.cnpj("11222333000181"), "CNPJ válido");
        exigir(!Validacoes.cnpj("11222333000182"), "CNPJ inválido");
        {
            LivroController c = new LivroController(dao(LivroDAO.class));
            invalido(c::save);
            exigir(!c.erroProperty("titulo").get().isEmpty(), "Erro individual de Livro.titulo");
            exigir(!c.erroProperty("autor").get().isEmpty(), "Erro individual de Livro.autor");
            exigir(!c.erroProperty("codigo").get().isEmpty(), "Erro individual de Livro.codigo");
            exigir(!c.erroProperty("editora").get().isEmpty(), "Erro individual de Livro.editora");
            exigir(!c.erroProperty("dataCadastro").get().isEmpty(), "Erro individual de Livro.dataCadastro");
            exigir(!c.erroProperty("genero").get().isEmpty(), "Erro individual de Livro.genero");
            c.tituloProperty().set("Teste");
            c.autorProperty().set("Autor");
            c.codigoProperty().set("L-001");
            c.editoraProperty().set("Editora");
            c.dataCadastroProperty().set(LocalDate.of(2020, 1, 1));
            c.generoProperty().set("Romance");
            c.save();
            exigir(c.getLista().size() == 1, "Cadastro");
            exigir(c.idProperty().get() == 0, "Limpeza após salvar");
            var item = c.getLista().getFirst();
            c.fromEntity(item);
            c.update();
            exigir(c.getLista().size() == 1, "Atualização");
            c.searchByName("teste");
            exigir(c.getLista().size() == 1, "Pesquisa");
            c.fromEntity(c.getLista().getFirst());
            c.dataCadastroProperty().set(LocalDate.now().plusDays(1));
            invalido(c::update);
            exigir(!c.erroProperty("dataCadastro").get().isBlank(), "Data futura");
            c.delete(c.getLista().getFirst());
            exigir(c.getLista().isEmpty(), "Exclusão");
            System.out.println("Livro: cadastro, consulta, alteração, exclusão e validações OK");
        }
        {
            LeitorController c = new LeitorController(dao(LeitorDAO.class));
            invalido(c::save);
            exigir(!c.erroProperty("nome").get().isEmpty(), "Erro individual de Leitor.nome");
            exigir(!c.erroProperty("cpf").get().isEmpty(), "Erro individual de Leitor.cpf");
            exigir(!c.erroProperty("telefone").get().isEmpty(), "Erro individual de Leitor.telefone");
            exigir(!c.erroProperty("email").get().isEmpty(), "Erro individual de Leitor.email");
            exigir(!c.erroProperty("dataNascimento").get().isEmpty(), "Erro individual de Leitor.dataNascimento");
            exigir(!c.erroProperty("endereco").get().isEmpty(), "Erro individual de Leitor.endereco");
            c.nomeProperty().set("Leitor");
            c.cpfProperty().set("52998224725");
            c.telefoneProperty().set("11987654321");
            c.emailProperty().set("leitor@example.com");
            c.dataNascimentoProperty().set(LocalDate.of(2020, 1, 1));
            c.enderecoProperty().set("Rua de teste");
            c.save();
            exigir(c.getLista().size() == 1, "Cadastro");
            exigir(c.idProperty().get() == 0, "Limpeza após salvar");
            var item = c.getLista().getFirst();
            c.fromEntity(item);
            c.update();
            exigir(c.getLista().size() == 1, "Atualização");
            c.searchByName("teste");
            exigir(c.getLista().size() == 1, "Pesquisa");
            c.fromEntity(c.getLista().getFirst());
            c.dataNascimentoProperty().set(LocalDate.now().plusDays(1));
            invalido(c::update);
            exigir(!c.erroProperty("dataNascimento").get().isBlank(), "Data futura");
            c.delete(c.getLista().getFirst());
            exigir(c.getLista().isEmpty(), "Exclusão");
            System.out.println("Leitor: cadastro, consulta, alteração, exclusão e validações OK");
        }
        {
            FuncionarioController c = new FuncionarioController(dao(FuncionarioDAO.class));
            invalido(c::save);
            exigir(!c.erroProperty("nome").get().isEmpty(), "Erro individual de Funcionario.nome");
            exigir(!c.erroProperty("cpf").get().isEmpty(), "Erro individual de Funcionario.cpf");
            exigir(!c.erroProperty("telefone").get().isEmpty(), "Erro individual de Funcionario.telefone");
            exigir(!c.erroProperty("cargo").get().isEmpty(), "Erro individual de Funcionario.cargo");
            exigir(!c.erroProperty("dataAdmissao").get().isEmpty(), "Erro individual de Funcionario.dataAdmissao");
            exigir(!c.erroProperty("salario").get().isEmpty(), "Erro individual de Funcionario.salario");
            c.nomeProperty().set("Funcionário");
            c.cpfProperty().set("52998224725");
            c.telefoneProperty().set("11987654321");
            c.cargoProperty().set("Bibliotecário");
            c.dataAdmissaoProperty().set(LocalDate.of(2020, 1, 1));
            c.salarioProperty().set("2500,50");
            c.save();
            exigir(c.getLista().size() == 1, "Cadastro");
            exigir(c.idProperty().get() == 0, "Limpeza após salvar");
            var item = c.getLista().getFirst();
            c.fromEntity(item);
            c.update();
            exigir(c.getLista().size() == 1, "Atualização");
            c.searchByName("teste");
            exigir(c.getLista().size() == 1, "Pesquisa");
            c.fromEntity(c.getLista().getFirst());
            c.dataAdmissaoProperty().set(LocalDate.now().plusDays(1));
            invalido(c::update);
            exigir(!c.erroProperty("dataAdmissao").get().isBlank(), "Data futura");
            c.delete(c.getLista().getFirst());
            exigir(c.getLista().isEmpty(), "Exclusão");
            System.out.println("Funcionario: cadastro, consulta, alteração, exclusão e validações OK");
        }
        {
            EditoraController c = new EditoraController(dao(EditoraDAO.class));
            invalido(c::save);
            exigir(!c.erroProperty("nome").get().isEmpty(), "Erro individual de Editora.nome");
            exigir(!c.erroProperty("telefone").get().isEmpty(), "Erro individual de Editora.telefone");
            exigir(!c.erroProperty("email").get().isEmpty(), "Erro individual de Editora.email");
            exigir(!c.erroProperty("dataFundacao").get().isEmpty(), "Erro individual de Editora.dataFundacao");
            exigir(!c.erroProperty("cnpj").get().isEmpty(), "Erro individual de Editora.cnpj");
            exigir(!c.erroProperty("cidade").get().isEmpty(), "Erro individual de Editora.cidade");
            c.nomeProperty().set("Editora");
            c.telefoneProperty().set("11987654321");
            c.emailProperty().set("editora@example.com");
            c.dataFundacaoProperty().set(LocalDate.of(2020, 1, 1));
            c.cnpjProperty().set("11222333000181");
            c.cidadeProperty().set("São Paulo");
            c.save();
            exigir(c.getLista().size() == 1, "Cadastro");
            exigir(c.idProperty().get() == 0, "Limpeza após salvar");
            var item = c.getLista().getFirst();
            c.fromEntity(item);
            c.update();
            exigir(c.getLista().size() == 1, "Atualização");
            c.searchByName("teste");
            exigir(c.getLista().size() == 1, "Pesquisa");
            c.fromEntity(c.getLista().getFirst());
            c.dataFundacaoProperty().set(LocalDate.now().plusDays(1));
            invalido(c::update);
            exigir(!c.erroProperty("dataFundacao").get().isBlank(), "Data futura");
            c.delete(c.getLista().getFirst());
            exigir(c.getLista().isEmpty(), "Exclusão");
            System.out.println("Editora: cadastro, consulta, alteração, exclusão e validações OK");
        }
    }
}
