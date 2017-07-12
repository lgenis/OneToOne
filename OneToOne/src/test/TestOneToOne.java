package test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dao.DBManager;
import model.Address;
import model.Employee;


public class TestOneToOne {
	DBManager dbManager;
	Employee employee;
	
	
	//Esto se ejecuta antes de cualquier test
	@Before
	public void init(){
		dbManager = new DBManager();
		dbManager.connect();
		dbManager.deleteAll(Employee.class);
		dbManager.close();
	}
	
	//@Test
	public void testInsert(){
		ArrayList<Address> listAddress = new ArrayList<Address>();
		ArrayList<Employee> listEmploy = new ArrayList<Employee>();
		
		
		listAddress.add(getMockAddress("Calle test", 3306, "Barcelona"));
		listAddress.add(getMockAddress("Calle test2", 4400, "Valencia"));
		listAddress.add(getMockAddress("Calle test3", 2121, "Madrid"));
		
		listEmploy.add(getMockEmployee("Pedro", "Picapiedra" ));
		listEmploy.add(getMockEmployee("Peter", "Parra" ));
		listEmploy.add(getMockEmployee("Paula", "Perez" ));
		
		//Employee recovered = null;
		//employee.setAddress(address);
		
		
		
		dbManager.connect();
		for (int i=0; i<listEmploy.size(); i++){
			listEmploy.get(i).setAddress(listAddress.get(i));
			dbManager.insert(listEmploy.get(i));
		}
			
		dbManager.close();
		/*
		dbManager.connect();
			 recovered=(Employee) dbManager
					 .find(Employee.class, employee.getId());
		dbManager.close();
		*/
		Assert.assertEquals(3, listEmploy.size());
		
		for (int i=0; i<listEmploy.size(); i++){
			Assert.assertEquals(true, listEmploy.get(i).getId()>0);
			Assert.assertEquals(true, listEmploy.get(i).getAddress().getId()>0);
		}
	}
	
	@Test
	public void testSelect() {
		boolean result = true;
		DBManager dbManager = 
				new DBManager();
		Employee employee1 = getMockEmployee("Pablo", "Pozo");
		Address address1 = getMockAddress("Calle supertest", 1234, "Zaragoza");
		Employee employee2 = getMockEmployee("Pachi", "Pui");
		Address address2 = getMockAddress("Calle supertest2", 5246, "Bilbao");
		
		employee1.setAddress(address1);
		employee2.setAddress(address2);
		
		ArrayList<Employee> results = null;
		try {
			dbManager.connect();
			
			dbManager.insert(employee1);
			dbManager.insert(employee2);
			results = dbManager.selectEqual(Employee.class, "address.city", "Zaragoza");
			

		} catch (Exception e) {
			result=false;
			e.printStackTrace();
		}finally{
			dbManager.close();
		} 
		

		Assert.assertEquals(1, results.size());
		Assert.assertEquals(employee1.getAddress().getCity(), results.get(0).getAddress().getCity());
	}
	
	@Test
	public void testUpdate(){
		Address address = getMockAddress("Calle testito", 3306, "Sevilla");
		Employee employee = getMockEmployee("Pirulo" , "Palo");
		employee.setAddress(address);
		
		dbManager.connect();
			dbManager.insert(employee);
		dbManager.close();
		
		dbManager.connect();
			dbManager.getEntitymanager().getTransaction().begin();
			
				Employee employeeUpdate = dbManager
						.getEntitymanager()
							.find(Employee.class, employee.getId());
						
				employeeUpdate.setName("UpdateTestName");
				employeeUpdate.setSurname("UpdateTestSurname");
				Address newAddress = getMockAddress("Update Stree", 888, "San Sebastian");
				employeeUpdate.setAddress(newAddress);
				employeeUpdate.getAddress()
								.setCity("Donostia");
				employeeUpdate.getAddress()
								.setStreet("Paseo de la Concha");
				
			dbManager.getEntitymanager().getTransaction().commit();
		dbManager.close();
		
		Assert.assertEquals("UpdateTestName", employeeUpdate.getName());
		Assert.assertEquals("UpdateTestSurname", employeeUpdate.getSurname());
		Assert.assertEquals("Donostia", employeeUpdate.getAddress().getCity());
		Assert.assertEquals(true, employee.getAddress().getId()>0);
	}
	
	
	
	private Employee getMockEmployee(String name, String surname){
		Employee employee = new Employee();
		employee.setName(name);
		employee.setSurname(surname);
		return employee;
	}
	
	private Address getMockAddress(String street, int number, String city){
		Address address = new Address();
		address.setStreet(street);
		address.setNumber(number);
		address.setCity(city);
		address.setZipCode("08001" + number);
		return address;
		
	}
}
