package com.asmdev.api.pos.persistence.entity;


import com.asmdev.api.pos.utils.status.PurchaseStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sale")
public class SaleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private PurchaseStatus status;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItemEntity> items;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

}
