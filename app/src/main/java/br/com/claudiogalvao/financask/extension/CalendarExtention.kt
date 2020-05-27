package br.com.claudiogalvao.financask.extension

import java.text.SimpleDateFormat
import java.util.*

fun Calendar.formataParaBrasileiro(): String {
    val padraoBrasileiro = "dd/MM/yyyy"
    val pattern = SimpleDateFormat(padraoBrasileiro)
    return pattern.format(this.time)
}