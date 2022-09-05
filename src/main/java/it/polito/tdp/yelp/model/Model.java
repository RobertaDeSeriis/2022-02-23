package it.polito.tdp.yelp.model;

import java.sql.Date;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;


public class Model {
	
	YelpDao dao; 
	Graph <Review, DefaultWeightedEdge> grafo; 
	private List<Review> best; 
	
	public Model() {
		this.dao=new YelpDao(); 
	}
	
	public List<String> getCity() {
		return dao.getCity();
	}
	
	public List<Business> getLocali(String city){
		return dao.getAllBusiness(city);
	}
	
	public String creaGrafo(String city, Business locale) {
		this.grafo= new DefaultDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		List<Review> vertici= dao.getVertici(city, locale);
		Graphs.addAllVertices(this.grafo, vertici);
		double peso=0; 
		
		for(Review r: vertici) {
			for (Review r1: vertici) {
				
				if(r!= r1 && r.getDate().isBefore(r1.getDate())) {
				 peso = (int) ChronoUnit.DAYS.between(r.getDate(), r1.getDate());
				 if (peso>0)
					 Graphs.addEdge(this.grafo, r, r1, peso);
				 if (peso<0)
					 Graphs.addEdge(this.grafo, r1, r, (-1)*peso);
					
				}
				
			}
		}
		
		return "Grafo creato!\n# Vertici:"+grafo.vertexSet().size()+ "\n# Archi: "+grafo.edgeSet().size();	
	}
	
	public List<String> getUscenti(){ // recensione per cui il numero di archi uscenti sia massimo
		//Stampare: review_id e il relativo numero di archi uscenti
		List<String> uscenti= new LinkedList<>(); 
		int max=0; 
		for (Review r: grafo.vertexSet()) { //ciclo sugli archi
			int p= Graphs.successorListOf(this.grafo,r).size(); //peso= peso arco
			if(p>max) {
				max=p; 
			}
		}for (Review r: grafo.vertexSet()) { //ciclo sugli archi
			int p= Graphs.successorListOf(this.grafo,r).size(); //peso= peso arco
			if(p==max) {
				uscenti.add("\n" +r.getReviewId()+"     # Archi uscenti: "+max);
			}
		}
		
		return uscenti; 
		
	}
	
	
	
	public List<Review> trovaPercorso(){
		this.best = new ArrayList<>();
		List<Review> parziale = new ArrayList<>();
		parziale.addAll(grafo.vertexSet());
		Collections.sort(parziale);
		//lancio la ricorsione
		ricorsione(parziale);
		
		return best;
	}
	
	private void ricorsione(List<Review> parziale) {
		
		Review ultimo = parziale.get(parziale.size() - 1);
		//ottengo i vicini
		List<Review> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		for(Review v : vicini) {
			if(!parziale.contains(v) && v.getStars()>=ultimo.getStars()) {
				parziale.add(v);
				ricorsione(parziale);
				parziale.remove(v);
			} 
		}
		if(parziale.size() > best.size()) {
			this.best = new ArrayList<>(parziale);
		}
		
	}

	public List<Review> getBest() {
		return best;
	}

	public void setBest(List<Review> best) {
		this.best = best;
	}
	
	
	}
