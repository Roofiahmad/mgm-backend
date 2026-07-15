package com.roofiahmad.mgmbackend.model.intranet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "intranet_appeal_workflows")
@Getter
@Setter
public class IntranetAppealWorkflow {
    @Id
    private Long id; // ID disamakan dengan ID dari internet_db untuk kemudahan mapping/sync

    @Enumerated(EnumType.STRING)
    private AppealStatus status;

    private String customerName;
    private String subject;

    private String officerResponse;
    private LocalDateTime respondedAt;
    private LocalDateTime closedAt;
    private String closeReason;

//    // Getters and Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//    public AppealStatus getStatus() { return status; }
//    public void setStatus(AppealStatus status) { this.status = status; }
//    public String getOfficerResponse() { return officerResponse; }
//    public void setOfficerResponse(String officerResponse) { this.officerResponse = officerResponse; }
//    public LocalDateTime getRespondedAt() { return respondedAt; }
//    public void setRespondedAt(LocalDateTime respondedAt) { this.respondedAt = respondedAt; }
//    public LocalDateTime getClosedAt() { return closedAt; }
//    public void setClosedAt(LocalDateTime closedAt) { this.closedAt = closedAt; }
//    public String getCloseReason() { return closeReason; }
//    public void setCloseReason(String closeReason) { this.closeReason = closeReason; }
}