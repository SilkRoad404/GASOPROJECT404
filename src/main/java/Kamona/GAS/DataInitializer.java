package Kamona.GAS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbc;

    public DataInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        Integer existe = jdbc.queryForObject(
            "SELECT COUNT(*) FROM usuarios WHERE username = 'admin'", Integer.class);
        if (existe != null && existe == 0) {
            jdbc.update(
                "INSERT INTO usuarios (es_editable, password, rol, username) VALUES (?, ?, ?, ?)",
                true, "pmWkWSBCL51Bfkhn79xPuKBKHz//H6B+mZ6G7+MqNrw=", "ADMIN", "admin");
        }
    }
}
