package br.edu.exemplo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * Testes escritos requisito a requisito (REQUISITOS.md), sempre antes
 * da implementação correspondente em Carrinho (ciclo RED -> GREEN -> REFACTOR).
 */
class CarrinhoTest {

    private static final double DELTA = 0.001;

    private Carrinho carrinho;

    @BeforeEach
    void criarCarrinho() {
        carrinho = new Carrinho();
    }

    @Nested
    @DisplayName("Requisito 1 - Carrinho vazio")
    class Requisito1 {

        @Test
        @DisplayName("Carrinho recém-criado tem total R$ 0,00")
        void carrinhoNovoDeveTerTotalZero() {
            assertEquals(0.0, carrinho.getTotal(), DELTA);
        }

        @Test
        @DisplayName("Carrinho recém-criado não tem itens")
        void carrinhoNovoNaoDeveTerItens() {
            assertEquals(0, carrinho.getQuantidadeItens());
        }
    }

    @Nested
    @DisplayName("Requisito 2 - Adicionar um produto")
    class Requisito2 {

        @Test
        @DisplayName("Adicionar Teclado de R$ 100,00 resulta em total R$ 100,00")
        void deveAdicionarUmProduto() {
            carrinho.adicionar(new Produto("Teclado", 100.0));

            assertEquals(100.0, carrinho.getTotal(), DELTA);
            assertEquals(1, carrinho.getQuantidadeItens());
        }

        @Test
        @DisplayName("Produto nulo não pode ser adicionado")
        void naoDeveAdicionarProdutoNulo() {
            assertThrows(IllegalArgumentException.class, () -> carrinho.adicionar(null));
            assertEquals(0, carrinho.getQuantidadeItens());
        }
    }

    @Nested
    @DisplayName("Requisito 3 - Adicionar vários produtos")
    class Requisito3 {

        @Test
        @DisplayName("Teclado (R$ 100,00) + Mouse (R$ 50,00) = R$ 150,00")
        void deveSomarVariosProdutos() {
            carrinho.adicionar(new Produto("Teclado", 100.0));
            carrinho.adicionar(new Produto("Mouse", 50.0));

            assertEquals(150.0, carrinho.getTotal(), DELTA);
            assertEquals(2, carrinho.getQuantidadeItens());
        }

        @Test
        @DisplayName("Centavos são somados sem erro de arredondamento")
        void deveSomarCentavosCorretamente() {
            carrinho.adicionar(new Produto("Caneta", 0.10));
            carrinho.adicionar(new Produto("Lápis", 0.20));

            assertEquals(0.30, carrinho.getTotal(), 0.0);
        }
    }

    @Nested
    @DisplayName("Requisito 4 - Remover produto")
    class Requisito4 {

        @Test
        @DisplayName("Remover o Mouse de um carrinho de R$ 150,00 deixa R$ 100,00")
        void deveRemoverProduto() {
            Produto teclado = new Produto("Teclado", 100.0);
            Produto mouse = new Produto("Mouse", 50.0);
            carrinho.adicionar(teclado);
            carrinho.adicionar(mouse);

            assertTrue(carrinho.remover(mouse));

            assertEquals(100.0, carrinho.getTotal(), DELTA);
            assertEquals(1, carrinho.getQuantidadeItens());
        }

        @Test
        @DisplayName("Remover produto que não está no carrinho não altera o total")
        void naoDeveRemoverProdutoAusente() {
            carrinho.adicionar(new Produto("Teclado", 100.0));

            assertFalse(carrinho.remover(new Produto("Monitor", 900.0)));
            assertEquals(100.0, carrinho.getTotal(), DELTA);
        }
    }

    @Nested
    @DisplayName("Requisito 5 - Produto com preço inválido")
    class Requisito5 {

        @Test
        @DisplayName("Produto com preço negativo lança exceção e não entra no carrinho")
        void naoDeveAceitarPrecoNegativo() {
            Produto invalido = new Produto("Teclado", -100.0);

            IllegalArgumentException erro = assertThrows(
                    IllegalArgumentException.class,
                    () -> carrinho.adicionar(invalido));

            assertEquals("Preço do produto não pode ser negativo", erro.getMessage());
            assertEquals(0.0, carrinho.getTotal(), DELTA);
            assertEquals(0, carrinho.getQuantidadeItens());
        }

        @Test
        @DisplayName("Produto com preço zero (brinde) é aceito")
        void deveAceitarPrecoZero() {
            carrinho.adicionar(new Produto("Brinde", 0.0));

            assertEquals(1, carrinho.getQuantidadeItens());
            assertEquals(0.0, carrinho.getTotal(), DELTA);
        }
    }

    @Nested
    @DisplayName("Requisito 6 - Desconto de 10% a partir de R$ 500,00")
    class Requisito6 {

        @Test
        @DisplayName("Subtotal R$ 600,00 -> desconto R$ 60,00 -> total R$ 540,00")
        void deveAplicarDescontoAcimaDe500() {
            carrinho.adicionar(new Produto("Monitor", 600.0));

            assertEquals(600.0, carrinho.getSubtotal(), DELTA);
            assertEquals(60.0, carrinho.getDesconto(), DELTA);
            assertEquals(540.0, carrinho.getTotal(), DELTA);
        }

        @Test
        @DisplayName("Fronteira R$ 499,99 -> sem desconto")
        void naoDeveAplicarDescontoEm49999() {
            carrinho.adicionar(new Produto("Cadeira", 499.99));

            assertEquals(0.0, carrinho.getDesconto(), DELTA);
            assertEquals(499.99, carrinho.getTotal(), DELTA);
        }

        @Test
        @DisplayName("Fronteira R$ 500,00 -> com desconto (total R$ 450,00)")
        void deveAplicarDescontoEm50000() {
            carrinho.adicionar(new Produto("Cadeira", 500.00));

            assertEquals(50.0, carrinho.getDesconto(), DELTA);
            assertEquals(450.0, carrinho.getTotal(), DELTA);
        }

        @Test
        @DisplayName("Fronteira R$ 500,01 -> com desconto (total R$ 450,01)")
        void deveAplicarDescontoEm50001() {
            carrinho.adicionar(new Produto("Cadeira", 500.01));

            assertEquals(50.00, carrinho.getDesconto(), DELTA);
            assertEquals(450.01, carrinho.getTotal(), DELTA);
        }

        @Test
        @DisplayName("Desconto vale para a soma de vários produtos")
        void deveAplicarDescontoNaSomaDosProdutos() {
            carrinho.adicionar(new Produto("Teclado", 300.0));
            carrinho.adicionar(new Produto("Mouse", 200.0));

            assertEquals(450.0, carrinho.getTotal(), DELTA);
        }

        @Test
        @DisplayName("Desconto deixa de valer quando uma remoção baixa o subtotal de R$ 500,00")
        void deveRemoverDescontoQuandoSubtotalCai() {
            Produto mouse = new Produto("Mouse", 50.0);
            carrinho.adicionar(new Produto("Cadeira", 480.0));
            carrinho.adicionar(mouse);
            assertEquals(477.0, carrinho.getTotal(), DELTA);

            carrinho.remover(mouse);

            assertEquals(480.0, carrinho.getTotal(), DELTA);
        }
    }
}
