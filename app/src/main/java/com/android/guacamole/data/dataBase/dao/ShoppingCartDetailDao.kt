package com.android.guacamole.data.dataBase.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.android.guacamole.data.dataBase.entity.ShoppingCartDetailEntity
import com.android.guacamole.data.models.ProductReport
import com.android.guacamole.data.models.SalesReportWithProducts

@Dao
interface ShoppingCartDetailDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listCategory: List<ShoppingCartDetailEntity>)

    @Transaction
    suspend fun resume(userId: String): SalesReportWithProducts {
        val totalVentas = getTotalVentas(userId)
        val ventasTarjeta = getVentasTarjeta(userId)
        val ventasEfectivo = getVentasEfectivo(userId)
        val productos = getProductos(userId)
        return SalesReportWithProducts(totalVentas, ventasTarjeta, ventasEfectivo, productos)
    }

    @Query("SELECT COUNT(*) FROM ShoppingCartEntity WHERE userId = :userId")
    suspend fun getTotalVentas(userId: String): Double

    @Query("SELECT SUM(CASE WHEN typePayment = 'CARD' THEN 1 ELSE 0 END) FROM ShoppingCartEntity WHERE userId = :userId")
    suspend fun getVentasTarjeta(userId: String): Double

    @Query("SELECT SUM(CASE WHEN typePayment = 'CASH' THEN 1 ELSE 0 END) FROM ShoppingCartEntity WHERE userId = :userId")
    suspend fun getVentasEfectivo(userId: String): Double

    @Query("SELECT scde.nameProduct, SUM(scde.quantity) AS cantidadComprada, SUM(scde.price * scde.quantity) AS totalGastado " +
            "FROM ShoppingCartDetailEntity scde " +
            "LEFT JOIN ShoppingCartEntity sce ON sce.idShoppingCart = scde.idShoppingCart " +
            "WHERE sce.userId = :userId " +
            "GROUP BY scde.nameProduct " +
            "ORDER BY SUM(scde.quantity) DESC")
    suspend fun getProductos(userId: String): List<ProductReport>
}