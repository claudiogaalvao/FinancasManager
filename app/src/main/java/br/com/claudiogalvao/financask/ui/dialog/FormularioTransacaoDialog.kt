package br.com.claudiogalvao.financask.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.extension.converteParaCalendar
import br.com.claudiogalvao.financask.extension.formataParaBrasileiro
import br.com.claudiogalvao.financask.model.Tipo
import br.com.claudiogalvao.financask.model.Transacao
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.math.BigDecimal
import java.util.*

abstract class FormularioTransacaoDialog(
    val context: Context,
    val viewGroup: ViewGroup?
) {
    private val viewCriada = criaLayout()
    protected val campoValor = viewCriada.form_transacao_valor
    protected val campoData = viewCriada.form_transacao_data
    protected val campoCategoria = viewCriada.form_transacao_categoria
    protected abstract val tituloBotaoPositivo: String

    fun configuraDialog(tipo: Tipo, delegate: (transacao: Transacao) -> Unit) {
        configuraCampoData()
        configuraCampoCategoria(tipo)
        configuraFormulario(tipo, delegate)
    }

    private fun configuraFormulario(tipo: Tipo, delegate: (transacao: Transacao) -> Unit) {
        val titulo = tituloPorTipo(tipo)

        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
            .setPositiveButton(tituloBotaoPositivo) { _: DialogInterface, _: Int ->
                val valorEmTexto = campoValor.text.toString()
                val dataEmTexto = campoData.text.toString()
                val categoriaEmTexto = campoCategoria.selectedItem.toString()

                val valor = converteCampoValor(valorEmTexto)
                val data = dataEmTexto.converteParaCalendar()

                val novaTrasacao = Transacao(
                    tipo = tipo, valor = valor,
                    data = data, categoria = categoriaEmTexto
                )

                delegate(novaTrasacao)

            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    protected abstract fun tituloPorTipo(tipo: Tipo): Int

    private fun converteCampoValor(valorEmTexto: String): BigDecimal {
        return try {
            BigDecimal(valorEmTexto)
        } catch (Exception: NumberFormatException) {
            Toast.makeText(
                context,
                "Falha na conversão do valor",
                Toast.LENGTH_LONG
            )
            BigDecimal.ZERO
        }
    }

    private fun configuraCampoCategoria(tipo: Tipo) {
        val categorias = categoriasPorTipo(tipo)

        val adapter = ArrayAdapter.createFromResource(
            context,
            categorias,
            android.R.layout.simple_spinner_dropdown_item
        )

        campoCategoria.adapter = adapter
    }

    protected fun categoriasPorTipo(tipo: Tipo): Int {
        if (tipo == Tipo.RECEITA) {
            return R.array.categorias_de_receita
        }
        return R.array.categorias_de_despesa
    }

    private fun configuraCampoData() {
        val hoje = Calendar.getInstance()

        val ano = hoje[Calendar.YEAR]
        val mes = hoje[Calendar.MONTH]
        val dia = hoje[Calendar.DAY_OF_MONTH]

        campoData
            .setText(hoje.formataParaBrasileiro())
        campoData
            .setOnClickListener {
                DatePickerDialog(
                    context,
                    { _, ano, mes, dia ->
                        val dataSelecionada = Calendar.getInstance()
                        dataSelecionada.set(ano, mes, dia)
                        campoData.setText(dataSelecionada.formataParaBrasileiro())
                    }, ano, mes, dia
                ).show()
            }
    }

    fun criaLayout() : View {
        return LayoutInflater.from(context)
            .inflate(R.layout.form_transacao, viewGroup, false)
    }
}