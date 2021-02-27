package com.recruitment.manager.repo;

import com.recruitment.manager.entity.Address;
import com.recruitment.manager.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Created by Chaklader on Feb, 2021
 */
@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

}
