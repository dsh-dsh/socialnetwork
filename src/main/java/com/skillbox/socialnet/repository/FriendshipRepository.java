package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Semen V
 * @created 28|11|2021
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query("FROM Friendship WHERE (srcPerson = :person or dstPerson = :person) and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    List<Friendship> findAllFriends(Person person);

    @Query("FROM Friendship WHERE dstPerson = :person and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.REQUEST")
    List<Friendship> findAllRequest(Person person);

    @Query("FROM Friendship WHERE dstPerson = :currentPerson and srcPerson = :dstPerson and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.REQUEST")
    List<Friendship> requests(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson) or (dstPerson = :dstPerson and srcPerson = :currentPerson))")
    Optional<Friendship> getRelationship(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson)" +
             "or (dstPerson = :dstPerson and srcPerson = :currentPerson)) and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    List<Friendship> isFriends(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship WHERE srcPerson IN :persons OR dstPerson IN :persons")
    List<Friendship> findAllFriendsOfMyFriends(List<Person> persons);

//    @Override
//    void delete(Friendship friendship);
//
//    @Override
//    <S extends Friendship> S save(S s);
}
