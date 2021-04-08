package com.qa.choonz.rest.controller;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.choonz.rest.dto.ArtistDTO;
import com.qa.choonz.rest.dto.UserDTO;
import com.qa.choonz.service.ArtistService;
import com.qa.choonz.service.UserService;
import com.qa.choonz.utils.mappers.GenreMapper;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = { "classpath:test-schema.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public class ArtistControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	ArtistService service;

	@Autowired
	UserService uService;

	@Autowired
	GenreMapper mapper;

	@Autowired
	ObjectMapper objectMapper;

	static ExtentReports report = new ExtentReports("Documentation/reports/Artist_Controller_Integration_Report.html",
			true);
	static ExtentTest test;

	ArtistDTO validArtistDTO = new ArtistDTO("test");
	private UserDTO user = new UserDTO("cowiejr", "password");
	private String key = "";
	ArrayList<ArtistDTO> validArtistDTOs = new ArrayList<ArtistDTO>();
	ArrayList<Long> emptyList = new ArrayList<Long>();

	@BeforeEach
	void init() {

		if (key.isBlank()) {
			try {
				uService.create(user);
				key = "1000:00000001:7f1d6351d49e0bb872d4642ecec60ee3";

			} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		validArtistDTO = service.create(validArtistDTO);
		validArtistDTOs.add(validArtistDTO);
	}

	@AfterAll
	static void Exit() {
		report.flush();
	}

	@Test
	public void createArtistTest() throws Exception {
		test = report.startTest("Create artist test");
		ArtistDTO artistToSave = new ArtistDTO("test2");
		ArtistDTO expectedArtist = new ArtistDTO(validArtistDTO.getId() + 1, "test2", emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.POST, "/artists/create");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		mockRequest.content(objectMapper.writeValueAsString(artistToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isCreated();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedArtist));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readArtistTest() throws Exception {
		test = report.startTest("Read artist test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET, "/artists/read");
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validArtistDTOs));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readArtistByIdTest() throws Exception {
		test = report.startTest("Read artist by id test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/artists/read/id/" + validArtistDTO.getId());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validArtistDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void readArtistByNameTest() throws Exception {
		test = report.startTest("Read artist by name test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.GET,
				"/artists/read/name/" + validArtistDTO.getName());
		mockRequest.accept(MediaType.APPLICATION_JSON);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isOk();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(validArtistDTO));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void updateArtistTest() throws Exception {
		test = report.startTest("Update artist test");
		ArtistDTO artistToSave = new ArtistDTO("testaa");
		ArtistDTO expectedArtist = new ArtistDTO(validArtistDTO.getId(), "testaa", emptyList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.PUT, "/artists/update/1");
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.content(objectMapper.writeValueAsString(artistToSave));
		mockRequest.accept(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isAccepted();
		ResultMatcher contentMatcher = MockMvcResultMatchers.content()
				.json(objectMapper.writeValueAsString(expectedArtist));
		mvc.perform(mockRequest).andExpect(statusMatcher).andExpect(contentMatcher);
		test.log(LogStatus.PASS, "Ok");
	}

	@Test
	public void deleteArtistTest() throws Exception {
		test = report.startTest("Delete artist test");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.request(HttpMethod.DELETE,
				"/artists/delete/" + validArtistDTO.getId());
		mockRequest.contentType(MediaType.APPLICATION_JSON);
		mockRequest.header("Key", key);
		ResultMatcher statusMatcher = MockMvcResultMatchers.status().isNoContent();
		mvc.perform(mockRequest).andExpect(statusMatcher);
		test.log(LogStatus.PASS, "Ok");
	}
}
