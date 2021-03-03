package com.recruitment.manager.repo;

import com.recruitment.manager.entity.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Chaklader on Feb, 2021
 */
public interface AddressRepository extends CrudRepository<Address, Long> {

}
