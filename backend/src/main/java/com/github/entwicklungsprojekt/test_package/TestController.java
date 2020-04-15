package com.github.entwicklungsprojekt.test_package;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/testendpoint")
public class TestController {

    @PostMapping(path = "post/{summand}")
    public ResponseEntity<?> testEndpoint(@RequestParam(required = true) String zahl, @PathVariable(name = "summand") String summand){

        int zahlParsed = Integer.parseInt(zahl);
        zahlParsed += Integer.parseInt(summand);
        TestClass testClass = new TestClass(zahlParsed);
        return ResponseEntity.ok(testClass);
    }

}
