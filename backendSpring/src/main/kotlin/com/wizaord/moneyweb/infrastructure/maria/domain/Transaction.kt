package com.wizaord.moneyweb.infrastructure.maria.domain

import com.wizaord.moneyweb.configuration.toDate
import com.wizaord.moneyweb.configuration.toLocalDate
import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.Debit
import com.wizaord.moneyweb.domain.transactions.Transaction
import com.wizaord.moneyweb.domain.transactions.ventilations.CreditVentilation
import com.wizaord.moneyweb.domain.transactions.ventilations.DebitVentilation
import java.util.*
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Transaction(
        @Id var id: String? = null
) {

    var amount: Double? = null
    var userLibelle: String? = null
    var bankLibelle: String? = null
    var bankDetail: String? = null
    var isPointe: Boolean? = null
    var dateCreation: Date? = null
    var isCredit: Boolean? = null
    var accountInternalId: String? = null

    @Convert(converter = VentilationConverterJson::class)
    @Column(columnDefinition = "json")
    var ventilations: Ventilations? = null

    fun toDomain(): Transaction {
        if (this.amount!! >= 0) {
            return createCredit()
        } else {
            return createDebit()
        }
    }

    private fun createCredit(): Credit {
        val credit = Credit(userLibelle!!, bankLibelle!!, bankDetail, amount!!, isPointe!!, id!!, dateCreation!!.toLocalDate())
        ventilations!!.ventilations!!.forEach { credit.addVentilation(it.toDomain()) }
        return credit
    }

    private fun createDebit(): Debit {
        val debit = Debit(userLibelle!!, bankLibelle!!, bankDetail, amount!!, isPointe!!, id!!, dateCreation!!.toLocalDate())
        ventilations!!.ventilations!!.forEach { debit.addVentilation(it.toDomain()) }
        return debit
    }


    companion object {
        fun fromDomain(transaction: Transaction): com.wizaord.moneyweb.infrastructure.maria.domain.Transaction {
            val t = Transaction(transaction.id)
            t.amount = transaction.amount
            t.userLibelle = transaction.userLibelle
            t.bankLibelle = transaction.bankLibelle
            t.bankDetail = transaction.bankDetail
            t.isPointe = transaction.isPointe
            t.dateCreation = transaction.dateCreation.toDate()
            t.isCredit = (transaction.amount >= 0)
            t.ventilations = Ventilations.fromDomain(transaction.ventilations)
            return t
        }
    }
}

data class Ventilations(
        var ventilations: MutableList<Ventilation>? = mutableListOf()
) {

    companion object {
        fun fromDomain(v: List<com.wizaord.moneyweb.domain.transactions.ventilations.Ventilation>): Ventilations {
            val ventilations = Ventilations()
            v.map { Ventilation.fromDomain(it) }
                    .forEach { ventilations.ventilations!!.add(it) }
            return ventilations
        }
    }

}

data class Ventilation(
        var id: String? = null,
        var amount: Double? = null,
        var categoryId: String? = null,
        var isCredit: Boolean? = null
) {

    companion object {
        fun fromDomain(ventilation: com.wizaord.moneyweb.domain.transactions.ventilations.Ventilation): Ventilation {
            return Ventilation(ventilation.id,
                    ventilation.amount,
                    ventilation.categoryId,
                    (ventilation.amount >= 0))
        }
    }

    fun toDomain(): com.wizaord.moneyweb.domain.transactions.ventilations.Ventilation {
        return when (isCredit!!) {
            true -> createCreditVentilation()
            false -> createDebitVentilation()
        }
    }

    private fun createCreditVentilation(): CreditVentilation {
        return CreditVentilation(amount!!, categoryId)
    }

    private fun createDebitVentilation(): DebitVentilation {
        return DebitVentilation(amount!!, categoryId)
    }
}