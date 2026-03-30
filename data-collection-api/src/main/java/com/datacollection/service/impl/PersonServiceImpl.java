package com.datacollection.service.impl;

import com.datacollection.entity.Person;
import com.datacollection.repository.PersonRepository;
import com.datacollection.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    
    private final PersonRepository personRepository;
    
    @Override
    @Transactional
    public Person addPerson(Person person) {
        person.setStatus(1);
        return personRepository.save(person);
    }
    
    @Override
    @Transactional
    public Person updatePerson(Long id, Person person) {
        Person existing = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("人物不存在"));
        person.setId(id);
        return personRepository.save(person);
    }
    
    @Override
    @Transactional
    public void deletePerson(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("人物不存在"));
        person.setStatus(0);
        personRepository.save(person);
    }
    
    @Override
    public Person getPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("人物不存在"));
    }
    
    @Override
    public List<Person> searchPersons(String keyword, String country, String role) {
        return personRepository.searchPersons(keyword, country, role);
    }
    
    @Override
    public Long countActive() {
        return personRepository.countActive();
    }
}
