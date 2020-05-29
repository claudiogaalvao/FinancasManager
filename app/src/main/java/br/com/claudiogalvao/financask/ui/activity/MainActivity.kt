package br.com.claudiogalvao.financask.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.dao.TransacaoDAO
import br.com.claudiogalvao.financask.model.Tipo
import br.com.claudiogalvao.financask.model.Transacao
import br.com.claudiogalvao.financask.ui.ResumoView
import br.com.claudiogalvao.financask.ui.adapter.ListaTransacoesAdapter
import br.com.claudiogalvao.financask.ui.dialog.AdicionaTransacaoDialog
import br.com.claudiogalvao.financask.ui.dialog.AlteraTransacaoDialog
import kotlinx.android.synthetic.main.activity_lista_transacoes.*

class MainActivity : AppCompatActivity() {

    private val dao: TransacaoDAO = TransacaoDAO()
    private val transacoes = dao.transacoes
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
        dao.adiciona(transacao)
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
        with(lista_transacoes_listview) {
            adapter = ListaTransacoesAdapter(transacoes, this@MainActivity)
            setOnItemClickListener { parent, view, position, id ->
                val transacao = transacoes[position]
                chamaDialogAlteracao(transacao, position)
            }
            setOnCreateContextMenuListener { menu, view, menuInfo ->
                menu.add(Menu.NONE, 1, Menu.NONE, "Remover")
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val itemId = item.itemId
        if(itemId == 1) {
            val adapterContextMenuInfo = item.menuInfo as AdapterView.AdapterContextMenuInfo
            val posicaoDaTransacao = adapterContextMenuInfo.position
            remover(posicaoDaTransacao)
        }
        return super.onContextItemSelected(item)
    }

    private fun remover(posicao: Int) {
        dao.remove(posicao)
        atualizaTransacoes()
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
        dao.altera(transacao, position)
        atualizaTransacoes()
    }
}
