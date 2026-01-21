package es.cfcreation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StatsIngestor {

    public void procesarArchivo(String nombreArchivo, String temporadaEtiqueta) {
        Gson gson = new Gson();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(nombreArchivo)) {

            if (is == null) {
                System.err.println("Error: No se encontró el archivo en resources: " + nombreArchivo);
                return;
            }

            try (Reader reader = new InputStreamReader(is)) {
                Type listType = new TypeToken<List<SeasonStatDTO>>() {
                }.getType();
                List<SeasonStatDTO> stats = gson.fromJson(reader, listType);

                System.out.println("Iniciando ingesta de " + stats.size() + " registros...");
                DatabaseManager.setupDatabase();

                for (SeasonStatDTO s : stats) {
                    DatabaseManager.insertarRegistro(
                            s.player_id, s.name, s.team_name, s.comp_name,
                            s.goals, s.assists, s.apps, s.rating, temporadaEtiqueta
                    );
                }
                System.out.println("¡Ingesta completada!");
            }
        } catch (IOException e) {
            System.err.println("Error al procesar el JSON: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        StatsIngestor ingestor = new StatsIngestor();

        // Aquí pones la ruta del archivo que genera el Live Editor
        // Puedes automatizar esto para que busque el archivo con la fecha de hoy
        String ruta = "SEASON_STATS_12_05_2030.json";

        // Lanzamos la ingesta indicando la temporada manualmente
        ingestor.procesarArchivo(ruta, "2029/30");
    }
}
