package com.skillbox.socialnet.repository;

import com.skillbox.socialnet.model.entity.Friendship;
import com.skillbox.socialnet.model.entity.Person;
import com.skillbox.socialnet.model.enums.FriendshipStatusCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Semen V
 * @created 28|11|2021
 */
@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {

//    @Query("FROM Friendship WHERE (srcPerson = :person or dstPerson = :person) and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    @Query("FROM Friendship WHERE (srcPerson = :person or dstPerson = :person)")
    public List<Friendship> findAllFriends(Person person);

    @Query("FROM Friendship WHERE dstPerson = :person and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.REQUEST")
    public List<Friendship> findAllRequest(Person person);

    @Query("FROM Friendship WHERE dstPerson = :currentPerson and srcPerson = :dstPerson and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.REQUEST")
    public List<Friendship> findRequestFromDstPersonToSrcPerson(Person currentPerson, Person dstPerson);

//    @Query("FROM Friendship WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson)" +
//            "or (dstPerson = :dstPerson and srcPerson = :currentPerson)) and status.code = com.skillbox.socialnet.model.enums.FriendshipStatusCode.FRIEND")
    @Query("FROM Friendship WHERE ((dstPerson = :currentPerson and srcPerson = :dstPerson) or (dstPerson = :dstPerson and srcPerson = :currentPerson))")
    public List<Friendship> isFriends(Person currentPerson, Person dstPerson);

    @Override
    void delete(Friendship friendship);

    @Override
    <S extends Friendship> S save(S s);
}
