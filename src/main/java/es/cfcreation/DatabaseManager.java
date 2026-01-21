package es.cfcreation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:data/season_player_stats.db";

    public static void insertarLote(List<SeasonStatDTO> stats, String temporada) {
        String sql = "INSERT INTO stats_carrera(player_id, nombre, equipo, competicion, goles, asistencias, partidos, valoracion, temporada) VALUES(?,?,?,?,?,?,?,?,?)";

        // Abrimos la conexión UNA SOLA VEZ para todos
        try (Connection conn = DriverManager.getConnection(URL)) {
            conn.setAutoCommit(false); // <--- INICIA TRANSACCIÓN (MAGIA DE SQL)

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (SeasonStatDTO s : stats) {
                    pstmt.setInt(1, s.player_id);
                    pstmt.setString(2, s.name);
                    pstmt.setString(3, s.team_name);
                    pstmt.setString(4, s.comp_name);
                    pstmt.setInt(5, s.goals);
                    pstmt.setInt(6, s.assists);
                    pstmt.setInt(7, s.apps);
                    pstmt.setDouble(8, s.rating);
                    pstmt.setString(9, temporada);
                    pstmt.addBatch(); // Lo añade a la cola
                }
                pstmt.executeBatch(); // Envía todos de golpe
                conn.commit(); // Guarda cambios
                System.out.println("Base de datos actualizada correctamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error en la transacción: " + e.getMessage());
        }
    }
}