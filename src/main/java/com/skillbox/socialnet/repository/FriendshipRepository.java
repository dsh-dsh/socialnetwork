package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Friendship;
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

    @Query("FROM Friendship WHERE (srcPerson = :person or dstPerson = :person) and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    List<Friendship> findAllFriends(Person person);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE (friendship.srcPerson = :person or friendship.dstPerson = :person) " +
            "AND friendship.status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    Page<Friendship> findAllFriendsPageable(Person person, Pageable pageable);

    @Query("FROM Friendship WHERE dstPerson = :person and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.REQUEST")
    List<Friendship> findAllRequest(Person person);

    @Query("SELECT friendship " +
            "FROM Friendship AS friendship " +
            "WHERE friendship.dstPerson = :person " +
            "AND friendship.status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.REQUEST")
    Page<Friendship> findAllRequestPageable(Person person, Pageable pageable);

    @Query("FROM Friendship WHERE dstPerson = :currentPerson and srcPerson = :dstPerson and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.REQUEST")
    List<Friendship> findRequests(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson) or (dstPerson = :dstPerson and srcPerson = :currentPerson))")
    Optional<Friendship> getRelationship(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson)" +
             "or (dstPerson = :dstPerson and srcPerson = :currentPerson)) and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    List<Friendship> isFriends(Person currentPerson, Person dstPerson);

    @Query("FROM Friendship WHERE (srcPerson IN (:persons) OR dstPerson IN (:persons)) and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    List<Friendship> findAllFriendsOfMyFriends(Collection<Person> persons);
}
