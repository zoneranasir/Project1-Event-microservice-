package edu.msudenver.venue;

import org.junit.jupiter.api.Assertions;
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
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({SpringExtension.class})
@WebMvcTest({VenueController.class})
public class VenueControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private VenueRepository venueRepository;
    @MockBean
    private EntityManagerFactory entityManagerFactory;
    @MockBean
    private EntityManager entityManager;
    @SpyBean
    private VenueService venueService;

    public VenueControllerTest() {
    }

    @BeforeEach
    public void setup() {
        venueService.entityManager = entityManager;
    }

    @Test
    public void testGetVenues() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/", new Object[0])
                .accept(new MediaType[]{MediaType.APPLICATION_JSON})
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setType("public");
        testVenue.setName("cool venue");
        testVenue.setStreetAddress("1234 xyz street");
        testVenue.setVenueId(1);
        testVenue.setActive(true);

        Mockito.when(this.venueRepository.findAll()).thenReturn(Arrays.asList(testVenue));

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains("cool venue"));
    }

    @Test
    public void testGetVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/1", new Object[0])
                .accept(new MediaType[]{MediaType.APPLICATION_JSON})
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setType("public");
        testVenue.setName("cool venue");
        testVenue.setStreetAddress("1234 xyz street");
        testVenue.setVenueId(1);
        testVenue.setActive(true);

        Mockito.when(this.venueRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(testVenue));

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains("cool venue"));
    }

    @Test
    public void testGetVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/venues/5", new Object[0])
                .accept(new MediaType[]{MediaType.APPLICATION_JSON})
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(this.venueRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"cool venue\",\"active\":true,\"type\":\"public\",\"streetAddress\":\"1234 xyz street\",\"venueId\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setType("public");
        testVenue.setName("cool venue");
        testVenue.setStreetAddress("1234 xyz street");
        testVenue.setVenueId(1);
        testVenue.setActive(true);

        Mockito.when(venueRepository.saveAndFlush(Mockito.any())).thenReturn(testVenue);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("cool venue"));
    }

    @Test
    public void testCreateVenueBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/venues/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Test1\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"some venue\",\"active\":true,\"type\":\"public\",\"streetAddress\":\"1234 xyz street\",\"venueId\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue testVenue = new Venue();
        testVenue.setType("public");
        testVenue.setName("cool venue");
        testVenue.setStreetAddress("1234 xyz street");
        testVenue.setVenueId(1);
        testVenue.setActive(true);

        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(testVenue));

        Venue testVenue2 = new Venue();
        testVenue2.setType("public");
        testVenue2.setName("cool venue updated");
        testVenue2.setStreetAddress("1234 xyz street");
        testVenue2.setVenueId(1);
        testVenue2.setActive(true);

        Mockito.when(venueRepository.saveAndFlush(Mockito.any())).thenReturn(testVenue2);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("cool venue updated"));

    }

    @Test
    public void testUpdateVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/33")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"some other venue name\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateVenueBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/venues/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"some other venue name\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Venue venue = new Venue();
        venue.setType("public");
        venue.setName("cool venue updated");
        venue.setStreetAddress("1234 xyz street");
        venue.setVenueId(1);
        venue.setActive(true);


        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(venue));

        Mockito.when(venueRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteVenue() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/venues/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        Venue venue = new Venue();
        venue.setType("public");
        venue.setName("cool venue updated");
        venue.setStreetAddress("1234 xyz street");
        venue.setVenueId(1);
        venue.setActive(true);


        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.of(venue));
        Mockito.when(venueRepository.existsById(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteVenueNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/venues/55")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(venueRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(venueRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(venueRepository)
                .deleteById(Mockito.any());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }


}
