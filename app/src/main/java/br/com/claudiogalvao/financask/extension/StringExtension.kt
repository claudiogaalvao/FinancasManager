package br.com.claudiogalvao.financask.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.limitaEmAte(caracteres: Int): String {

    if(this.length > caracteres) return "${this.substring(0, caracteres)}..."

    return this
}

fun String.converteParaCalendar() : Calendar {
    val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val dataFormatada = simpleDateFormat.parse(this)
    val data = Calendar.getInstance()
    data.time = dataFormatada
    return data
}