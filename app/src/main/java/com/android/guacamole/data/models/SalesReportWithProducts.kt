package com.android.guacamole.data.models

data class SalesReportWithProducts(
    val totalVentas: Double,
    val ventasTarjeta: Double,
    val ventasEfectivo: Double,
    val productos: List<ProductReport>
)
