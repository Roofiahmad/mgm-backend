package com.roofiahmad.mgmbackend.config;

import com.roofiahmad.mgmbackend.model.internet.Appeal;
import com.roofiahmad.mgmbackend.model.intranet.AppealStatus;
import com.roofiahmad.mgmbackend.model.intranet.IntranetAppealWorkflow;
import com.roofiahmad.mgmbackend.repository.internet.AppealRepository;
import com.roofiahmad.mgmbackend.repository.intranet.IntranetAppealWorkflowRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final AppealRepository appealRepository;
    private final IntranetAppealWorkflowRepository workflowRepository;

    @Override
    @Transactional(transactionManager = "intranetTransactionManager")
    public void run(String... args) throws Exception {

        Appeal appeal1 = Appeal.builder()
                .customerName("Budi Santoso")
                .subject("Koneksi Internet Lambat")
                .message("Sudah 2 hari bandwidth tidak sesuai paket langganan.")
                .submittedAt(LocalDateTime.now())
                .build();

        appealRepository.save(appeal1);

        Appeal appeal2 =  Appeal.builder()
                .customerName("Siti Aminah")
                .subject("Gagal Bayar Tagihan")
                .message("Saldo terpotong di m-banking tapi status di app masih pascabayar.")
                .submittedAt(LocalDateTime.now().minusHours(2))
                .build();

        appealRepository.save(appeal2);

        Appeal appeal3 = Appeal.builder()
                .customerName("Agus Wijaya")
                .subject("Request Pindah Tiang")
                .message("Tiang internet menghalangi proyek pagar rumah.")
                .submittedAt(LocalDateTime.now().minusDays(1))
                .build();

        Appeal savedAppeal3 = appealRepository.save(appeal3);

        IntranetAppealWorkflow wf3 = IntranetAppealWorkflow.builder()
                .id(savedAppeal3.getId())
                .customerName(savedAppeal3.getCustomerName())
                .subject(savedAppeal3.getSubject())
                .status(AppealStatus.OPEN)
                .build();

        workflowRepository.save(wf3);

        Appeal appeal4 = Appeal.builder()
                .customerName("Dewi Lestari")
                .subject("Wifi Router Rusak")
                .message("Lampu indikator loss merah terus menerus.")
                .submittedAt(LocalDateTime.now().minusDays(2))
                .build();

        Appeal savedAppeal4 = appealRepository.save(appeal4);

        IntranetAppealWorkflow wf4 = IntranetAppealWorkflow.builder()
                .id(savedAppeal4.getId())
                .status(AppealStatus.AWAITING_CUSTOMER)
                .customerName(savedAppeal4.getCustomerName())
                .subject(savedAppeal4.getSubject())
                .officerResponse("Tim teknisi dijadwalkan ke rumah Anda besok jam 10 pagi. Mohon konfirmasi kesediaan Anda.")
                .respondedAt(LocalDateTime.now().minusMinutes(5))
                .build();

        workflowRepository.save(wf4);

        Appeal appeal5 = Appeal.builder()
                .customerName("Eko Prasetyo")
                .subject("Pengajuan Berlangganan Baru")
                .message("Apakah area perumahan Griya Asri sudah tercover jaringan?")
                .submittedAt(LocalDateTime.now().minusDays(10))
                .build();

        Appeal savedAppeal5 = appealRepository.save(appeal5);

        IntranetAppealWorkflow wf5 = IntranetAppealWorkflow.builder()
                .id(savedAppeal5.getId())
                .status(AppealStatus.AWAITING_CUSTOMER)
                .customerName(savedAppeal5.getCustomerName())
                .subject(savedAppeal5.getSubject())
                .officerResponse("Area tersebut sudah tercover. Harap lampirkan foto KTP Anda untuk registrasi.")
                .respondedAt(LocalDateTime.now().minusDays(8))
                .build();

        workflowRepository.save(wf5);

        log.info("== Seeding Selesai! 5 Data Awal Siap Digunakan ==");
    }
}
