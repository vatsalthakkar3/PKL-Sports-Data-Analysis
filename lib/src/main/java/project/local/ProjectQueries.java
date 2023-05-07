package project.local;

/**
 * This Class Contains all the Queries which will be sent to DBConnector to
 * fetch results from employee database according to call made in index.jsp.
 * 
 * 
 * @author Vatsal Thakkar
 * 
 * @return SQL Query String
 */

public class ProjectQueries {

        /**
         * Query-1: The success rate of raiders who have made the most attempted raids across all seasons
         * 
         * @author Aditya Deval
         * 
         * @return SQL Query String
         */

        public String query1() {
                return """
                        SELECT player_name AS 'Player Name',
                        ROUND(SUM(Player_successful_raids)/SUM(player_total_number_of_raids) * 100, 2) AS 'Raider Success Rate(%)'
                 FROM player_match_stats
                 JOIN Player ON player.player_id = player_match_stats.player_id
                 WHERE player_match_stats.player_id in
                     (SELECT player_id
                      FROM
                        (SELECT player_id,
                                SUM(player_total_number_of_raids) AS total_raids
                         FROM player_match_stats
                         GROUP BY player_id
                         ORDER BY Total_raids DESC
                         LIMIT 10) AS T)
                 GROUP BY player_match_stats.player_id ;
                                        """;
        }

        /**
         * Query-2: Season-wise comparison between the maximum and average raid points, tackle points, and total points
         * 
         * @author Punith Kandula
         * 
         * @return String SQL Query 
         */
        public String query2() {
                return """
                SELECT season_name AS 'Season Name',
                        ROUND(AVG(total_raid_points), 0) AS 'Average Raid Points',
                        MAX(total_raid_points) AS 'Maximum Raid Points in a Match',
                        ROUND(AVG(total_tackle_points), 0) AS 'Average Tackle Points',
                        MAX(total_tackle_points) AS 'Maximum Tackle Points in a Match',
                        ROUND(AVG(score), 0) AS 'Average Total Points',
                        MAX(score) AS 'Maximum Total Points in a match'
                FROM team_match_stats AS TMS
                JOIN pkl.match AS M ON TMS.match_id = M.match_id
                JOIN season AS S ON M.season_id = S.season_id
                GROUP BY M.season_id;

                                """;
        }

        /**
         * Query-3: For each team, the comparison between their win percentage at home matches, their win percentage at neutral venues, and their win percentage at away matches.
         * 
         * @author Vatsal Thakkar
         * 
         * @return SQL Query String
         */
        public String query3() {
                return """
                        SELECT team_name AS 'Team Name',
                        ROUND(Numeber_of_home_matches_won/Total_home_matches * 100, 2) AS 'Win Percentage Home',
                        ROUND(number_of_neutral_matches_won/Total_neutral_matches * 100, 2) 'Win Percentage Neutral',
                        ROUND(Number_of_away_matches_won/Total_away_matches * 100, 2) 'Win Percentage Away'
                 FROM
                   (SELECT team_id,
                           COUNT(CASE
                                     WHEN team_id = venue_type
                                          AND team_id = match_winner_id THEN 1
                                 END) AS Numeber_of_home_matches_won,
                           COUNT(CASE
                                     WHEN team_id = venue_type THEN 1
                                 END) AS Total_home_matches,
                           COUNT(CASE
                                     WHEN venue_type='Neutral'
                                          AND team_id = match_winner_id THEN 1
                                 END) AS number_of_neutral_matches_won,
                           COUNT(CASE
                                     WHEN venue_type='Neutral' THEN 1
                                 END) Total_neutral_matches,
                           COUNT(CASE
                                     WHEN team_id != venue_type
                                          AND venue_type!='Neutral'
                                          AND team_id = match_winner_id THEN 1
                                 END) Number_of_away_matches_won,
                           COUNT(CASE
                                     WHEN team_id != venue_type
                                          AND venue_type!='Neutral' THEN 1
                                 END) Total_away_matches
                    FROM team_match_stats AS TMS3
                    JOIN
                      (SELECT M.match_id,
                              match_winner_id,
                              team_1,
                              team_2,
                              CASE
                                  WHEN team_1 = v.team_id THEN team_1
                                  WHEN team_2 = v.team_id THEN team_2
                                  ELSE 'Neutral'
                              END AS venue_type
                       FROM pkl.match AS M
                       JOIN
                         (SELECT TMS.team_id AS team_1,
                                 TMS2.team_id AS team_2,
                                 TMS.match_id
                          FROM team_match_stats AS TMS
                          JOIN
                            (SELECT *
                             FROM team_match_stats AS T1
                             ORDER BY team_id DESC) AS TMS2 ON TMS.match_id = TMS2.match_id
                          AND TMS.team_id <> TMS2.team_id
                          AND TMS.team_id < TMS2.team_id) AS T ON M.match_id = T.match_id
                       JOIN venue AS V ON M.venue_id = V.venue_id) AS T3 ON T3.match_id = TMS3.match_id
                    GROUP BY tms3.team_id) AS T4
                 JOIN team ON T4.team_id = team.team_id;
                                """;
        }

