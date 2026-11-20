package edu.curso.control;

/** Regras compartilhadas: formato e dígitos verificadores dos documentos. */
public final class Validacoes {
    private Validacoes() {}

    public static boolean cpf(String valor) {
        if (valor == null || !valor.matches("[0-9]{11}") || valor.chars().distinct().count() == 1) return false;
        for (int tamanho = 9; tamanho <= 10; tamanho++) {
            int soma = 0;
            for (int i = 0; i < tamanho; i++) soma += (valor.charAt(i) - '0') * (tamanho + 1 - i);
            int digito = 11 - soma % 11;
            if (digito >= 10) digito = 0;
            if (digito != valor.charAt(tamanho) - '0') return false;
        }
        return true;
    }

    public static boolean cnpj(String valor) {
        if (valor == null || !valor.matches("[0-9]{14}") || valor.chars().distinct().count() == 1) return false;
        for (int tamanho = 12; tamanho <= 13; tamanho++) {
            int soma = 0, peso = 2;
            for (int i = tamanho - 1; i >= 0; i--) {
                soma += (valor.charAt(i) - '0') * peso;
                peso = peso == 9 ? 2 : peso + 1;
            }
            int digito = 11 - soma % 11;
            if (digito >= 10) digito = 0;
            if (digito != valor.charAt(tamanho) - '0') return false;
        }
        return true;
    }
}
