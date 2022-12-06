package edu.msudenver.city;

import edu.msudenver.country.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = CityController.class)
class CityControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean // a mock for subbing
    private CityRepository cityRepository;
    @MockBean
    private EntityManagerFactory entityManagerFactory;
    @MockBean
    private EntityManager entityManager;
    @SpyBean
    private CityService cityService;
    private final Country testCountry = new Country().setCountryName("United States").setCountryCode("us");
    private final Country testCountry2 = new Country().setCountryName("Mexico").setCountryCode("mx");
    private final City testCity = new City("80226", testCountry, testCountry.getCountryCode(), "Aurora");
    private final City testCity2 = new City("9", testCountry2, testCountry2.getCountryCode(), "Mexico City");
    private final City dumbtown = new City("1", testCountry2, testCountry2.getCountryCode(), "dumbtown");
    @BeforeEach // sets the mock beans for the tests
    public void setup() {
        cityService.entityManager = entityManager;
    }

    @Test
    void getCitiesTest() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        /*
         *  'stubbing' ex: Mockito. then this kinda stuff
         *  when(mock.isOk()).thenReturn(true);
         *  when(mock.isOk()).thenThrow(exception);
         *  doThrow(exception).when(mock).someVoidMethod();
         */
        Mockito.when(cityRepository.findAll()).thenReturn(asList(testCity));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains(testCity.getName()));
    }
    @Test
    void getCityTest() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/cities/")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        Mockito.when(cityRepository.findAll()).thenReturn(asList(testCity));
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("United States"));
    }
    @Test
    void createCitiesTest() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/cities")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"mx\"}/{\"cityName\": \"dumbtown\"}/{\"postalCode\":\"1\"}")
                .contentType(MediaType.APPLICATION_JSON);
        Mockito.when(cityRepository.saveAndFlush(Mockito.any())).thenReturn(dumbtown);
        Mockito.when(cityRepository.save(Mockito.any())).thenReturn(dumbtown);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("dumbtown"));
    }
    @Test
    void updateCityTest() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/cities/mx/9")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"mx\", \"cityName\": \"testCity2 Updated\"}/{\"postalCode\":\"9\"}")
                .contentType(MediaType.APPLICATION_JSON);
        //"/{countryCode}/{postalCode}"
        //"{\"countryCode\":\"mx\", \"cityName\": \"testCity2 Updated\"}/{\"postalCode\":\"9\"}"
        //"/{countryCode}"
        //"{\"countryCode\":\"ca\", \"countryName\": \"Canada Updated\"}"
        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(testCity2));
        City updatedTestCity2 = new City();
        updatedTestCity2.setCountry(testCountry2);
        updatedTestCity2.setPostalCode("9");
        //mexicoUpdated.setCountryName("Canada Updated");
        updatedTestCity2.setName("Mexico ruins");
        //canadaUpdated.setCountryCode("ca");
        Mockito.when(cityRepository.save(Mockito.any())).thenReturn(updatedTestCity2);
        Mockito.when(cityRepository.saveAndFlush(Mockito.any())).thenReturn(updatedTestCity2);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Mexico ruins") &&
                !(response.getContentAsString().contains("Mexico City")));
    }
    @Test
    void deleteCityTest() throws Exception{
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/cities/us/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);
        Mockito.when(cityRepository.findById(Mockito.any())).thenReturn(Optional.of(dumbtown));
        Mockito.when(cityRepository.existsById(Mockito.any())).thenReturn(true);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
        assertTrue(!(response.getContentAsString().contains("dumbtown")));
    }
}