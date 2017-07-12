package dao;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;


/**
 * 
Java Persistence API, más conocida por sus siglas JPA, es la API de persistencia 
desarrollada para la plataforma Java EE. 
Es un framework del lenguaje de programación Java que maneja datos relacionales 
en aplicaciones usando la Plataforma Java en sus ediciones Standard (Java SE) y
Enterprise (Java EE). La JPA se origina a partir del trabajo del JSR 220 Expert
Group el cual correspondía a EJB3. JPA 2.0 sería el trabajo del JSR 317 y 
posteriormente JPA 2.1 en el JSR 338.
Persistencia en este contexto cubre tres áreas:
La API en sí misma, definida en el paquete javax.persistence
El lenguaje de consulta Java Persistence Query Language (JPQL).
Metadatos objeto/relacional.
El objetivo que persigue el diseño de esta API es no perder las ventajas de 
la orientación a objetos al interactuar con una base de datos 
(siguiendo el patrón de mapeo objeto-relacional), como sí pasaba con EJB2,
 y permitir usar objetos regulares (conocidos como POJOs).
 
 * @author campino
 *
 */
public class DBManager { 
	


	private static final String PERSISTENCE_UNIT_NAME = "OneToOne"; 
	private static EntityManagerFactory factory=null;
	
	EntityManager entitymanager; 
	
	public static EntityManagerFactory getEMF(){ 
		factory = factory==null? Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME)
				: factory;
		return factory; 
	}
	
	public EntityManager getEntitymanager() {
		return entitymanager;
	}

	
	public void connect() {
		entitymanager = getEMF().createEntityManager();
	}

	public void insert(Object object){			  
		entitymanager.getTransaction().begin();
			entitymanager.persist(object);
		entitymanager.getTransaction().commit(); 
	}




	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object find(Class clazz , int id)  {
		entitymanager.getTransaction().begin();
		Object object=entitymanager.find(clazz, id);
		entitymanager.getTransaction().commit();
		return object;
	}

	public <T> void deleteAll(Class<T> clazz){
		entitymanager.getTransaction().begin();
		ArrayList<T> list = selectAll(clazz);
				for (T object: list)
					entitymanager.remove(object);
		entitymanager.getTransaction().commit();
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> selectAll(Class<T> clazz){
		Query query = entitymanager.createQuery("SELECT c FROM "+ clazz.getSimpleName() +" c");		  
		 return new ArrayList<T>(query.getResultList()); 
	}

	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> selectEqual(Class<T> clazz,String column,String equal){
		  Query query = entitymanager.createQuery(
				    "SELECT c FROM "+clazz.getSimpleName()+" c "
				  	+ "WHERE c."+column+"='"+equal+"'");
		  return new ArrayList<T>(query.getResultList()); 
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> selectLike(Class<T> clazz,String column,String like){
		  Query query = entitymanager.createQuery(
				    "SELECT c FROM "+clazz.getSimpleName()+" c "
				  	+ "WHERE c."+column+" LIKE '"+like+"%'");
	      
		  return new ArrayList<T>(query.getResultList()); 
	}
	
	@SuppressWarnings("unchecked")
	public <T> ArrayList<T> selectBt(Class<T> clazz,String column,String bt){
		  Query query = entitymanager.createQuery(
				    "SELECT c FROM "+clazz.getSimpleName()+" c "
				  	+ "WHERE c."+column+" "+bt);	    
		  return new ArrayList<T>(query.getResultList()); 
	}
	
	   
	public void close() {
		entitymanager.close();
		entitymanager = null; 
	}


}