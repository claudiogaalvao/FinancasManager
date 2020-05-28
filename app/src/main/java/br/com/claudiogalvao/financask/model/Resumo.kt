package br.com.claudiogalvao.financask.model

import java.math.BigDecimal

class Resumo(private val transacoes: List<Transacao>) {

    val receita get() = somaPorTipo(Tipo.RECEITA)

    val despesa get() = somaPorTipo(Tipo.DESPESA)

    val total get() = receita.subtract(despesa)

    private fun somaPorTipo(tipo: Tipo) : BigDecimal {
        val totalSomaPorTipo = BigDecimal(
            transacoes
                .filter { transacao -> transacao.tipo == tipo }
                .sumByDouble { transacao -> transacao.valor.toDouble() }
        )
        return totalSomaPorTipo
    }
}