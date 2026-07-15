package com.roofiahmad.mgmbackend.controller;

import com.roofiahmad.mgmbackend.model.internet.Appeal;
import com.roofiahmad.mgmbackend.model.intranet.AppealStatus;
import com.roofiahmad.mgmbackend.model.intranet.IntranetAppealWorkflow;
import com.roofiahmad.mgmbackend.repository.internet.AppealRepository;
import com.roofiahmad.mgmbackend.repository.intranet.IntranetAppealWorkflowRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class AppealController {

    private final AppealRepository appealRepository;
    private final IntranetAppealWorkflowRepository workflowRepository;

    public AppealController(AppealRepository appealRepository, IntranetAppealWorkflowRepository workflowRepository) {
        this.appealRepository = appealRepository;
        this.workflowRepository = workflowRepository;
    }

    // 1. POST /appeals -> simpan ke internet_db
    @PostMapping("/appeals")
    public ResponseEntity<Appeal> createAppeal(@RequestBody Appeal appeal) {
        Appeal savedAppeal = appealRepository.save(appeal);
        return ResponseEntity.ok(savedAppeal);
    }

    // 2. GET /intranet/appeals -> ambil dari intranet_db
    @GetMapping("/intranet/appeals")
    public ResponseEntity<List<IntranetAppealWorkflow>> getAllIntranetAppeals() {
        return ResponseEntity.ok(workflowRepository.findAll());
    }

    // 3. POST /intranet/appeals/{id}/respond -> update intranet_db
    @PostMapping("/intranet/appeals/{id}/respond")
    public ResponseEntity<?> respondToAppeal(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String responseText = body.get("response");

        return workflowRepository.findById(id)
                .map(workflow -> {
                    workflow.setOfficerResponse(responseText);
                    workflow.setStatus(AppealStatus.AWAITING_CUSTOMER);
                    workflow.setRespondedAt(LocalDateTime.now());
                    workflowRepository.save(workflow);
                    return ResponseEntity.ok(workflow);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
