package it.polito.tdp.yelp.model;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;


public class Model {
	
	private YelpDao dao; 
	private Graph<Review, DefaultWeightedEdge> grafo; 
	List <String> city;
	List <Business> locali;
	List<Review> reviews;
	//Map<Business, Review> idMap; 
	
	
	
	public Model() {
		this.dao = new YelpDao(); 
		this.city = dao.getCitta();
		
		//this.dao.getAllReviews(idMap); 
	}


	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

	public List<Business> getLocali(String c) { //inserisci la stringa in base a cui filtrare
		return locali=dao.getLocaliCitta(c);
	}


	public  String creaGrafo(String l) {
		this.grafo= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		 
		//Aggiungi vertici 
		this.reviews= dao.getVertici(l);
		Graphs.addAllVertices(this.grafo, this.reviews);
		return "Grafo creato con " +this.grafo.vertexSet().size()+ " vertici";
	}
	
	}