        /**
         * Query-4: The win rate of a team is being compared between the scenarios where they won the toss and where they did not win the toss
         * 
         * 
         * @author vatsal Thakkar
         * 
         * @return SQL Query String
         */
        public String query4() {
                return """
                        SELECT team_name AS 'Team Name',
                        Round(COUNT(CASE
                                        WHEN TMS.team_id = match_winner_id
                                             AND TMS.team_id = toss_winner_id THEN 1
                                    END) / COUNT(CASE
                                                     WHEN TMS.team_id = toss_winner_id THEN 1
                                                 END)*100, 2) AS 'Win Rate (Toss Won)(%)',
                        Round(COUNT(CASE
                                        WHEN TMS.team_id = match_winner_id
                                             AND TMS.team_id != toss_winner_id THEN 1
                                    END) /COUNT(CASE
                                                    WHEN TMS.team_id != toss_winner_id THEN 1
                                                END) *100, 2) AS 'Win Rate (Toss Not Won)(%)'
                 FROM team_match_stats AS TMS
                 JOIN pkl.match AS M ON TMS.match_id = M.match_id
                 JOIN team AS T ON TMS.team_id = T.team_id
                 GROUP BY TMS.team_id;
                                """;
        }

        /**
         * Query-5 : The success rate of defenders who have attempted the highest number of tackles across all seasons.
         * 
         * 
         * @author Aditya Deval
         * 
         * @return SQL Query String
         */
        public String query5() {
                return """
                        SELECT player_name AS 'Player Name',
                        ROUND(SUM(player_successful_tackles)/SUM(player_total_number_of_tackles) * 100, 2) AS 'Tackle Success Rate(%)'
                 FROM player_match_stats
                 JOIN Player ON player.player_id = player_match_stats.player_id
                 WHERE player_match_stats.player_id in
                     (SELECT player_id
                      FROM
                        (SELECT player_id,
                                SUM(player_total_number_of_tackles) AS total_tackles
                         FROM player_match_stats
                         GROUP BY player_id
                         ORDER BY total_tackles DESC
                         LIMIT 10) AS T)
                 GROUP BY player_match_stats.player_id ;
                                """;
        }

        /**
         *  Query-6: The success and failure rates of teams in knockout or decider matches.
         * 
         * @author Punith kandula, Vatsal Thakkar
         * @return SQL Query String
         */
        public String query6() {
                return """
                        SELECT team_name AS 'Team Name',
                        ROUND(matches_lost/matches_played*100, 2) 'Losing Rate(%)',
                        ROUND(matches_won/matches_played*100, 2) 'Winning Rate(%)',
                        matches_played AS 'Total Deciders Played'
                 FROM
                   (SELECT team_id,
                           COUNT(CASE
                                     WHEN team_id != match_winner_id THEN 1
                                 END) matches_lost,
                           COUNT(CASE
                                     WHEN team_id= match_winner_id THEN 1
                                 END) matches_won,
                           COUNT(team_id) matches_played
                    FROM pkl.match AS M
                    JOIN team_match_stats AS TMS ON M.match_id = TMS.match_id
                    WHERE match_number NOT LIKE 'MATCH%'
                    GROUP BY team_id) AS T1
                 JOIN team ON T1.team_id = team.team_id;
                                """;
        }
}
