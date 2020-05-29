package br.com.claudiogalvao.financask.ui.dialog

import android.content.Context
import android.view.ViewGroup
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.model.Tipo

class AdicionaTransacaoDialog(context: Context,
                              viewGroup: ViewGroup) : FormularioTransacaoDialog(context, viewGroup) {

    override val tituloBotaoPositivo: String
        get() = "Adicionar"

    override fun tituloPorTipo(tipo: Tipo): Int {
        if (tipo == Tipo.RECEITA) {
            return R.string.adiciona_receita
        }
        return R.string.adiciona_despesa
    }

}