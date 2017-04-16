package hu.bvillei.todolist.config;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class AppConfig {

	@Bean
	public MailSender mailSender() throws IOException {
		Resource resource = new ClassPathResource("/mail-smtp.properties");
		Properties props = PropertiesLoaderUtils.loadProperties(resource);

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setJavaMailProperties(props);
		mailSender.setPassword(props.getProperty("mail.smtp.password"));
		return mailSender;
	}
}
