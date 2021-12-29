package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {

    @Query("SELECT dialog FROM Dialog AS dialog " +
            "WHERE :person IN elements(dialog.persons)")
    Page<Dialog> findByPerson(Person person, Pageable pageable);

    @Query("SELECT dialog FROM Dialog AS dialog " +
            "WHERE :person IN elements(dialog.persons)")
    List<Dialog> findByPerson(Person person);

}