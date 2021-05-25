import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class NBARecord {

    @JsonFormat
    private String playerName;

    @JsonFormat
    private String teamName;

    @JsonFormat(
            shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd"
    )
    private Date gameDate;

    @JsonFormat
    private String season;

    @JsonFormat
    private int playerId;

    @JsonFormat
    private int teamId;

    @JsonFormat
    private int espnGameId;

    @JsonFormat
    private int gamePeriod;

    @JsonFormat
    private int minRemaining;

    @JsonFormat
    private int secRemaining;

    @JsonFormat
    private int shotFlag;

    @JsonFormat
    private String actionType;

    @JsonFormat
    private String shotType;

    @JsonFormat
    private int shotDistance;

    @JsonFormat
    private String opponent;

    @JsonFormat
    private int courtX;

    @JsonFormat
    private int courtY;

    public NBARecord(){

    }

    public NBARecord(String playerName, String teamName, Date gameDate, String season, int playerId, int teamId, int espnGameId, int gamePeriod, int minRemaining, int secRemaining, int shotFlag, String actionType, String shotType, int shotDistance, String opponent, int courtX, int courtY) {
        this.playerName = playerName;
        this.teamName = teamName;
        this.gameDate = gameDate;
        this.season = season;
        this.playerId = playerId;
        this.teamId = teamId;
        this.espnGameId = espnGameId;
        this.gamePeriod = gamePeriod;
        this.minRemaining = minRemaining;
        this.secRemaining = secRemaining;
        this.shotFlag = shotFlag;
        this.actionType = actionType;
        this.shotType = shotType;
        this.shotDistance = shotDistance;
        this.opponent = opponent;
        this.courtX = courtX;
        this.courtY = courtY;
    }
}
