package br.edu.exemplo;

import br.edu.exemplo.repository.ProdutoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.isA;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/*
 * Exercício 2 - fluxo Controller -> Service -> Repository -> H2 com MockMvc.
 * O MockMvc chama o DispatcherServlet direto, sem subir servidor HTTP real.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProdutoControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProdutoRepository repository;

    // CT04 - Cadastro via API
    @Test
    void deveCadastrarProdutoPelaApi() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Mouse\",\"preco\":80.0}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(isA(Number.class)))
                .andExpect(jsonPath("$.nome").value("Mouse"))
                .andExpect(jsonPath("$.preco").value(80.0));

        assertEquals(1, repository.count());
    }

    // CT05 - Consulta via API
    @Test
    void deveBuscarProdutoPelaApi() throws Exception {
        String resposta = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Teclado\",\"preco\":150.0}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JsonNode json = objectMapper.readTree(resposta);
        long id = json.get("id").asLong();

        mockMvc.perform(get("/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Teclado"))
                .andExpect(jsonPath("$.preco").value(150.0));
    }

    // Casos extras: a API não tem tratamento de erro, então a
    // IllegalArgumentException do service chega até o MockMvc

    @Test
    void naoDeveCadastrarProdutoInvalidoPelaApi() {
        ServletException erro = assertThrows(ServletException.class, () ->
                mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"\",\"preco\":80.0}")));

        assertInstanceOf(IllegalArgumentException.class, erro.getCause());
        assertEquals(0, repository.count());
    }

    @Test
    void naoDeveEncontrarProdutoInexistentePelaApi() {
        ServletException erro = assertThrows(ServletException.class, () ->
                mockMvc.perform(get("/produtos/{id}", 99999)));

        assertEquals("Produto não encontrado", erro.getCause().getMessage());
    }
}
