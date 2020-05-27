package br.com.claudiogalvao.financask.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.model.Tipo
import br.com.claudiogalvao.financask.model.Transacao
import br.com.claudiogalvao.financask.ui.adapter.ListaTransacoesAdapter
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import java.math.BigDecimal
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        val transacoes: List<Transacao> = transacoesDeExemplo()

        configuraLista(transacoes)
    }

    private fun configuraLista(transacoes: List<Transacao>) {
        lista_transacoes_listview.adapter = ListaTransacoesAdapter(transacoes, this)
    }

    private fun transacoesDeExemplo(): List<Transacao> {
        return listOf(
            Transacao(valor = BigDecimal(200.0), categoria = "Mercado", tipo = Tipo.DESPESA),
            Transacao(valor = BigDecimal(150.0), categoria = "Economia", tipo = Tipo.RECEITA),
            Transacao(
                valor = BigDecimal(550.50),
                categoria = "Almoço de domingo a tarde",
                tipo = Tipo.DESPESA
            )
        )
    }
}
