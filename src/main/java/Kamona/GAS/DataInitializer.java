package Kamona.GAS;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final JdbcTemplate jdbc;

    // Hash BCrypt de "123"
    private static final String HASH = "$2a$10$iZg/uTMqe1m1oFRb0LQxTuBpFPjQHohMozSfhNkjTUN51wZZJPyu6";

    public DataInitializer(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        try {
            List<Map<String, Object>> cols = jdbc.queryForList(
                "SELECT table_name, column_name FROM information_schema.columns WHERE table_schema = 'public'");

            System.out.println(">>> INIT: columnas encontradas = " + cols.size());

            String[] userNames = {"username", "usuario", "nombre_usuario", "user_name", "nombre", "email"};
            String[] passNames = {"password", "contrasena", "clave", "pass"};

            for (Map<String, Object> c : cols) {
                String table = (String) c.get("table_name");
                String col = (String) c.get("column_name");
                boolean isPass = false;
                for (String p : passNames) if (p.equalsIgnoreCase(col)) isPass = true;
                if (!isPass) continue;

                for (String u : userNames) {
                    boolean has = cols.stream().anyMatch(x ->
                        table.equals(x.get("table_name")) && u.equalsIgnoreCase((String) x.get("column_name")));
                    if (!has) continue;

                    int n = jdbc.update(
                        "UPDATE \"" + table + "\" SET \"" + col + "\" = ? WHERE UPPER(\"" + u + "\") IN ('AD','ADMIN')",
                        HASH);
                    System.out.println(">>> INIT: tabla=" + table + " usuarioCol=" + u + " passCol=" + col + " filasActualizadas=" + n);

                    List<Map<String, Object>> rows = jdbc.queryForList("SELECT * FROM \"" + table + "\"");
                    for (Map<String, Object> r : rows) {
                        System.out.println(">>> INIT: fila = " + r.entrySet().stream()
                            .filter(e -> !e.getKey().equalsIgnoreCase(col))
                            .toList());
                    }
                    return;
                }
            }
            System.out.println(">>> INIT: no encontre tabla de usuarios");
        } catch (Exception e) {
            System.out.println(">>> INIT ERROR: " + e.getMessage());
        }
    }
}
