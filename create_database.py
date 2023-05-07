#=============================================
#   Python file to create database and database tables and populate them
#   author = VATSAL THAKKAR
#=============================================
# Import libraries
import pandas as pd
import mysql.connector
import os

# Get Data From .csv files
match_df = pd.read_csv("./Final_Dataset/match.csv" ,low_memory=False) 
player_df = pd.read_csv("./Final_Dataset/player.csv" ,low_memory=False) 
player_match_stats_df = pd.read_csv("./Final_Dataset/player_match_stats.csv" ,low_memory=False) 
season_df = pd.read_csv("./Final_Dataset/season.csv" ,low_memory=False) 
season_has_teams_has_players_df = pd.read_csv("./Final_Dataset/season_has_teams_has_players.csv" ,low_memory=False) 
team_df = pd.read_csv("./Final_Dataset/team.csv" ,low_memory=False) 
team_match_stats_df = pd.read_csv("./Final_Dataset/team_match_stats.csv" ,low_memory=False) 
venue_df = pd.read_csv("./Final_Dataset/venue.csv" ,low_memory=False) 

# Defining database name
database_name = "pkl"

# Queries to create Tables
create_season_table_query = f"""
        CREATE TABLE IF NOT EXISTS {database_name}.Season (
            season_id INT NOT NULL,
            season_name VARCHAR(45) NULL,
            year INT NULL,
            PRIMARY KEY (season_id))
    """

create_team_table_query =f'''
    CREATE TABLE IF NOT EXISTS {database_name}.Team (
    team_id INT NOT NULL,
    team_name VARCHAR(45) NULL,
    short_name VARCHAR(45) NULL,
    PRIMARY KEY (team_id))
    '''

create_venue_table_query = f'''
    CREATE TABLE IF NOT EXISTS {database_name}.Venue (
    venue_id INT NOT NULL,
    venue_name VARCHAR(45) NULL,
    venue_city VARCHAR(45) NULL,
    team_id INT NOT NULL,
    PRIMARY KEY (venue_id),
    FOREIGN KEY (team_id)
    REFERENCES {database_name}.Team (team_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    '''

create_match_table_query = f"""
    CREATE TABLE IF NOT EXISTS {database_name}.Match (
    match_id INT NOT NULL,
    match_number VARCHAR(20) NULL,
    date DATE NULL,
    start_time VARCHAR(10) NULL,
    toss_winner_id INT NOT NULL,
    match_winner_id INT NOT NULL,
    season_id INT NOT NULL,
    venue_id INT NOT NULL,
    PRIMARY KEY (match_id),
    FOREIGN KEY (season_id)
    REFERENCES {database_name}.Season (season_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    FOREIGN KEY (venue_id)
    REFERENCES {database_name}.Venue (venue_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    """

create_player_table_query = f'''
    CREATE TABLE IF NOT EXISTS {database_name}.Player (
    player_id INT NOT NULL,
    player_name VARCHAR(45) NULL,
    PRIMARY KEY (player_id))'''

create_season_has_teams_has_player_table_query = f'''
    CREATE TABLE IF NOT EXISTS {database_name}.Season_has_teams_has_players (
    team_id INT NOT NULL,
    season_id INT NOT NULL,
    player_id INT NOT NULL,
    PRIMARY KEY (team_id, season_id, player_id),
    FOREIGN KEY (team_id)
    REFERENCES {database_name}.Team (team_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (season_id)
    REFERENCES {database_name}.Season (season_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (player_id)
    REFERENCES {database_name}.Player (player_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)'''

create_team_match_stats_table_query = f'''
    CREATE TABLE IF NOT EXISTS {database_name}.Team_match_stats (
    team_id INT NOT NULL,
    match_id INT NOT NULL,
    score INT NULL,
    total_All_outs INT NULL,
    Total_declares INT NULL,
    all_out_points INT NULL,
    extra_points INT NULL,
    raid_bonus_points INT NULL,
    total_raid_points INT NULL,
    raid_touch_points INT NULL,
    tackle_capture_points INT NULL,
    tackle_capture_bonus_points INT NULL,
    total_tackle_points INT NULL,
    total_points_in_match INT NULL,
    empty_raids INT NULL,
    successful_raids INT NULL,
    super_raids INT NULL,
    total_number_of_raids INT NULL,
    unsuccessful_raids INT NULL,
    successful_tackles INT NULL,
    super_tackles INT NULL,
    total_number_of_tackles INT NULL,
    unsuccessful_tackles INT NULL,
    PRIMARY KEY (team_id, match_id),
    FOREIGN KEY (match_id)
    REFERENCES {database_name}.Match (match_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (team_id)
    REFERENCES {database_name}.Team (team_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)'''

