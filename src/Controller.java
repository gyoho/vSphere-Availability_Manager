
public class Controller {

	public static void main(String[] args) throws Exception {
		MapperTest test = new MapperTest();
		test.test();
		
		AvailabilityManager availabilityManager = new AvailabilityManager();
		availabilityManager.start();
	}
}
