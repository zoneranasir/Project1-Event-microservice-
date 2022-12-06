package edu.msudenver.event;

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
@WebMvcTest({EventController.class})
public class EventControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventRepository eventRepository;
    @MockBean
    private EntityManagerFactory entityManagerFactory;
    @MockBean
    private EntityManager entityManager;
    @SpyBean
    private EventService eventService;

    public EventControllerTest() {
    }

    @BeforeEach
    public void setup() {
        eventService.entityManager = entityManager;
    }

    @Test
    public void testGetEvents() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/", new Object[0]).accept(new MediaType[]{MediaType.APPLICATION_JSON}).contentType(MediaType.APPLICATION_JSON);
        Event testEvent = new Event();
        testEvent.setTitle("Fight Club");
        Mockito.when(this.eventRepository.findAll()).thenReturn(Arrays.asList(testEvent));
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains("Fight Club"));
    }

    @Test
    public void testGetEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/events/1", new Object[0]).accept(new MediaType[]{MediaType.APPLICATION_JSON}).contentType(MediaType.APPLICATION_JSON);
        Event testEvent = new Event();
        testEvent.setTitle("Fight Club");
        Mockito.when(this.eventRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(testEvent));
        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().contains("Fight Club"));
    }

    @Test
    public void testGetEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/events/5", new Object[0])
                .accept(new MediaType[]{MediaType.APPLICATION_JSON})
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(this.eventRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        Assertions.assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testCreateEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"tesT\",\"starts\":\"2022-10-26T04:06:24.344Z\",\"ends\":\"2022-10-26T04:06:35.890Z\",\"venueId\":1}")
                .contentType(MediaType.APPLICATION_JSON);

        Event event = new Event();
        event.setTitle("my new event");

        Mockito.when(eventRepository.saveAndFlush(Mockito.any())).thenReturn(event);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("my new event"));
    }

    @Test
    public void testCreateEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/events/")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Test1\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"countryCode\":\"ca\", \"countryName\": \"Canada Updated\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event event = new Event();
        event.setTitle("my new event");

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(event));

        Event event2 = new Event();
        event2.setTitle("my new updated event");

        Mockito.when(eventRepository.saveAndFlush(Mockito.any())).thenReturn(event2);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("my new updated event"));

    }

    @Test
    public void testUpdateEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/33")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"some title\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testUpdateEventBadRequest() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"new title\"}")
                .contentType(MediaType.APPLICATION_JSON);

        Event event2 = new Event();
        event2.setTitle("my new updated event");

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(event2));

        Mockito.when(eventRepository.saveAndFlush(Mockito.any())).thenThrow(IllegalArgumentException.class);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }

    @Test
    public void testDeleteEvent() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);


        Event event = new Event();
        event.setTitle("my new event");

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.of(event));
        Mockito.when(eventRepository.existsById(Mockito.any())).thenReturn(true);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
    }

    @Test
    public void testDeleteEventNotFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/events/55")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        Mockito.when(eventRepository.findById(Mockito.any())).thenReturn(Optional.empty());
        Mockito.when(eventRepository.existsById(Mockito.any())).thenReturn(false);
        Mockito.doThrow(IllegalArgumentException.class)
                .when(eventRepository)
                .deleteById(Mockito.any());

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().isEmpty());
    }


}
