package pl.edu.agh.ztis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class Application {

	@Autowired
	private ReadNotesBean readNotesBean;
	
	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-config.xml");
		Application app = context.getBean(Application.class);
		
		// do sth with app
	}
}
