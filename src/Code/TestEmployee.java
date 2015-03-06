package Code;

public class TestEmployee extends junit.framework.TestCase {

	public void testEmployee() {
		
		Employee testEmployee = new Employee("Ola", "CEO", "ola1","passord", 12345678, false);
	
			assertEquals("Ola", testEmployee.getName());
		testEmployee.setName("Kari");
			assertEquals("Kari", testEmployee.getName());
			assertEquals("CEO", testEmployee.getPosition());
		testEmployee.setPosition("Konsulent");
			assertEquals("Konsulent", testEmployee.getPosition());
			assertEquals("ola1", testEmployee.getUsername());
		testEmployee.setUsername("kari1");
			assertEquals("kari1", testEmployee.getUsername());
			assertEquals("passord", testEmployee.getPassword());
		testEmployee.setPassword("hemmelig");
			assertEquals("hemmelig", testEmployee.getPassword());
		//	assertEquals(12345678, testEmployee.getTelnum());
			
	//	Group testGruppe = new Group("Squash", "Squashgruppen i selskapet", testEmployee);
		
	//	assertTrue(testEmployee.joinGroup(testGruppe));			
	}
}
