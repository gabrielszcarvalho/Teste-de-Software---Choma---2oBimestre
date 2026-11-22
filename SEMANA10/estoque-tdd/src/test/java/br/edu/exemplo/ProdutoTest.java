package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoTest {

    // ---------------------------------------------------------------
    // Casos de teste fornecidos pelo professor (Requisitos 1 a 6)
    // ---------------------------------------------------------------

    @Test
    void produtoDeveIniciarComEstoqueZero() {
        Produto produto = new Produto("Teclado");

        assertEquals(0, produto.getQuantidade());
    }

    @Test
    void deveAdicionarProdutoAoEstoque() {
        Produto produto = new Produto("Teclado");

        produto.adicionarEstoque(10);

        assertEquals(10, produto.getQuantidade());
    }

    @Test
    void deveAcumularQuantidadeEmEstoque() {
        Produto produto = new Produto("Teclado");

        produto.adicionarEstoque(10);
        produto.adicionarEstoque(5);

        assertEquals(15, produto.getQuantidade());
    }

    @Test
    void deveRetirarProdutoDoEstoque() {
        Produto produto = new Produto("Teclado");

        produto.adicionarEstoque(10);
        produto.retirarEstoque(3);

        assertEquals(7, produto.getQuantidade());
    }

    @Test
    void naoDeveRetirarQuantidadeMaiorQueEstoque() {
        Produto produto = new Produto("Teclado");

        produto.adicionarEstoque(10);
        produto.retirarEstoque(15);

        assertEquals(10, produto.getQuantidade());
    }

    @Test
    void naoDeveAdicionarQuantidadeNegativa() {
        Produto produto = new Produto("Teclado");

        produto.adicionarEstoque(-10);

        assertEquals(0, produto.getQuantidade());
    }

    // ---------------------------------------------------------------
    // Casos de teste adicionais
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Requisito 6: entrada igual a zero também é recusada")
    void naoDeveAdicionarQuantidadeZero() {
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(5);

        assertFalse(produto.adicionarEstoque(0));
        assertEquals(5, produto.getQuantidade());
    }

    @Test
    @DisplayName("Entrada válida retorna true")
    void entradaValidaDeveRetornarVerdadeiro() {
        assertTrue(new Produto("Teclado").adicionarEstoque(1));
    }

    @Test
    @DisplayName("Retirar exatamente o estoque disponível zera o produto (fronteira)")
    void deveRetirarTodoOEstoque() {
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(10);

        assertTrue(produto.retirarEstoque(10));
        assertEquals(0, produto.getQuantidade());
    }

    @Test
    @DisplayName("Retirada maior que o estoque retorna false")
    void retiradaMaiorQueEstoqueDeveRetornarFalso() {
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(10);

        assertFalse(produto.retirarEstoque(11));
    }

    @Test
    @DisplayName("Retirada de quantidade zero ou negativa é recusada")
    void naoDeveRetirarQuantidadeZeroOuNegativa() {
        Produto produto = new Produto("Teclado");
        produto.adicionarEstoque(10);

        assertFalse(produto.retirarEstoque(0));
        assertFalse(produto.retirarEstoque(-5));
        assertEquals(10, produto.getQuantidade());
    }

    @Test
    @DisplayName("O nome informado no construtor é mantido")
    void deveGuardarNomeDoProduto() {
        assertEquals("Teclado", new Produto("Teclado").getNome());
    }

    @Test
    @DisplayName("Produto sem nome não é aceito")
    void naoDeveCriarProdutoSemNome() {
        assertThrows(IllegalArgumentException.class, () -> new Produto(null));
        assertThrows(IllegalArgumentException.class, () -> new Produto("   "));
    }
}
