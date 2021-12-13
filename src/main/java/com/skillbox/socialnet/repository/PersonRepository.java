package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByeMail(String email);
    Person findPersonById(int id);
    Optional<Person> findByConfirmationCode(String confirmationCode);
}
