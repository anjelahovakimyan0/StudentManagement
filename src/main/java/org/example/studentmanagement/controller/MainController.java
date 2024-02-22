package org.example.studentmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.example.studentmanagement.util.FileComponent;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final FileComponent fileComponent;

    @GetMapping("/")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return fileComponent.getImage(picName);
    }
}
