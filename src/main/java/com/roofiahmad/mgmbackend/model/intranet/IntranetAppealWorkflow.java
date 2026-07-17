package com.roofiahmad.mgmbackend.model.intranet;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "intranet_appeal_workflows")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IntranetAppealWorkflow {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private AppealStatus status;

    private String customerName;
    private String subject;

    private String officerResponse;
    private LocalDateTime respondedAt;
    private LocalDateTime closedAt;
    private String closeReason;

}