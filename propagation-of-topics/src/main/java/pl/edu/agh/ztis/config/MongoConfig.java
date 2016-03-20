package pl.edu.agh.ztis.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;


@Configuration
@EnableMongoRepositories(basePackages={"pl.edu.agh.ztis"})
@ComponentScan(basePackages={"pl.edu.agh.*"})
class MongoConfig extends AbstractMongoConfiguration {

	@Override
	protected String getDatabaseName() {
		return "notes";
	}

	@Override
	@SuppressWarnings("deprecation") // ignored for purpose, it should work that way for now
	public Mongo mongo() throws Exception {
		  return new Mongo();
	}
	
}
