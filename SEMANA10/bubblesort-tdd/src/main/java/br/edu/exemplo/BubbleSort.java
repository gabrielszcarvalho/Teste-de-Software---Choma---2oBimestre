package br.edu.exemplo;

public class BubbleSort {

    private int[] numeros;

    public BubbleSort(int[] numeros) {
        if (numeros == null) {
            throw new IllegalArgumentException("O vetor não pode ser nulo");
        }
        this.numeros = numeros;
    }

    public void ordenar() {
        int fim = numeros.length - 1;
        boolean houveTroca = true;

        // a cada passada o maior valor restante "borbulha" para o fim;
        // se uma passada não fizer nenhuma troca, o vetor já está ordenado
        while (houveTroca) {
            houveTroca = false;
            for (int i = 0; i < fim; i++) {
                if (numeros[i] > numeros[i + 1]) {
                    trocar(i, i + 1);
                    houveTroca = true;
                }
            }
            fim--;
        }
    }

    private void trocar(int i, int j) {
        int aux = numeros[i];
        numeros[i] = numeros[j];
        numeros[j] = aux;
    }

    public int[] getNumeros() {
        return numeros;
    }
}
