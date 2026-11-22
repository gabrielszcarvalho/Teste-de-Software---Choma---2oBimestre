package br.edu.exemplo;

import br.edu.exemplo.model.Produto;
import br.edu.exemplo.repository.ProdutoRepository;
import br.edu.exemplo.service.ProdutoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/*
 * Exercício 1 - integração Service -> Repository -> H2, sem mocks.
 * @Transactional faz o rollback ao fim de cada teste, então um teste
 * não enxerga os registros criados por outro.
 */
@SpringBootTest
@Transactional
class ProdutoServiceIntegrationTest {

    @Autowired
    ProdutoService service;

    @Autowired
    ProdutoRepository repository;

    // CT01 - Cadastro válido
    @Test
    void deveCadastrarERecuperarProduto() {
        Produto salvo = service.cadastrar("Teclado", 150.0);

        assertNotNull(salvo.getId());

        // busca direto no banco: confirma que o registro foi persistido de fato
        Produto doBanco = repository.findById(salvo.getId()).orElseThrow();
        assertEquals("Teclado", doBanco.getNome());
        assertEquals(150.0, doBanco.getPreco(), 0.001);
    }

    // CT02 - Nome inválido
    @Test
    void naoDevePersistirProdutoComNomeEmBranco() {
        long antes = repository.count();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("   ", 80.0));

        assertEquals(antes, repository.count());
    }

    // CT03 - Atualização (desafio)
    @Test
    void deveAtualizarPrecoPersistido() {
        Produto mouse = service.cadastrar("Mouse", 80.0);

        service.atualizarPreco(mouse.getId(), 95.0);

        Produto doBanco = repository.findById(mouse.getId()).orElseThrow();
        assertEquals(95.0, doBanco.getPreco(), 0.001);
    }

    // Casos extras

    @Test
    void naoDevePersistirProdutoComNomeNulo() {
        long antes = repository.count();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar(null, 80.0));

        assertEquals(antes, repository.count());
    }

    @Test
    void naoDevePersistirProdutoComPrecoZeroOuNegativo() {
        long antes = repository.count();

        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("Mouse", 0));
        assertThrows(IllegalArgumentException.class, () -> service.cadastrar("Mouse", -10));

        assertEquals(antes, repository.count());
    }

    @Test
    void naoDeveAtualizarParaPrecoInvalido() {
        Produto mouse = service.cadastrar("Mouse", 80.0);

        assertThrows(IllegalArgumentException.class, () -> service.atualizarPreco(mouse.getId(), -1));

        assertEquals(80.0, repository.findById(mouse.getId()).orElseThrow().getPreco(), 0.001);
    }

    @Test
    void deveBuscarProdutoCadastrado() {
        Produto salvo = service.cadastrar("Monitor", 900.0);

        Produto encontrado = service.buscar(salvo.getId());

        assertEquals("Monitor", encontrado.getNome());
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        IllegalArgumentException erro =
                assertThrows(IllegalArgumentException.class, () -> service.buscar(99999L));

        assertEquals("Produto não encontrado", erro.getMessage());
    }

    @Test
    void cadaTesteComecaComOBancoVazio() {
        // graças ao rollback do @Transactional, nada dos outros testes ficou no banco
        assertEquals(0, repository.count());
    }
}
