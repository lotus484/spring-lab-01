package kz.iitu.springlab.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.Map;

@RestController
public class CsrfController {

    @GetMapping(value = "/csrf-form", produces = MediaType.TEXT_HTML_VALUE)
    public String csrfForm(HttpServletRequest request) {

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");

        return """
                <!DOCTYPE html>
                <html>
                <body>
                    <h2>CSRF Protection Test</h2>
                    <form method="post" action="/csrf-test">
                        <input type="hidden" name="%s" value="%s">
                        <button type="submit">Submit</button>
                    </form>
                </body>
                </html>
                """.formatted(token.getParameterName(), token.getToken());
    }

    @PostMapping("/csrf-test")
    public Map<String, String> csrfTest() {
        return Map.of(
                "message", "CSRF validation successful!",
                "status", "POST request accepted"
        );
    }
}
