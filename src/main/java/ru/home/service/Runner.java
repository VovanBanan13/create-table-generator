package ru.home.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

	private final FileService fileService;

	@Override
	public void run(String... args) throws Exception {

		Scanner console = new Scanner(System.in);

		System.out.println("\n *** Генератор *** \n");

		fileService.createFile(fileService.generateSql(fileService.readCsv()));
	}
}
