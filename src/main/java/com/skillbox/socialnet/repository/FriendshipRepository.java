package com.skillbox.socialnet.repository;


import com.skillbox.socialnet.model.dto.NotificationInterfaceProjectile;
import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import org.springframework.data.domain.Page;
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

    @Query(value = "select dst_person_id as dst, src_person_id as src from friendship f " +
            "LEFT JOIN friendship_status fs ON fs.id = f.status_id " +
            "where (src_person_id = :id or dst_person_id = :id)  and fs.code like 'FRIEND'",
    nativeQuery = true)
    List<NotificationInterfaceProjectile> getIdsForNotification(int id);

    @Query("FROM Friendship " +
            "WHERE (srcPerson = :person or dstPerson = :person) " +
            "AND status.code = :code")
    List<Friendship> findAllFriends(Person person, FriendshipStatusCode code);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE (friendship.srcPerson = :person or friendship.dstPerson = :person) " +
            "AND (friendship.status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND " +
            "OR friendship.status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.BLOCKED) " +
            "ORDER BY friendship.dstPerson.lastName, friendship.srcPerson.lastName")
    Page<Friendship> findAllFriendsPageable(Person person, Pageable pageable);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE friendship.dstPerson = :person " +
            "AND friendship.status.code = :code")
    List<Friendship> findAllRequests(Person person, FriendshipStatusCode code);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE friendship.dstPerson = :person " +
            "AND friendship.status.code = :code " +
            "ORDER BY friendship.srcPerson.lastName, friendship.dstPerson.lastName")
    Page<Friendship> findAllRequestsPageable(Person person, FriendshipStatusCode code, Pageable pageable);

    @Query("FROM Friendship " +
            "WHERE (srcPerson = :srcPerson AND dstPerson = :dstPerson) " +
            "OR (srcPerson = :dstPerson AND dstPerson = :srcPerson) " +
            "AND status.code = :code")
    List<Friendship> findRequests(Person srcPerson, Person dstPerson, FriendshipStatusCode code);

    @Query("FROM Friendship " +
            "WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson) " +
            "OR (dstPerson = :dstPerson and srcPerson = :currentPerson))")
    Optional<Friendship> getRelationship(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship " +
            "WHERE ((srcPerson = :srcPerson and dstPerson = :dstPerson) " +
            "OR (srcPerson = :dstPerson and dstPerson = :srcPerson)) " +
            "AND status.code = :code")
    List<Friendship> findFriendshipsByStatusCode(Person srcPerson, Person dstPerson, FriendshipStatusCode code);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE ((friendship.srcPerson = :srcPerson and friendship.dstPerson = :dstPerson) " +
            "OR (friendship.srcPerson = :dstPerson and friendship.dstPerson = :srcPerson)) " +
            "AND (friendship.status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND " +
            "OR friendship.status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.BLOCKED)")
    List<Friendship> findFriendships(Person srcPerson, Person dstPerson);

    @Query("FROM Friendship " +
            "WHERE (srcPerson IN (:persons) OR dstPerson IN (:persons)) " +
            "AND status.code = :code")
    List<Friendship> findAllFriendsOfMyFriends(Collection<Person> persons, FriendshipStatusCode code);

    Optional<Friendship> findBySrcPersonAndDstPerson(Person srcPerson, Person dstPerson);

    @Query("FROM Friendship " +
            "WHERE ((srcPerson = :srcPerson and dstPerson = :dstPerson) " +
            "OR (srcPerson = :dstPerson and dstPerson = :srcPerson)) " +
            "AND status.code = :code")
    Optional<Friendship> findFriendship(Person srcPerson, Person dstPerson, FriendshipStatusCode code);

    @Query("FROM Friendship " +
            "WHERE srcPerson = :srcPerson AND dstPerson = :dstPerson " +
            "AND status.code = :code")
    Optional<Friendship> findFriendshipByStatusCode(Person srcPerson, Person dstPerson, FriendshipStatusCode code);
}
