package com.korit12.demo;

import com.korit12.demo.domain.AppUser;
import com.korit12.demo.domain.AppUserRepository;
import com.korit12.demo.domain.Content;
import com.korit12.demo.domain.ContentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	private final AppUserRepository appUserRepository;
	private final ContentRepository contentRepository;

	public Application(AppUserRepository appUserRepository, ContentRepository contentRepository) {
		this.appUserRepository = appUserRepository;
		this.contentRepository = contentRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		AppUser u1 = new AppUser("jmw0818", "1234", "admin");
		AppUser u2 = new AppUser("aa", "1111", "user");

		appUserRepository.saveAll(Arrays.asList(u1, u2));

		List<Content> contents = Arrays.asList(
				new Content("정처기 시험 준비", "정보처리기사 시험 한달 남았는데 어떻게 준비해야함?", u1),
				new Content("창원대학교", "창원대 학생들 수준 ㅋ", u2)
		);

		contentRepository.saveAll(contents);
	}

}
