package br.com.claudiogalvao.financask.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.extension.formataParaBrasileiro
import br.com.claudiogalvao.financask.extension.limitaEmAte
import br.com.claudiogalvao.financask.model.Tipo
import br.com.claudiogalvao.financask.model.Transacao
import kotlinx.android.synthetic.main.transacao_item.view.*
import java.text.DecimalFormat
import java.util.*

class ListaTransacoesAdapter(private val transacoes: List<Transacao>,
                             private val contexto: Context)
    : BaseAdapter() {

    private val limiteDaCategoria = 14

    override fun getView(posicao: Int, view: View?, parent: ViewGroup?): View {
        val viewCriada = LayoutInflater.from(contexto).inflate(R.layout.transacao_item, parent, false)

        val transacao = transacoes[posicao]

        adicionaCategoria(viewCriada, transacao)
        adicionaData(viewCriada, transacao)
        adicionaValor(transacao, viewCriada)
        adicionaIcone(transacao, viewCriada)

        return viewCriada
    }

    private fun adicionaCategoria(
        viewCriada: View,
        transacao: Transacao
    ) {
        viewCriada.transacao_categoria.text = transacao.categoria.limitaEmAte(limiteDaCategoria)
    }

    private fun adicionaData(
        viewCriada: View,
        transacao: Transacao
    ) {
        viewCriada.transacao_data.text = transacao.data.formataParaBrasileiro()
    }

    private fun adicionaIcone(
        transacao: Transacao,
        viewCriada: View
    ) {
        val icone = iconePorTipo(transacao.tipo)
        viewCriada.transacao_icone.setBackgroundResource(icone)
    }

    private fun iconePorTipo(
        tipo: Tipo
    ): Int {
        if (tipo == Tipo.RECEITA) {
            return R.drawable.icone_transacao_item_receita
        }
        return R.drawable.icone_transacao_item_despesa
    }

    private fun adicionaValor(
        transacao: Transacao,
        viewCriada: View
    ) {
        val cor = corPorTipo(transacao.tipo)
        viewCriada.transacao_valor.setTextColor(cor)
        viewCriada.transacao_valor.text = transacao.valor.formataParaBrasileiro()
    }

    private fun corPorTipo(
        tipo: Tipo
    ): Int {
        if (tipo == Tipo.RECEITA) {
            return ContextCompat.getColor(contexto, R.color.receita)
        }
        return ContextCompat.getColor(contexto, R.color.despesa)
    }

    override fun getItem(posicao: Int): Transacao {
        return transacoes[posicao]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return transacoes.size
    }
}