package br.com.claudiogalvao.financask.dao

import br.com.claudiogalvao.financask.model.Transacao

class TransacaoDAO {

    var transacoes: List<Transacao> = Companion.transacoes
    companion object {
        private var transacoes: MutableList<Transacao> = mutableListOf()
    }

    fun adiciona(transacao: Transacao) {
        Companion.transacoes.add(transacao)
    }

    fun altera(transacao: Transacao, posicao: Int) {
        Companion.transacoes[posicao] = transacao
    }

    fun remove(posicao: Int) {
        Companion.transacoes.removeAt(posicao)
    }
}