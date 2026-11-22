package br.edu.exemplo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContaBancariaTest {

    // ---------------------------------------------------------------
    // Casos de teste fornecidos pelo professor (não alterados)
    // ---------------------------------------------------------------

    @Test
    void deveDepositarValorNaConta() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    void deveAcumularDepositos() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.depositar(50);
        assertEquals(150, conta.getSaldo(), 0.001);
    }

    @Test
    void deveSacarValorDaConta() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.sacar(40);
        assertEquals(60, conta.getSaldo(), 0.001);
    }

    @Test
    void naoDevePermitirSaqueMaiorQueSaldo() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);
        conta.sacar(150);
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    // ---------------------------------------------------------------
    // Casos de teste adicionais
    // ---------------------------------------------------------------

    @Test
    @DisplayName("Conta nova começa com saldo zero")
    void contaNovaDeveTerSaldoZero() {
        assertEquals(0, new ContaBancaria().getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Depósito válido retorna true")
    void depositoValidoDeveRetornarVerdadeiro() {
        ContaBancaria conta = new ContaBancaria();
        assertTrue(conta.depositar(10));
    }

    @Test
    @DisplayName("Depósito de valor zero é recusado e não altera o saldo")
    void naoDeveDepositarValorZero() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        assertFalse(conta.depositar(0));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Depósito de valor negativo é recusado e não altera o saldo")
    void naoDeveDepositarValorNegativo() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        assertFalse(conta.depositar(-30));
        assertEquals(100, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Saque maior que o saldo retorna false")
    void saqueMaiorQueSaldoDeveRetornarFalso() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        assertFalse(conta.sacar(100.01));
    }

    @Test
    @DisplayName("Saque de todo o saldo é permitido e zera a conta (fronteira)")
    void devePermitirSacarSaldoTotal() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        assertTrue(conta.sacar(100));
        assertEquals(0, conta.getSaldo(), 0.001);
    }

    @Test
    @DisplayName("Saque de valor zero ou negativo é recusado")
    void naoDeveSacarValorZeroOuNegativo() {
        ContaBancaria conta = new ContaBancaria();
        conta.depositar(100);

        assertFalse(conta.sacar(0));
        assertFalse(conta.sacar(-20));
        assertEquals(100, conta.getSaldo(), 0.001);
    }
}
