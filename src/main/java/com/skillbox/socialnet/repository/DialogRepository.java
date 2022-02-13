package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Dialog;
import com.skillbox.socialnet.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Long> {

    @Query("SELECT dialog FROM Dialog AS dialog " +
            "WHERE :person IN elements(dialog.persons)")
    List<Dialog> findByPerson(Person person);

}
