package com.roofiahmad.mgmbackend.config;

import com.roofiahmad.mgmbackend.model.internet.Appeal;
import com.roofiahmad.mgmbackend.model.intranet.AppealStatus;
import com.roofiahmad.mgmbackend.model.intranet.IntranetAppealWorkflow;
import com.roofiahmad.mgmbackend.repository.internet.AppealRepository;
import com.roofiahmad.mgmbackend.repository.intranet.IntranetAppealWorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Configuration
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final AppealRepository appealRepository;
    private final IntranetAppealWorkflowRepository workflowRepository;

    public DataSeeder(AppealRepository appealRepository, IntranetAppealWorkflowRepository workflowRepository) {
        this.appealRepository = appealRepository;
        this.workflowRepository = workflowRepository;
    }

    @Override
    @Transactional(transactionManager = "intranetTransactionManager")
    public void run(String... args) throws Exception {
        log.info("== Démarrage du Seeding des Données Awal ==");

        // --- DATA 1: Data Baru di Internet DB (Belum Masuk Intranet) ---
        // Skenario: Menunggu ditarik otomatis oleh Sync Job ke Intranet DB menjadi OPEN
        Appeal appeal1 = new Appeal();
        appeal1.setCustomerName("Budi Santoso");
        appeal1.setSubject("Koneksi Internet Lambat");
        appeal1.setMessage("Sudah 2 hari bandwidth tidak sesuai paket langganan.");
        appeal1.setSubmittedAt(LocalDateTime.now());
        appealRepository.save(appeal1);

        // --- DATA 2: Data Baru Lainnya di Internet DB (Belum Masuk Intranet) ---
        Appeal appeal2 = new Appeal();
        appeal2.setCustomerName("Siti Aminah");
        appeal2.setSubject("Gagal Bayar Tagihan");
        appeal2.setMessage("Saldo terpotong di m-banking tapi status di app masih pascabayar.");
        appeal2.setSubmittedAt(LocalDateTime.now().minusHours(2));
        appealRepository.save(appeal2);

        // --- DATA 3: Data yang Sudah Tersinkronisasi & Berstatus OPEN di Intranet ---
        Appeal appeal3 = new Appeal();
        appeal3.setCustomerName("Agus Wijaya");
        appeal3.setSubject("Request Pindah Tiang");
        appeal3.setMessage("Tiang internet menghalangi proyek pagar rumah.");
        appeal3.setSubmittedAt(LocalDateTime.now().minusDays(1));
        Appeal savedAppeal3 = appealRepository.save(appeal3);

        IntranetAppealWorkflow wf3 = new IntranetAppealWorkflow();
        wf3.setId(savedAppeal3.getId());
        wf3.setCustomerName(savedAppeal3.getCustomerName());
        wf3.setSubject(savedAppeal3.getSubject());
        wf3.setStatus(AppealStatus.OPEN);
        workflowRepository.save(wf3);

        // --- DATA 4: Data yang Sudah Dijawab Staf (AWAITING_CUSTOMER) - Masih Baru ---
        // Skenario: Aman dari Auto-Close karena baru dijawab 5 menit yang lalu
        Appeal appeal4 = new Appeal();
        appeal4.setCustomerName("Dewi Lestari");
        appeal4.setSubject("Wifi Router Rusak");
        appeal4.setMessage("Lampu indikator loss merah terus menerus.");
        appeal4.setSubmittedAt(LocalDateTime.now().minusDays(2));
        Appeal savedAppeal4 = appealRepository.save(appeal4);

        IntranetAppealWorkflow wf4 = new IntranetAppealWorkflow();
        wf4.setId(savedAppeal4.getId());
        wf4.setStatus(AppealStatus.AWAITING_CUSTOMER);
        wf4.setCustomerName(savedAppeal4.getCustomerName());
        wf4.setSubject(savedAppeal4.getSubject());
        wf4.setOfficerResponse("Tim teknisi dijadwalkan ke rumah Anda besok jam 10 pagi. Mohon konfirmasi kesediaan Anda.");
        wf4.setRespondedAt(LocalDateTime.now().minusMinutes(5)); // Baru 5 menit lalu
        workflowRepository.save(wf4);

        // --- DATA 5: TARGET AUTO-CLOSE (Lebih dari 7 Hari / 1 Menit Demo) ---
        // Skenario: Berstatus AWAITING_CUSTOMER dan respondedAt sudah lama sekali.
        // Saat Scheduler Auto-Close berjalan di menit pertama, data ini akan LANGSUNG ter-CLOSE otomatis.
        Appeal appeal5 = new Appeal();
        appeal5.setCustomerName("Eko Prasetyo");
        appeal5.setSubject("Pengajuan Berlangganan Baru");
        appeal5.setMessage("Apakah area perumahan Griya Asri sudah tercover jaringan?");
        appeal5.setSubmittedAt(LocalDateTime.now().minusDays(10));
        Appeal savedAppeal5 = appealRepository.save(appeal5);

        IntranetAppealWorkflow wf5 = new IntranetAppealWorkflow();
        wf5.setId(savedAppeal5.getId());
        wf5.setStatus(AppealStatus.AWAITING_CUSTOMER);
        wf5.setCustomerName(savedAppeal5.getCustomerName());
        wf5.setSubject(savedAppeal5.getSubject());
        wf5.setOfficerResponse("Area tersebut sudah tercover. Harap lampirkan foto KTP Anda untuk registrasi.");

        // Sengaja dibuat berumur 8 hari yang lalu agar lolos validasi > 7 hari maupun > 1 menit untuk demo
        wf5.setRespondedAt(LocalDateTime.now().minusDays(8));
        workflowRepository.save(wf5);

        log.info("== Seeding Selesai! 5 Data Awal Siap Digunakan ==");
    }
}
