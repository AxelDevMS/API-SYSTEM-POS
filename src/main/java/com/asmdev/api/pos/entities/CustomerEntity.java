package com.asmdev.api.pos.entities;


import com.asmdev.api.pos.utils.status.Status;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String lastname;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<SaleEntity> sales;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}
