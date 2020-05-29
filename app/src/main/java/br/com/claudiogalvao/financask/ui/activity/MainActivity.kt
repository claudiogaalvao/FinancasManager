package br.com.claudiogalvao.financask.ui.activity

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.model.Tipo
import br.com.claudiogalvao.financask.model.Transacao
import br.com.claudiogalvao.financask.ui.ResumoView
import br.com.claudiogalvao.financask.ui.adapter.ListaTransacoesAdapter
import br.com.claudiogalvao.financask.ui.dialog.AdicionaTransacaoDialog
import br.com.claudiogalvao.financask.ui.dialog.AlteraTransacaoDialog
import kotlinx.android.synthetic.main.activity_lista_transacoes.*

class MainActivity : AppCompatActivity() {

    private val transacoes: MutableList<Transacao> = mutableListOf()
    private val viewDaActivity by lazy {
        window.decorView
    }
    private val viewGroupDaActivity by lazy {
        viewDaActivity as ViewGroup
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_transacoes)

        configuraResumo()
        configuraLista()
        configuraFab()
    }

    private fun configuraFab() {
        lista_transacoes_adiciona_receita
            .setOnClickListener {
                chamaDialogAdicao(Tipo.RECEITA)
            }

        lista_transacoes_adiciona_despesa
            .setOnClickListener {
                chamaDialogAdicao(Tipo.DESPESA)
            }
    }

    private fun chamaDialogAdicao(tipo: Tipo) {
        AdicionaTransacaoDialog(this, viewGroupDaActivity)
            .configuraDialog(tipo) { transacaoCriada ->
                adiciona(transacaoCriada)
                lista_transacoes_adiciona_menu.close(true)
            }
    }

    private fun adiciona(transacao: Transacao) {
        transacoes.add(transacao)
        atualizaTransacoes()
    }

    private fun atualizaTransacoes() {
        configuraLista()
        configuraResumo()
    }

    private fun configuraResumo() {
        val view = viewDaActivity
        val resumoView =
            ResumoView(this, view, transacoes)
        resumoView.atualiza()

    }

    private fun configuraLista() {
        lista_transacoes_listview.adapter = ListaTransacoesAdapter(transacoes, this)
        lista_transacoes_listview.setOnItemClickListener { parent, view, position, id ->
            val transacao = transacoes[position]
            chamaDialogAlteracao(transacao, position)
        }
    }

    private fun chamaDialogAlteracao(
        transacao: Transacao,
        position: Int
    ) {
        AlteraTransacaoDialog(this, viewGroupDaActivity)
            .configuraDialog(transacao) { transacaoAlterada ->
                altera(transacaoAlterada, position)
            }
    }

    private fun altera(
        transacao: Transacao,
        position: Int
    ) {
        transacoes[position] = transacao
        atualizaTransacoes()
    }
}
