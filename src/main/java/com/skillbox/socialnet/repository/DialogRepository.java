package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Dialog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {
//    Page<Dialog> findBy
}
