package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.FriendshipStatus;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Semen V
 * @created 28|11|2021
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

    @Query("FROM Friendship " +
            "WHERE (srcPerson = :person or dstPerson = :person) " +
            "AND status.code = :code")
    List<Friendship> findAllFriends(Person person, FriendshipStatusCode code);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE (friendship.srcPerson = :person or friendship.dstPerson = :person) " +
            "AND friendship.status.code = :code")
    Page<Friendship> findAllFriendsPageable(Person person, FriendshipStatusCode code, Pageable pageable);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE friendship.dstPerson = :person " +
            "AND friendship.status.code = :code")
    Page<Friendship> findAllRequestPageable(Person person, FriendshipStatusCode code, Pageable pageable);

    @Query("FROM Friendship " +
            "WHERE (srcPerson = :srcPerson AND dstPerson = :dstPerson) " +
            "OR ( srcPerson = :dstPerson AND dstPerson = :srcPerson) " +
            "AND status.code = :code")
    List<Friendship> findRequests(Person srcPerson, Person dstPerson, FriendshipStatusCode code);

    @Query("FROM Friendship " +
            "WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson) " +
            "OR (dstPerson = :dstPerson and srcPerson = :currentPerson))")
    Optional<Friendship> getRelationship(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship " +
            "WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson) " +
            "OR (dstPerson = :dstPerson and srcPerson = :currentPerson)) " +
            "AND status.code = :code")
    List<Friendship> isFriends(Person currentPerson, Person dstPerson, FriendshipStatusCode code);

    @Query("FROM Friendship " +
            "WHERE (srcPerson IN (:persons) OR dstPerson IN (:persons)) " +
            "AND status.code = :code")
    List<Friendship> findAllFriendsOfMyFriends(Collection<Person> persons, FriendshipStatusCode code);
}