create_player_match_stats_table_query =f'''
    CREATE TABLE IF NOT EXISTS {database_name}.Player_match_stats (
    player_id INT NOT NULL,
    match_id INT NOT NULL,
    Player_played VARCHAR(20) NULL,
    Player_captain VARCHAR(20) NULL,
    Player_on_court VARCHAR(20) NULL,
    player_on_starter VARCHAR(20) NULL,
    player_total_points INT NULL,
    player_total_raid_points INT NULL,
    Player_raid_touch_points INT NULL,
    Player_raid_bonus_points INT NULL,
    Player_total_tackle_points INT NULL,
    Player_tackle_touch_points INT NULL,
    player_tackle_capture_bonus_points INT NULL,
    Player_total_number_of_raids INT NULL,
    Player_successful_raids INT NULL,
    player_unsuccessful_raids INT NULL,
    player_empty_raids INT NULL,
    player_total_number_of_tackles INT NULL,
    player_successful_tackles INT NULL,
    player_unsuccessful_tackles INT NULL,
    PRIMARY KEY (player_id, match_id),
    FOREIGN KEY (match_id)
    REFERENCES {database_name}.Match (match_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    FOREIGN KEY (player_id)
    REFERENCES {database_name}.Player (player_id)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)'''


# Create database connection
cnx = mysql.connector.connect(
    host=os.getenv("DB_HOST", "localhost"),
    user=os.getenv("DB_USER", "root"),
    password=os.getenv("DB_PASS", "") # You need to set default password or DB_PASS according to your database configuration
)#                                ⬆




# create a cursor object to execute SQL queries
cursor = cnx.cursor()

