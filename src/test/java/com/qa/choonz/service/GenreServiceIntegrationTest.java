package com.qa.choonz.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.qa.choonz.persistence.domain.Album;
import com.qa.choonz.persistence.domain.Genre;
import com.qa.choonz.persistence.repository.AlbumRepository;
import com.qa.choonz.persistence.repository.GenreRepository;
import com.qa.choonz.rest.dto.GenreDTO;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
public class GenreServiceIntegrationTest {

	@Autowired
	private AlbumRepository aRepo;

	@Autowired
	private GenreService service;

	@Autowired
	private GenreRepository repo;

	static ExtentReports report = new ExtentReports("Documentation/reports/Genre_Service_Integration_Report.html",
			true);
	static ExtentTest test;

	private List<Long> emptyList = new ArrayList<Long>();
	private List<Genre> genre = new ArrayList<Genre>();
	private List<GenreDTO> genreDTO = new ArrayList<GenreDTO>();
	private Album validAlbum = new Album(1, "test", null, null, null, "test");
	private List<Album> validAlbums = new ArrayList<Album>();
	private Genre validGenre;
	private GenreDTO validGenreDTO;

	@BeforeEach
	public void init() {
		repo.deleteAll();
		validAlbum = aRepo.save(validAlbum);
		validGenre = new Genre(1, "test", "test", validAlbums);
		genre = new ArrayList<Genre>();
		genreDTO = new ArrayList<GenreDTO>();
		validGenre = repo.save(validGenre);
		validGenreDTO = service.map(validGenre);
		genre.add(validGenre);
		genreDTO.add(validGenreDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	public void createGenreTest() {
		test = report.startTest("Create genre test");
		GenreDTO newGenre = new GenreDTO("test2", "test2");
		GenreDTO expectedGenre = new GenreDTO(validGenre.getId() + 1, "test2", "test2", emptyList);
		assertThat(expectedGenre).isEqualTo(service.create(newGenre));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readAllGenreTest() {
		test = report.startTest("Read genres test");
		List<GenreDTO> genreInDb = service.read();
		assertThat(genreDTO).isEqualTo(genreInDb);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readGenreIdTest() {
		test = report.startTest("Read genre by id test");
		assertThat(validGenreDTO).isEqualTo(service.read(validGenre.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readGenreNameTest() {
		test = report.startTest("Read genre by name test");
		assertThat(validGenreDTO).isEqualTo(service.read(validGenre.getName()));
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updateGenreTest() {
		test = report.startTest("Updated genre test");
		GenreDTO sentGenre = new GenreDTO("updated", "updated");
		GenreDTO responseGenre = new GenreDTO(validGenre.getId(), "updated", "updated", emptyList);
		GenreDTO updatedGenre = service.update(sentGenre, validGenre.getId());

		assertThat(responseGenre).isEqualTo(updatedGenre);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteGenreTest() {
		test = report.startTest("Deleted genre test");
		assertThat(true).isEqualTo(service.delete(validGenre.getId()));
		test.log(LogStatus.PASS, "Ok");
	}

}
