package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findByeMail(String email);
    Person findPersonById(int id);
    Optional<Person> findByConfirmationCode(String confirmationCode);

    @Query("SELECT person " +
            "FROM Person AS person " +
            "WHERE (:firstName is null OR lower(person.firstName) like %:firstName%) " +
            "AND (:lastName is null OR lower(person.lastName) like %:lastName%) " +
            "AND person.birthDate BETWEEN :from AND :to " +
            "AND (:country is null OR person.country = :country) " +
            "AND (:city is null OR person.city = :city) " +
            "AND person.isBlocked = false " +
            "AND person.isApproved = true " +
            "ORDER BY person.firstName, person.lastName")
    Page<Person> findBySearchRequest(String firstName, String lastName, String country, String city, Date from, Date to, Pageable pageable);

    Set<Person> findByIdIn(List<Integer> idList);

    Optional<Person> getPersonById(int id);

    Page<Person> findByFirstNameContainingOrLastNameContainingIgnoreCase(String fistName, String lastName, Pageable pageable);

    @Query("SELECT person " +
            "FROM Person AS person " +
            "WHERE person NOT IN (:myFriends) " +
            "ORDER BY person.regDate DESC")
    List<Person> findNewFriendsLimit(Collection<Person> myFriends, Pageable pageable);

    boolean existsByeMail(String email);

    @Query("select person from Person as person where person.isDeleted = :isDeleted and person.lastOnlineTime < :timestamp")
    List<Person> findAllByDeleted(boolean isDeleted, Timestamp timestamp);

    @Query(value = "select id from person order by id",
            nativeQuery = true)
    List<Integer> getAllIds();

    @Query(value = "select id from person where id =:id and (EXTRACT(DAY FROM person.birth_date) - EXTRACT(DAY FROM now()) IN (0,1))",
            nativeQuery = true)
    Integer getIdIfBirthDayIsTomorrowOrToday(int id);

    @Query(value = "select e_mail from person where id =:id",
            nativeQuery = true)
    String getEmailById(int id);

}