try:
    # execute SQL query to create new database
    drop_database_query = f'DROP DATABASE IF EXISTS {database_name}'
    create_database_query = f"CREATE DATABASE IF NOT EXISTS {database_name}"
    cursor.execute(drop_database_query)
    cursor.execute(create_database_query)

    cnx.commit()

    
    # Change Database
    cursor.execute(f"USE {database_name}")


    # Execute create table queries and create database tables
    cursor.execute(create_season_table_query)
    cursor.execute(create_team_table_query)
    cursor.execute(create_venue_table_query)
    cursor.execute(create_match_table_query)
    cursor.execute(create_player_table_query)
    cursor.execute(create_season_has_teams_has_player_table_query)
    cursor.execute(create_team_match_stats_table_query)
    cursor.execute(create_player_match_stats_table_query)
    cnx.commit()

    # Populate database tables using insert queries

    cursor.execute(f"USE {database_name}")

    for i, row in season_df.iterrows():
        query = "INSERT INTO pkl.Season (season_id, season_name, year) VALUES (%s, %s, %s)"
        values = (row["season_id"], row["season_name"], row["year"])
        cursor.execute(query, values)

    cnx.commit()

    for i, row in team_df.iterrows():
        query = "INSERT INTO pkl.Team (team_id, team_name, short_name) VALUES (%s, %s, %s)"
        values = (row["team_id"], row["team_name"], row["short_name"])
        cursor.execute(query, values)

    cnx.commit()

    for i,row in venue_df.iterrows():
        query = "INSERT INTO pkl.Venue (venue_id, venue_name, venue_city, team_id) VALUES (%s, %s, %s, %s)"
        values = (row["venue_id"], row["venue_name"], row["venue_city"],row['team_id'])
        cursor.execute(query, values)

    cnx.commit()

    for i,row in match_df.iterrows():
        query = """INSERT INTO pkl.Match 
        (match_id, match_number, date, start_time, toss_winner_id, match_winner_id, season_id, venue_id) 
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s)"""
        values = (row["match_id"], row["match_number"], row["date"],row['start_time'],row["toss_winner_id"], row["match_winner_id"], row["season_id"],row['venue_id'])
        cursor.execute(query, values)

    cnx.commit()

    for i, row in player_df.iterrows():
        query = "INSERT INTO pkl.Player (player_id, player_name) VALUES (%s, %s)"
        values = (row["player_id"], row["player_name"])
        cursor.execute(query, values)

    cnx.commit()

    for i, row in season_has_teams_has_players_df.iterrows():
        query = "INSERT INTO pkl.Season_has_teams_has_players (team_id, season_id, player_id) VALUES (%s, %s, %s)"
        values = (int(row["team_id"]), int(row["season_id"]), int(row["player_id"]))
        cursor.execute(query, values)


    cnx.commit()

    for i, row in team_match_stats_df.iterrows():
        query = """INSERT INTO pkl.Team_match_stats (team_id, match_id, score, total_All_outs, total_declares,
            all_out_points, extra_points, raid_bonus_points,
            total_raid_points, raid_touch_points, tackle_capture_points,
            tackle_capture_bonus_points, total_tackle_points,
            total_points_in_match, empty_raids, successful_raids,
            super_raids, total_number_of_raids, unsuccessful_raids,
            successful_tackles, super_tackles, total_number_of_tackles,
            unsuccessful_tackles) VALUES (%s, %s, %s,%s, %s, %s, %s,%s, %s, %s, %s,%s, %s, %s, %s,%s, %s, %s, %s,%s, %s, %s, %s)"""
        values = (int(row["team_id"]), int(row["match_id"]), int(row["score"]),int(row["total_All_outs"]),int(row["total_declares"]), int(row["all_out_points"]), int(row["extra_points"]),int(row["raid_bonus_points"]),int(row["total_raid_points"]), int(row["raid_touch_points"]), int(row["tackle_capture_points"]),int(row["tackle_capture_bonus_points"]),int(row["total_tackle_points"]), int(row["total_points_in_match"]), int(row["empty_raids"]),int(row["successful_raids"]),int(row["super_raids"]), int(row["total_number_of_raids"]), int(row["unsuccessful_raids"]),int(row["successful_tackles"]), int(row["super_tackles"]), int(row["total_number_of_tackles"]),int(row["unsuccessful_tackles"]))
        cursor.execute(query, values)

    cnx.commit()

    for i, row in player_match_stats_df.iterrows():
        query = """INSERT INTO pkl.Player_match_stats (match_id, player_id, player_played, player_captain,
            player_on_court, player_on_starter, player_total_points,
            player_total_raid_points, player_raid_touch_points,
            player_raid_bonus_points, player_total_tackle_points,
            player_tackle_touch_points, player_tackle_capture_bonus_points,
            player_total_number_of_raids, player_successful_raids,
            player_unsuccessful_raids, player_Empty_raids,
            player_total_number_of_tackles, player_successful_tackles,
            player_unsuccessful_tackles) VALUES (%s, %s, %s, %s,%s, %s, %s, %s,%s, %s, %s, %s,%s, %s, %s, %s,%s, %s, %s, %s)"""
        values = (int(row["match_id"]), int(row["player_id"]), row["player_played"],row["player_captain"], row["player_on_court"], row["player_on_starter"],int(row["player_total_points"]), int(row["player_total_raid_points"]), int(row["player_raid_touch_points"]),int(row["player_raid_bonus_points"]), int(row["player_total_tackle_points"]), int(row["player_tackle_touch_points"]),int(row["player_tackle_capture_bonus_points"]), int(row["player_total_number_of_raids"]), int(row["player_successful_raids"]),int(row["player_unsuccessful_raids"]), int(row["player_Empty_raids"]), int(row["player_total_number_of_tackles"]),int(row["player_successful_tackles"]), int(row["player_unsuccessful_tackles"]))
        cursor.execute(query, values)
    
    cnx.commit()
    print("Pro-Kabaddi League Database is created successfully🥳!! (Name of Database is pkl)")

finally:
    
    cursor.close()
    cnx.close()
 