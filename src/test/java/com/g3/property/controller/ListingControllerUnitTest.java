package com.g3.property.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g3.property.entity.Listing;
import com.g3.property.repository.ListingRepository;
import com.g3.property.service.ListingService;
import com.g3.property.repository.AgentRepository;


//@SpringBootTest
@WebMvcTest(controllers = ListingController.class)
@AutoConfigureMockMvc
public class ListingControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // convertion of Java object to JSON

    @MockitoBean
	private ListingRepository listingRepository;

    @MockitoBean
    private ListingService listingService;

    @MockitoBean
    private AgentRepository agentRepository;

    @DisplayName("Listing creation Controller layer Unit test")
    @Test
    public void validListingTest() throws Exception {

        // Create a Listing object
        Listing savedListing = Listing.builder()
                                    .town("Yishun")
                                    .street_name("Yishun Ring Road 10")
                                    .price(1000000L)
                                    .build();

        // mock listing service by inserting the new listing by agent and agentID and return the newly created listing
        // savedListing is mocked as the newly created listing                             
        when(listingService.createListing(any(Listing.class), eq(1L))).thenReturn(savedListing);

        //  the input for the request from the client (controller layer) will be in Json 
        String newListingAsJSON = objectMapper.writeValueAsString(savedListing);

    
        // Build the request
        //listing/agent/{agentId}
        RequestBuilder request = MockMvcRequestBuilders.post("/listing/agent/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(newListingAsJSON);
    
        // Perform the request, get response and assert
        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.town").value("Yishun"))
                .andExpect(jsonPath("$.street_name").value("Yishun Ring Road 10"))
                .andExpect(jsonPath("$.price").value(1000000L));
    }
}
