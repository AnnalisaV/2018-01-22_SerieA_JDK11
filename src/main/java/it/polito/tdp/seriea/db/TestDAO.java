package it.polito.tdp.seriea.db;

import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class TestDAO {

	public static void main(String[] args) {
		SerieADAO dao = new SerieADAO();

		List<Season> seasons = dao.listAllSeasons();
		System.out.println(seasons);
		System.out.println("Seasons # rows: " + seasons.size());

		List<Team> teams = dao.listTeams();
		System.out.println(teams);
		System.out.println("Teams # rows: " + teams.size());
		
		
		Map<Integer, Integer> mappa= dao.getPointsHomeTeam(new Team("Napoli")); 
		//for (Integer season : mappa.ke)
		
	}

}
