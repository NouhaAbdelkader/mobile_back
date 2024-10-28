package tn.talan.tripaura_backend.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import tn.talan.tripaura_backend.entities.Test;

import tn.talan.tripaura_backend.services.ServicesTest;

import java.util.List;
@RestController
@AllArgsConstructor
@Tag(name = "ForumTest")
@RequestMapping("/questions")
public class TestController {
    ServicesTest serviceTest ;
    //@Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public List<Test> getAllAnswers() {
        return serviceTest.getAllModule();

    }
}
