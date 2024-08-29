package com.shop.bike.consumer.repository;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.entity.User;
import com.shop.bike.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Qualifier(ApplicationConstant.CONSUMER)
public interface UserConsumerRepository extends UserRepository {

    Optional<User> findByEmail(String email);

}