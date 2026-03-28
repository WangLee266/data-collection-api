package com.datacollection.repository;

import com.datacollection.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    @Query("SELECT p FROM Person p WHERE " +
           "(:keyword IS NULL OR p.nameCn LIKE %:keyword% OR p.nameEn LIKE %:keyword% OR p.country LIKE %:keyword%) AND " +
           "(:country IS NULL OR p.country = :country) AND " +
           "(:role IS NULL OR p.role = :role) AND " +
           "p.status = 1")
    List<Person> searchPersons(String keyword, String country, String role);
    
    @Query("SELECT COUNT(p) FROM Person p WHERE p.status = 1")
    Long countActive();
}
