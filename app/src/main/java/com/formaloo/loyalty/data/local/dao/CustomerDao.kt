package com.formaloo.loyalty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.formaloo.loyalty.data.model.customer.Customer

@Dao
abstract class CustomerDao : BaseDao<Customer>() {

    @Query("SELECT * FROM Customer")
    abstract suspend fun getCustomers(): List<Customer>

    @Query("SELECT * FROM Customer WHERE code = :code")
    abstract suspend fun getCustomer(code: String): Customer

    @Query("DELETE FROM Customer")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(customer: Customer) {
        insert(customer)
    }

    suspend fun save(customers: List<Customer>) {
        insert(customers)
    }
}