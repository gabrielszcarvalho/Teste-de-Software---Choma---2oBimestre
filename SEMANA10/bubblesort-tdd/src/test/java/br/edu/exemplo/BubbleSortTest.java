package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BubbleSortTest {

    // ---------------------------------------------------------------
    // Casos de teste fornecidos pelo professor (não alterados)
    // ---------------------------------------------------------------

    @Test
    void deveOrdenarVetorDesordenado() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{5, 3, 1, 4, 2});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                bubbleSort.getNumeros()
        );
    }

    @Test
    void deveManterVetorJaOrdenado() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{1, 2, 3, 4, 5});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{1, 2, 3, 4, 5},
                bubbleSort.getNumeros()
        );
    }

    @Test
    void deveOrdenarVetorComValoresRepetidos() {
        BubbleSort bubbleSort =
                new BubbleSort(new int[]{3, 1, 3, 2, 1});

        bubbleSort.ordenar();

        assertArrayEquals(
                new int[]{1, 1, 2, 3, 3},
                bubbleSort.getNumeros()
        );
    }

    // ---------------------------------------------------------------
    // Casos de teste adicionais (casos de borda)
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Vetor em ordem decrescente (pior caso) deve ficar crescente")
    void deveOrdenarVetorEmOrdemDecrescente() {
        BubbleSort bubbleSort = new BubbleSort(new int[]{9, 7, 5, 3, 1});

        bubbleSort.ordenar();

        assertArrayEquals(new int[]{1, 3, 5, 7, 9}, bubbleSort.getNumeros());
    }

    @Test
    @DisplayName("Números negativos e zero devem ser ordenados corretamente")
    void deveOrdenarVetorComNegativos() {
        BubbleSort bubbleSort = new BubbleSort(new int[]{0, -4, 8, -1, 3});

        bubbleSort.ordenar();

        assertArrayEquals(new int[]{-4, -1, 0, 3, 8}, bubbleSort.getNumeros());
    }

    @Test
    @DisplayName("Vetor vazio continua vazio")
    void deveAceitarVetorVazio() {
        BubbleSort bubbleSort = new BubbleSort(new int[]{});

        bubbleSort.ordenar();

        assertArrayEquals(new int[]{}, bubbleSort.getNumeros());
    }

    @Test
    @DisplayName("Vetor com um único elemento não muda")
    void deveAceitarVetorComUmElemento() {
        BubbleSort bubbleSort = new BubbleSort(new int[]{42});

        bubbleSort.ordenar();

        assertArrayEquals(new int[]{42}, bubbleSort.getNumeros());
    }

    @Test
    @DisplayName("Vetor nulo não é aceito")
    void naoDeveAceitarVetorNulo() {
        assertThrows(IllegalArgumentException.class, () -> new BubbleSort(null));
    }
}
