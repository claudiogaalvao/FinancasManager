package br.com.claudiogalvao.financask.model

import java.math.BigDecimal
import java.util.Calendar

class Transacao(val valor: BigDecimal,
                val categoria: String,
                val tipo: Tipo,
                val data: Calendar = Calendar.getInstance())