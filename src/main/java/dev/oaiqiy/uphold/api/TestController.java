package dev.oaiqiy.uphold.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public ResultInfo<String> test(){
        System.out.println("test");
        return new ResultInfo<>(0,"success","mhl");
    }
}
