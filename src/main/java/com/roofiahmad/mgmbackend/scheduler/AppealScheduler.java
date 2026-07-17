package com.roofiahmad.mgmbackend.scheduler;

import com.roofiahmad.mgmbackend.model.internet.Appeal;
import com.roofiahmad.mgmbackend.model.intranet.AppealStatus;
import com.roofiahmad.mgmbackend.model.intranet.IntranetAppealWorkflow;
import com.roofiahmad.mgmbackend.repository.internet.AppealRepository;
import com.roofiahmad.mgmbackend.repository.intranet.IntranetAppealWorkflowRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class AppealScheduler {

    private static final Logger log = LoggerFactory.getLogger(AppealScheduler.class);

    private final AppealRepository appealRepository;
    private final IntranetAppealWorkflowRepository workflowRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional(transactionManager = "intranetTransactionManager")
    public void syncInternetToIntranetJob() {
        log.info("Starting Sync Job: internet_db -> intranet_db");

        List<Appeal> internetAppeals = appealRepository.findAll();
        int syncCount = 0;

        for (Appeal appeal : internetAppeals) {
            if (!workflowRepository.existsById(appeal.getId())) {
                IntranetAppealWorkflow workflow = new IntranetAppealWorkflow();
                workflow.setId(appeal.getId());
                workflow.setStatus(AppealStatus.OPEN);

                workflow.setCustomerName(appeal.getCustomerName());
                workflow.setSubject(appeal.getSubject());

                workflowRepository.save(workflow);
                syncCount++;
            }
        }

        log.info("Sync Job Finished. Total records synced: {}", syncCount);
    }

    @Scheduled(fixedRate = 60000)
    @Transactional(transactionManager = "intranetTransactionManager")
    public void autoCloseAppealsJob() {
        log.info("Starting Auto-Close Job");
        LocalDateTime thresholdTime = LocalDateTime.now().minusMinutes(1);

        List<IntranetAppealWorkflow> expiredAppeals = workflowRepository
                .findByStatusAndRespondedAtBefore(AppealStatus.AWAITING_CUSTOMER, thresholdTime);

        int closedCount = 0;
        for (IntranetAppealWorkflow workflow : expiredAppeals) {
            workflow.setStatus(AppealStatus.CLOSED);
            workflow.setClosedAt(LocalDateTime.now());
            workflow.setCloseReason("auto-closed: no customer reply");

            workflowRepository.save(workflow);
            closedCount++;
            log.info("Appeal ID {} has been auto-closed.", workflow.getId());
        }

        log.info("Auto-Close Job Finished. Total records closed: {}", closedCount);
    }
}
