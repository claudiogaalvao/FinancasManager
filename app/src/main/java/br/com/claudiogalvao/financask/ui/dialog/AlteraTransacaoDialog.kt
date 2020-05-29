package br.com.claudiogalvao.financask.ui.dialog

import android.content.Context
import android.view.ViewGroup
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.extension.formataParaBrasileiro
import br.com.claudiogalvao.financask.model.Tipo
import br.com.claudiogalvao.financask.model.Transacao

class AlteraTransacaoDialog(
    context: Context,
    viewGroup: ViewGroup) : FormularioTransacaoDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Alterar"

    fun configuraDialog(transacao: Transacao, delegate: (transacao: Transacao) -> Unit) {
        val tipo = transacao.tipo
        super.configuraDialog(transacao.tipo, delegate)
        inicializaCampos(transacao)
    }

    private fun inicializaCampos(transacao: Transacao) {
        inicializaCampoValor(transacao)
        inicializaCampoData(transacao)
        inicializaCampoCategorias(transacao)
    }

    private fun inicializaCampoCategorias(transacao: Transacao) {
        val categorias = context.resources.getStringArray(categoriasPorTipo(transacao.tipo))
        val posicaoCategoria = categorias.indexOf(transacao.categoria)
        campoCategoria.setSelection(posicaoCategoria)
    }

    private fun inicializaCampoData(transacao: Transacao) {
        campoData.setText(transacao.data.formataParaBrasileiro())
    }

    private fun inicializaCampoValor(transacao: Transacao) {
        campoValor.setText(transacao.valor.toString())
    }

    override fun tituloPorTipo(tipo: Tipo): Int {
        if (tipo == Tipo.RECEITA) {
            return R.string.altera_receita
        }
        return R.string.altera_despesa
    }
}