package org.astashonok.rest;

import org.astashonok.model.Developer;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperRestControllerV1 {

    private List<Developer> developers = Stream.of(
            new Developer(1, "Ivan", "Ivanov"),
            new Developer(2, "Petr", "Petrov"),
            new Developer(3, "Sergey", "Sergeev")
    ).collect(Collectors.toList());

    @GetMapping
    @ResponseBody
    public List<Developer> getAll(){
        return developers;
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Developer getById(@PathVariable long id){
        return developers
                .stream()
                .filter(developer -> developer.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
