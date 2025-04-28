package com.g3.property.util;

import org.springframework.stereotype.Component;

import com.g3.property.entity.Agent;
import com.g3.property.entity.Listing;
import com.g3.property.repository.AgentRepository;
import com.g3.property.repository.ListingRepository;

import jakarta.annotation.PostConstruct;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DataLoader {

  private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  private ListingRepository listingRepository;
  private AgentRepository agentRepository;

  public DataLoader(ListingRepository listingRepository, AgentRepository agentRepository) {
    this.listingRepository = listingRepository;
    this.agentRepository = agentRepository;
  }

  @PostConstruct
  public void loadData() {
    // Clear the database first
    listingRepository.deleteAll();
    agentRepository.deleteAll();

    logger.info("loading data");
    // Load data
    listingRepository
        .save(Listing.builder().street_name("ANG MO KIO AVE 10").price(232000L).town("ANG MO KIO").build());
    

    agentRepository.saveAll(List.of(
      Agent.builder().firstName("John").lastName("Tan").email("johntan@mail.com").build(),
      Agent.builder().firstName("Alison").lastName("Lee").email("alisonlee@mail.com").build(),
      Agent.builder().firstName("Tony").lastName("Baloney").email("tonybaloney@mail.com").build()
      ));

  }
}
