package com.asmdev.api.pos.utils.status;

public enum PurchaseStatus {
    COMPLETED,  // La compra fue finalizada correctamente.
    CANCELED,   // La compra fue anulada antes de completarse.
    DRAFT,      // La compra está en borrador (aún no finalizada ni aprobada).
    DELETED     // La compra fue eliminada (lógica, no física).
}
