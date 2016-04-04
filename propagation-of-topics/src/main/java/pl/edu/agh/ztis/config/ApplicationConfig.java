package pl.edu.agh.ztis.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import pl.edu.agh.ztis.ReaderFromFiles;

@Configuration
@ComponentScan(basePackageClasses = ReaderFromFiles.class, excludeFilters = @Filter({Controller.class, Configuration.class}))
@Import(MongoConfig.class)
@EnableScheduling
class ApplicationConfig {
	
}