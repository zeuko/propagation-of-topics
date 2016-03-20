package pl.edu.agh.ztis.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Sample application used to test if MongoDB is up and running, might be usable while developing.
 * 
 * 
 * @author Patrycja
 */
@Component
public class TestMongoDBApplication {

	@Autowired
	private TestNotesBean readNotesBean;

	private void test() {
		readNotesBean.saveTestObject();
	}

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		TestMongoDBApplication testApp = context.getBean(TestMongoDBApplication.class);
		testApp.test();
	}
}
