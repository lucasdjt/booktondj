package fr.but3.controller.admin;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class AdminDBController {

    private final JdbcTemplate jdbc;

    public AdminDBController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/admin/db")
    public String db(Model model) {

        model.addAttribute("users",        asObjectArrayRows(jdbc.queryForList("SELECT * FROM users")));
        model.addAttribute("slots",        asObjectArrayRows(jdbc.queryForList("SELECT * FROM slots")));
        model.addAttribute("reservations", asObjectArrayRows(jdbc.queryForList("SELECT * FROM reservations")));

        return "admin/db";
    }

    private static List<Object[]> asObjectArrayRows(List<Map<String, Object>> rows) {
        List<Object[]> result = new ArrayList<>();
        for (Map<String, Object> row : rows) {
            result.add(row.values().toArray());
        }
        return result;
    }
}