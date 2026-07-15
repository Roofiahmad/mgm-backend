package com.roofiahmad.mgmbackend.repository.intranet;

import com.roofiahmad.mgmbackend.model.intranet.AppealStatus;
import com.roofiahmad.mgmbackend.model.intranet.IntranetAppealWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IntranetAppealWorkflowRepository extends JpaRepository<IntranetAppealWorkflow, Long> {
    List<IntranetAppealWorkflow> findByStatusAndRespondedAtBefore(AppealStatus status, LocalDateTime time);
}