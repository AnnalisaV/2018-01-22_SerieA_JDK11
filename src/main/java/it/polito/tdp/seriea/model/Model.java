package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {

	private SerieADAO dao; 
	private Graph<Integer, DefaultWeightedEdge> graph; 
	
	public Model() {
		this.dao= new SerieADAO(); 
	}
	
	public List<Team> getTeams(){
		return dao.listTeams();
	}
	
	public Map<Integer, Integer> getSeasonPoints(Team team){
		Map<Integer,Integer> map= new HashMap<>();
		
		Map<Integer, Integer> winHome= dao.getPointsHomeTeam(team);
		
		for (Integer season: winHome.keySet()) {
			if(!map.containsKey(season)) {
				map.put(season, winHome.get(season));
			}
			else {
				//season gia' presente, aggiungo al punteggio presente quello nuovo
				map.put(season, map.get(season)+winHome.get(season)); 
			}
		}
		
		//idem per punteggi di Trasferta e pareggi
       Map<Integer, Integer> winAway= dao.getPointsAwayTeam(team);
		
		for (Integer season: winAway.keySet()) {
			if(!map.containsKey(season)) {
				map.put(season, winAway.get(season));
			}
			else {
				//season gia' presente, aggiungo al punteggio presente quello nuovo
				map.put(season, map.get(season)+winAway.get(season)); 
			}
		}
		 Map<Integer, Integer> pareggi= dao.getPointsPareggio(team);
			
			for (Integer season: pareggi.keySet()) {
				if(!map.containsKey(season)) {
					map.put(season, pareggi.get(season));
				}
				else {
					//season gia' presente, aggiungo al punteggio presente quello nuovo
					map.put(season, map.get(season)+pareggi.get(season)); 
				}
			}
			
			return map; 
	}
	
	private void creaGrafo(Team team) {
		this.graph= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class); 
		
		Map<Integer, Integer> valoriSquadra= getSeasonPoints(team);
		
		//vertex (le stagioni giocate dalla squadra)
		Graphs.addAllVertices(graph,  valoriSquadra.keySet()); 
		
		//edge 
		for (Integer s1: valoriSquadra.keySet()) {
			for (Integer s2 : valoriSquadra.keySet()) {
				                      // così giro una volta sola 
				if (!s1.equals(s2) && s1>s2) {
					//il punteggio nella prima stagione e' > di quello nella seconda, arco da 2 a 1
					if (valoriSquadra.get(s1) > valoriSquadra.get(s2)) {
						int peso= valoriSquadra.get(s1)-valoriSquadra.get(s2);
						Graphs.addEdge(graph, s2, s1, peso); 
					}
					else {
						//la seconda stagione ha fatto piu' punti , arco da 1 a 2
						int peso= valoriSquadra.get(s2)-valoriSquadra.get(s1);
						Graphs.addEdge(graph, s1, s2, peso); 
					}
				}
			}
		}
		
		System.out.println("Grafo creato : "+graph.vertexSet().size()+" vertex and "+graph.edgeSet().size()+" edges");
		
		
	}
	
	public String annataDOro(Team team) {
		creaGrafo(team); 
		 
		int goldSeason=0; 
		int goldPoints=0; // a caso
		
		for (Integer seas : graph.vertexSet()) {
			
			List<Integer> entranti= Graphs.predecessorListOf(graph, seas); 
			List<Integer> uscenti= Graphs.successorListOf(graph, seas); 
			
			int sommaEnt=0; //somma archi entranti
			int sommaUsc=0; //somma archi uscenti
			
			if( entranti.size() !=0) {
	
			for (Integer ent : entranti) {
				                                                 // rispetto l'ordine altriementi non mi trova l'arco
				sommaEnt+=(int) graph.getEdgeWeight(graph.getEdge(ent, seas)); 
			}
			}
			if (uscenti.size() !=0) {
			for (Integer usc : uscenti) {
				sommaUsc+= (int) graph.getEdgeWeight(graph.getEdge(seas, usc)); 
			}
			}
			
			int somma= sommaEnt-sommaUsc; 
			// se sono nei valori max allora aggiorno
			if (somma>goldPoints) {
				goldPoints= somma; 
				goldSeason= seas; 
			}
		}
		
		return "Stagione migliore "+goldSeason+" con differenza di punti "+goldPoints; 
	}
}
