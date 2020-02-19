package com.wizaord.moneyweb.infrastructure.domain

import com.wizaord.moneyweb.domain.transactions.Credit
import com.wizaord.moneyweb.domain.transactions.ventilations.CreditVentilation
import com.wizaord.moneyweb.domain.transactions.ventilations.DebitVentilation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate

@Document
abstract class Transaction (
        @Id val id: String
) {

    var amount: Double? = null
    var userLibelle: String? = null
    var bankLibelle: String? = null
    var bankDetail: String? = null
    var isPointe: Boolean? = null
    var dateCreation: LocalDate? = null
    var accountInternalId: String? = null
    var ventilations = mutableListOf<Ventilation>()

    fun loadTransaction(transaction: com.wizaord.moneyweb.domain.transactions.Transaction){
        this.amount = transaction.amount
        this.userLibelle = transaction.userLibelle
        this.bankLibelle = transaction.bankLibelle
        this.bankDetail = transaction.bankDetail
        this.isPointe = transaction.isPointe
        this.dateCreation = transaction.dateCreation
        transaction.ventilations.forEach { ventilation ->
            val ventilationInfra = when (ventilation) {
                is DebitVentilation -> com.wizaord.moneyweb.infrastructure.domain.DebitVentilation.fromDomain(ventilation)
                is CreditVentilation -> com.wizaord.moneyweb.infrastructure.domain.CreditVentilation.fromDomain(ventilation)
                else -> error("Not possible")
            }
            this.ventilations.add(ventilationInfra)
        }
    }

    abstract fun toDomain(): com.wizaord.moneyweb.domain.transactions.Transaction

}

class Debit(id: String) : Transaction(id) {
    companion object {
        fun fromDomain(debit: com.wizaord.moneyweb.domain.transactions.Debit): Debit {
            val c = Debit(debit.id)
            c.loadTransaction(debit)
            return c
        }
    }

    override fun toDomain(): com.wizaord.moneyweb.domain.transactions.Debit {
        return com.wizaord.moneyweb.domain.transactions.Debit(userLibelle!!, bankLibelle!!, bankDetail, amount!!, isPointe!!, id, dateCreation!!)
    }
}
class Credit(id: String) : Transaction(id) {
    companion object {
        fun fromDomain(credit: Credit): com.wizaord.moneyweb.infrastructure.domain.Credit {
            val c = Credit(credit.id)
            c.loadTransaction(credit)
            return c
        }
    }

    override fun toDomain(): com.wizaord.moneyweb.domain.transactions.Credit {
        return Credit(userLibelle!!, bankLibelle!!, bankDetail, amount!!, isPointe!!, id, dateCreation!!)
    }
}

abstract class Ventilation(
        val amount: Double,
        val categoryId: String?
)

class DebitVentilation(amount: Double, categoryId: String?) : Ventilation(amount, categoryId) {
    companion object {
        fun fromDomain(ventilation: DebitVentilation) =
                com.wizaord.moneyweb.infrastructure.domain.DebitVentilation(ventilation.amount, ventilation.category?.id)
    }
}
class CreditVentilation(amount: Double, categoryId: String?) : Ventilation(amount, categoryId) {
    companion object {
        fun fromDomain(ventilation: CreditVentilation) =
                com.wizaord.moneyweb.infrastructure.domain.CreditVentilation(ventilation.amount, ventilation.category?.id)
    }
}