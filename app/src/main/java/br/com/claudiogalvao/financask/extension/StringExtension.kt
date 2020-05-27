package br.com.claudiogalvao.financask.extension

fun String.limitaEmAte(caracteres: Int): String {

    if(this.length > caracteres) return "${this.substring(0, caracteres)}..."

    return this
}