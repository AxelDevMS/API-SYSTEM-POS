package com.asmdev.api.pos.utils.method;

public enum InventoryMovementType {
    INBOUND,        // Entrada general
    OUTBOUND,       // Salida general
    PURCHASE,       // Entrada por compra a proveedor
    SALE,           // Salida por venta al cliente
    RETURN_PURCHASE,// Devolución a proveedor
    RETURN_SALE,    // Devolución de cliente
    ADJUSTMENT,     // Ajuste manual de inventario
    TRANSFER_IN,    // Entrada desde otra ubicación
    TRANSFER_OUT,   // Salida hacia otra ubicación
    DAMAGE,         // Pérdida por daño o caducidad
    THEFT,          // Pérdida por robo
    INVENTORY_COUNT // Ajuste por conteo físico (reconteo)
}
