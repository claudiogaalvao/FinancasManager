package br.com.claudiogalvao.financask.delegate

import br.com.claudiogalvao.financask.model.Transacao

interface TransacaoDelegate {
    fun delegate(transacao: Transacao)
}