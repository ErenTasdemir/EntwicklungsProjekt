package com.github.entwicklungsprojekt.test_package;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/testendpoint")
public class TestController {

    @GetMapping
    public ResponseEntity<?> testEndpoint(){

        TestClass testClass = new TestClass();
    }

}
