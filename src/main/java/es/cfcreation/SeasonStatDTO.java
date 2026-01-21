package es.cfcreation;

public class SeasonStatDTO {
    public int player_id;
    public String name;
    public String team_name;
    public int team_id;
    public String comp_name;
    public int goals;
    public int assists;
    public int apps;
    public double rating;
    // GSON usará reflexión para llenar estos campos
}
