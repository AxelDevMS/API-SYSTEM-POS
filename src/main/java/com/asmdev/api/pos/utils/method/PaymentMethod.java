package com.asmdev.api.pos.utils.method;

public enum PaymentMethod {
    CASH,
    CREDIT,
    DEBIT,
    TRANSFER,        // Transferencia bancaria
    CHECK,           // Cheque
    MOBILE_PAYMENT,  // Pago móvil (Apple Pay, Google Pay)
    VOUCHER,         // Vale físico o digital
    COUPON,          // Cupón de descuento como forma de pago
    GIFT_CARD,       // Tarjeta de regalo
    DIGITAL_WALLET,  // Wallets como MercadoPago, PayPal, etc.
    CRYPTO           // Criptomonedas (opcional si tu negocio lo acepta)
}
