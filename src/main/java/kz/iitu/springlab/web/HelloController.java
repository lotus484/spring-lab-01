package kz.iitu.springlab.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/hello")
    public Greeting hello(@RequestParam(defaultValue = "world") String name) {
        return new Greeting("Hello, " + name + "!", owner, LocalDateTime.now());
    }

    @GetMapping("/info")
    public Info info() {
        return new Info(
                owner,
                System.getProperty("java.version"),
                Runtime.getRuntime().availableProcessors()
        );
    }

    @GetMapping("/wordcount")
    public WordCount wordCount(@RequestParam(defaultValue = "") String text) {
        if (text.isBlank()) {
            return new WordCount(0, 0, "");
        }

        String[] words = text.trim().split("\\s+");

        String longestWord = "";
        for (String word : words) {
            if (word.length() > longestWord.length()) {
                longestWord = word;
            }
        }

        return new WordCount(
                words.length,
                text.length(),
                longestWord
        );
    }

    public record Greeting(String message, String owner, LocalDateTime timestamp) {
    }

    public record Info(String owner, String javaVersion, int cpuCores) {
    }

    public record WordCount(int words, int characters, String longestWord) {
    }
}