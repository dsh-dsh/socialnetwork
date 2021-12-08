package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByeMail(String email);
    Person findPersonById(int id);
    Optional<Person> findByConfirmationCode(String confirmationCode);

    @Query("SELECT person " +
            "FROM Person AS person " +
            "WHERE ((:firstName is null OR person.firstName = :firstName) " +
            "OR (:lastName is null OR person.lastName = :lastName)) " +
            "AND person.birthDate BETWEEN :from AND :to " + //OR person.birthDate >= :from OR person.birthDate <= :to) " +
            "AND (:country is null OR person.country = :country) " +
            "AND (:city is null OR person.city = :city) " +
            "AND person.isBlocked = false " +
            "AND person.isApproved = true " +  // TODO если эта строка нужна?
            "ORDER BY person.firstName, person.lastName")
    Page<Person> findBySearchRequest(String firstName, String lastName, String country, String city, Date from, Date to, Pageable pageable);


}

