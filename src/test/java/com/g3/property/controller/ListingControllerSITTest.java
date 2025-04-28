package com.g3.property.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.g3.property.entity.Agent;
import com.g3.property.entity.Listing;
import com.g3.property.repository.ListingRepository;
import com.g3.property.repository.AgentRepository;


@SpringBootTest
@AutoConfigureMockMvc
public class ListingControllerSITTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // convertion of Java object to JSON

    @Autowired
    private ListingRepository listingRepository;
    
    @Autowired
    private AgentRepository agentRepository;

    private Agent testAgent;
    private Listing testListing;

    @BeforeEach
    public void setup() {
        
        // Clear previous test data
        listingRepository.deleteAll();
        agentRepository.deleteAll();
        
        // Create a test agent
        testAgent = Agent.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
        testAgent = agentRepository.save(testAgent);
        
        // Create test listings linked to the agent
        Listing listing = Listing.builder()
                .town("Tampines")
                .street_name("Tampines Street 45")
                .price(1200000L)
                .agent(testAgent) // Link to agent
                .build();
        
        testListing = listingRepository.save(listing);
   
    }

    @DisplayName("Listing creation SIT test")
    @Test
    public void validListingTest() throws Exception {

        // Create a Listing object
        Listing savedListing = Listing.builder()
                                    .town("Yishun")
                                    .street_name("Yishun Ring Road 10")
                                    .price(1000000L)
                                    .build();


        String newListingAsJSON = objectMapper.writeValueAsString(savedListing);

    
        // Build the request
        //listing/agent/{agentId}
        RequestBuilder request = MockMvcRequestBuilders.post("/listing/agent/" + testAgent.getId())
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

    @Test
    @DisplayName("Get all listings for a specific agent")
    public void getListingsByAgentId() throws Exception {
        // Perform GET request to endpoint that lists agent's listings
        mockMvc.perform(MockMvcRequestBuilders.get("/listing/agent/" + testAgent.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].town").exists());
    }
    

}
