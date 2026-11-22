package br.edu.exemplo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Carrinho {

    private static final BigDecimal VALOR_MINIMO_DESCONTO = new BigDecimal("500.00");
    private static final BigDecimal PERCENTUAL_DESCONTO = new BigDecimal("0.10");

    private final List<Produto> produtos = new ArrayList<>();

    public void adicionar(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo");
        }
        if (produto.getPreco() < 0) {
            throw new IllegalArgumentException("Preço do produto não pode ser negativo");
        }
        produtos.add(produto);
    }

    public boolean remover(Produto produto) {
        return produtos.remove(produto);
    }

    public int getQuantidadeItens() {
        return produtos.size();
    }

    public double getSubtotal() {
        return subtotal().doubleValue();
    }

    public double getDesconto() {
        return desconto().doubleValue();
    }

    public double getTotal() {
        return subtotal().subtract(desconto()).doubleValue();
    }

    // valores monetários calculados com BigDecimal para evitar erros de ponto flutuante
    private BigDecimal subtotal() {
        BigDecimal soma = BigDecimal.ZERO;
        for (Produto produto : produtos) {
            soma = soma.add(BigDecimal.valueOf(produto.getPreco()));
        }
        return soma.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal desconto() {
        BigDecimal subtotal = subtotal();
        if (subtotal.compareTo(VALOR_MINIMO_DESCONTO) < 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return subtotal.multiply(PERCENTUAL_DESCONTO).setScale(2, RoundingMode.HALF_UP);
    }
}
