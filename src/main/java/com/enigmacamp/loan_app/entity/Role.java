package com.enigmacamp.loan_app.entity;

import com.enigmacamp.loan_app.constant.DbBash;
import com.enigmacamp.loan_app.constant.ERole;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DbBash.ROLE_TBL)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private ERole role;
}

