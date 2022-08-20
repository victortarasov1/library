package com.example.library;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.library.controller.LibraryRestController;

@SpringBootTest
public class LibraryApplicationTest {
	@Autowired
	LibraryRestController restController;
	
	@Test
	public void contextLoads() {
		assertThat(restController).isNotNull();
	}
}
