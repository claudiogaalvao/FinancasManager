package br.com.claudiogalvao.financask.ui.dialog

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.claudiogalvao.financask.R
import br.com.claudiogalvao.financask.delegate.TransacaoDelegate
import br.com.claudiogalvao.financask.extension.converteParaCalendar
import br.com.claudiogalvao.financask.extension.formataParaBrasileiro
import br.com.claudiogalvao.financask.model.Tipo
import br.com.claudiogalvao.financask.model.Transacao
import kotlinx.android.synthetic.main.activity_lista_transacoes.*
import kotlinx.android.synthetic.main.form_transacao.view.*
import java.lang.NumberFormatException
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

class AlteraTransacaoDialog(private val context: Context,
                            private val viewGroup: ViewGroup) {
    private val viewCriada = criaLayout()
    private val campoValor = viewCriada.form_transacao_valor
    private val campoData = viewCriada.form_transacao_data
    private val campoCategoria = viewCriada.form_transacao_categoria

    fun configuraDialog(transacao: Transacao, transacaoDelegate: TransacaoDelegate) {
        val tipo = transacao.tipo


        configuraCampoData()
        configuraCampoCategoria(tipo)
        configuraFormulario(tipo, transacaoDelegate)

        campoValor.setText(transacao.valor.toString())
        campoData.setText(transacao.data.formataParaBrasileiro())
        val categorias = context.resources.getStringArray(categoriasPorTipo(transacao.tipo))
        val posicaoCategoria = categorias.indexOf(transacao.categoria)
        campoCategoria.setSelection(posicaoCategoria)
    }

    private fun configuraFormulario(tipo: Tipo, transacaoDelegate: TransacaoDelegate) {
        val titulo = tituloPorTipo(tipo)

        AlertDialog.Builder(context)
            .setTitle(titulo)
            .setView(viewCriada)
            .setPositiveButton("Alterar") { _: DialogInterface, _: Int ->
                val valorEmTexto = campoValor.text.toString()
                val dataEmTexto = campoData.text.toString()
                val categoriaEmTexto = campoCategoria.selectedItem.toString()

                val valor = converteCampoValor(valorEmTexto)
                val data = dataEmTexto.converteParaCalendar()

                val novaTrasacao = Transacao(
                    tipo = tipo, valor = valor,
                    data = data, categoria = categoriaEmTexto
                )

                transacaoDelegate.delegate(novaTrasacao)

            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun tituloPorTipo(tipo: Tipo): Int {
        if (tipo == Tipo.RECEITA) {
            return R.string.altera_receita
        }
        return R.string.altera_despesa
    }

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

    private fun categoriasPorTipo(tipo: Tipo): Int {
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

    private fun criaLayout() : View {
        return LayoutInflater.from(context)
            .inflate(R.layout.form_transacao, viewGroup, false)
    }
}