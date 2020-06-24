package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;

import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * Punti della stagione per la Team passata che gioca in casa
	 * @param home
	 * @return
	 */
	public Map<Integer, Integer> getPointsHomeTeam(Team home){
		String sql="SELECT season, 3*COUNT(FTR) AS punti " + 
				"FROM matches " + 
				"WHERE hometeam=? AND FTR='H' " + 
				"GROUP BY season " + 
				"ORDER by season asc";
		Map<Integer,Integer> map= new HashMap<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, home.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				map.put(res.getInt("season"), res.getInt("punti"));
			}

			conn.close();
			return map;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	/**
	 * Punti della stagione per la Team passata che gioca in trasferta
	 * @param home
	 * @return
	 */
	public Map<Integer,Integer> getPointsAwayTeam(Team away){
		String sql="SELECT season, 3*COUNT(FTR) AS punti " + 
				"FROM matches " + 
				"WHERE awayteam=? AND FTR='A' " + 
				"GROUP BY season " + 
				"ORDER by season asc";
		Map<Integer,Integer> map= new HashMap<>(); 
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, away.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				map.put(res.getInt("season"), res.getInt("punti"));
			}

			conn.close();
			return map;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}


	/**
	 * Punti pareggio  della stagione per la Team passata che gioca in trasferta o a casa
	 * @param home
	 * @return
	 */
	public Map<Integer, Integer> getPointsPareggio(Team team){
		String sql="SELECT season, COUNT(FTR) AS punti " + 
				"FROM matches " + 
				"WHERE awayteam=? OR hometeam=? AND FTR='D' " + 
				"GROUP BY season " + 
				"ORDER by season asc";
		Map<Integer, Integer> map= new HashMap<>(); 
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, team.getTeam());
			st.setString(2, team.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				map.put(res.getInt("season"), res.getInt("punti"));
			}

			conn.close();
			return map;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

}

