package com.roofiahmad.mgmbackend.repository.internet;

import com.roofiahmad.mgmbackend.model.internet.Appeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppealRepository extends JpaRepository<Appeal, Long> {
}
