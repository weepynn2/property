package com.g3.property.service;

import org.springframework.stereotype.Service;
import com.g3.property.entity.Listing;
import com.g3.property.entity.Agent;
import com.g3.property.exception.AgentNotFoundException;
import com.g3.property.exception.ListingNotFoundException;
import com.g3.property.exception.UnauthorizedAccessException;
import com.g3.property.repository.ListingRepository;
import com.g3.property.repository.AgentRepository;

import java.util.ArrayList;
import java.util.List;
 
// create the listing CRUD here and assign the requestMapping API
// on the controller level

@Service
public class ListingServiceImpl implements ListingService {

    private ListingRepository listingRepository;
    private AgentRepository agentRepository;

    public ListingServiceImpl(ListingRepository listingRepository, AgentRepository agentRepository) {
        this.listingRepository = listingRepository;
        this.agentRepository = agentRepository;
    }

    @Override
    public Listing createListing(Listing listing, Long agentId) {

        // make sure that the agent exists
        Agent agent = agentRepository.findById(agentId)
                                    .orElseThrow(() -> new AgentNotFoundException(agentId));
                                    
        listing.setAgent(agent); // set foreign key

        return listingRepository.save(listing);
    }

    @Override
    public List<Listing> searchListings(String town, Double minPrice, Double maxPrice) {

        //Declare empty listing
        List<Listing> foundListings = new ArrayList<>();

        // Check if town is not specified
        if (town == null || town.trim().isEmpty()) {
            foundListings = listingRepository.findByPriceBetween(minPrice, maxPrice);
        } else {
        // Otherwise, search by town and price range
            foundListings = listingRepository.findByTownAndPriceBetween(town, minPrice, maxPrice);
        }
        
        // .orElseThrow(() only works on List Optional
        if (foundListings.isEmpty()) {
            throw new ListingNotFoundException("No listings found in town: " + town + " with price range: " + minPrice + " - " + maxPrice);
        }
                                                
        return foundListings;
    }

    @Override
    public List<Listing> getAgentListing(Long agentId){
        List<Listing> listingOfAgent = listingRepository.findByAgentId(agentId);

        // .orElseThrow(() only works on List Optional
        if (listingOfAgent.isEmpty()) {
            throw new ListingNotFoundException("No listings found for agent with id " + (agentId));
        }
        return listingOfAgent;
    }

    @Override
    public Listing updateListing(Long listingId, Listing listing, Long agentId) {
        
        // find whether listing exists 
        Listing listingToUpdate = listingRepository.findById(listingId).orElseThrow(() -> new ListingNotFoundException("Could not find agent with id: " + (agentId)));
        
        // Check if the listing's agent id matches the provided agent id
        if (!listingToUpdate.getAgent().getId().equals(agentId)) {
            throw new UnauthorizedAccessException(agentId);
        }

        // Update listing object
        listingToUpdate.setTown(listing.getTown());
        listingToUpdate.setStreet_name(listing.getStreet_name());
        listingToUpdate.setPrice(listing.getPrice());
   
        // Save the updated listing object to the DB
        Listing savedListing = listingRepository.save(listingToUpdate);
        return savedListing;
    }

    @Override
    public void deleteListing(Long listingId, Long agentId) {
        
        // find whether listing exists 
        Listing listingToDelete = listingRepository.findById(listingId).orElseThrow(() -> new ListingNotFoundException("Could not find listing with id: " + (listingId)));
        
        // Check if the listing's agent id matches the provided agent id
        if (!listingToDelete.getAgent().getId().equals(agentId)) {
            throw new UnauthorizedAccessException(agentId);
        }

        // delete listing
        listingRepository.deleteById(listingId);
    }


}
