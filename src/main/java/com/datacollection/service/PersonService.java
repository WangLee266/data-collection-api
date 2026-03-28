package com.datacollection.service;

import com.datacollection.entity.Person;
import java.util.List;

/**
 * 人物服务接口
 */
public interface PersonService {
    
    /**
     * 添加人物
     */
    Person addPerson(Person person);
    
    /**
     * 更新人物
     */
    Person updatePerson(Long id, Person person);
    
    /**
     * 删除人物
     */
    void deletePerson(Long id);
    
    /**
     * 获取人物详情
     */
    Person getPersonById(Long id);
    
    /**
     * 搜索人物
     */
    List<Person> searchPersons(String keyword, String country, String role);
    
    /**
     * 获取活跃人物数量
     */
    Long countActive();
}
