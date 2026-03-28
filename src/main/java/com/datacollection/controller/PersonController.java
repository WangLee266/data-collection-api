package com.datacollection.controller;

import com.datacollection.entity.Person;
import com.datacollection.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 人物信源控制器
 */
@Tag(name = "人物管理", description = "人物信源的增删改查接口")
@RestController
@RequestMapping("/sources/persons")
@RequiredArgsConstructor
public class PersonController {
    
    private final PersonService personService;
    
    @Operation(summary = "添加人物")
    @PostMapping
    public ApiResponse<Person> addPerson(@RequestBody Person person) {
        return ApiResponse.success("人物添加成功", personService.addPerson(person));
    }
    
    @Operation(summary = "更新人物")
    @PutMapping("/{id}")
    public ApiResponse<Person> updatePerson(@PathVariable Long id, @RequestBody Person person) {
        return ApiResponse.success("人物更新成功", personService.updatePerson(id, person));
    }
    
    @Operation(summary = "删除人物")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ApiResponse.success("人物删除成功");
    }
    
    @Operation(summary = "获取人物详情")
    @GetMapping("/{id}")
    public ApiResponse<Person> getPerson(@PathVariable Long id) {
        return ApiResponse.success(personService.getPersonById(id));
    }
    
    @Operation(summary = "搜索人物")
    @GetMapping("/search")
    public ApiResponse<List<Person>> searchPersons(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String role) {
        return ApiResponse.success(personService.searchPersons(keyword, country, role));
    }
    
    @Operation(summary = "获取人物数量")
    @GetMapping("/count")
    public ApiResponse<Long> countPersons() {
        return ApiResponse.success(personService.countActive());
    }
}
