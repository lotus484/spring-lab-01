package kz.iitu.springlab.web;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;
import java.util.Map;

@Controller
public class AdminController {

    private final JdbcTemplate jdbc;

    public AdminController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin(Model model) {

        List<Map<String, Object>> users = jdbc.queryForList(
                "SELECT username, enabled FROM users ORDER BY username"
        );

        model.addAttribute("users", users);

        return "admin";
    }

    @GetMapping("/method/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String methodAdmin() {
        return "admin";
    }
}
