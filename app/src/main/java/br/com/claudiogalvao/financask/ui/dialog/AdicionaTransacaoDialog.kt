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

class AdicionaTransacaoDialog(private val context: Context,
                              private val viewGroup: ViewGroup) {
    private val viewCriada = criaLayout()

    fun configuraDialog(transacaoDelegate: TransacaoDelegate) {
        configuraCampoData()
        configuraCampoCategoria()
        configuraFormulario(transacaoDelegate)
    }

    private fun configuraFormulario(transacaoDelegate: TransacaoDelegate) {
        AlertDialog.Builder(context)
            .setTitle(R.string.adiciona_receita)
            .setView(viewCriada)
            .setPositiveButton("Adicionar") { _: DialogInterface, _: Int ->
                val valorEmTexto = viewCriada.form_transacao_valor.text.toString()
                val dataEmTexto = viewCriada.form_transacao_data.text.toString()
                val categoriaEmTexto = viewCriada.form_transacao_categoria.selectedItem.toString()

                val valor = converteCampoValor(valorEmTexto)

                val data = dataEmTexto.converteParaCalendar()

                val novaTrasacao = Transacao(
                    tipo = Tipo.RECEITA, valor = valor,
                    data = data, categoria = categoriaEmTexto
                )

                transacaoDelegate.delegate(novaTrasacao)

            }
            .setNegativeButton("Cancelar", null)
            .show()
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

    private fun configuraCampoCategoria() {
        val adapter = ArrayAdapter.createFromResource(
            context,
            R.array.categorias_de_receita,
            android.R.layout.simple_spinner_dropdown_item
        )

        viewCriada.form_transacao_categoria.adapter = adapter
    }

    private fun configuraCampoData() {
        val hoje = Calendar.getInstance()

        val ano = hoje[Calendar.YEAR]
        val mes = hoje[Calendar.MONTH]
        val dia = hoje[Calendar.DAY_OF_MONTH]

        viewCriada.form_transacao_data
            .setText(hoje.formataParaBrasileiro())
        viewCriada.form_transacao_data
            .setOnClickListener {
                DatePickerDialog(
                    context,
                    DatePickerDialog.OnDateSetListener { _, ano, mes, dia ->
                        val dataSelecionada = Calendar.getInstance()
                        dataSelecionada.set(ano, mes, dia)
                        viewCriada.form_transacao_data
                            .setText(dataSelecionada.formataParaBrasileiro())
                    }, ano, mes, dia
                ).show()
            }
    }

    fun criaLayout() : View {
        return LayoutInflater.from(context)
            .inflate(R.layout.form_transacao, viewGroup, false)
    }
}