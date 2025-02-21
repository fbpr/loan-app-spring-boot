package com.enigmacamp.loan_app.entity;

import com.enigmacamp.loan_app.constant.EInstalmentType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "t_instalment_type")
public class InstalmentType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    @Column(name = "instalment_type")
    private EInstalmentType instalmentType;
}
