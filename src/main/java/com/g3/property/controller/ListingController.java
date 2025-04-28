package com.g3.property.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.g3.property.entity.Listing;
import com.g3.property.service.ListingService;

import jakarta.validation.Valid;

import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/listing")
public class ListingController {

    private ListingService listingService;

    public ListingController( ListingService listingService){
        this.listingService = listingService; 
    }

    // agent path for agent use only 
    @PostMapping(value = "/agent/{agentId}",
                consumes = MediaType.APPLICATION_JSON_VALUE, 
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Listing> createListing(@Valid @RequestBody Listing listing, @PathVariable Long agentId ) {
        return new ResponseEntity<>(listingService.createListing(listing, agentId), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Listing>> searchListings(
            // provide default value for searching
            @RequestParam(required = false) String town,
            @RequestParam(required = false, defaultValue = "0") Double minPrice,
            @RequestParam(required = false, defaultValue = "1000000") Double maxPrice) {   
        
        List<Listing> listings = listingService.searchListings(town, minPrice, maxPrice);
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }

    @GetMapping("/agent/{id}")
    public ResponseEntity<List<Listing>> getAgentListing(@PathVariable Long id) {
        return new ResponseEntity<>(listingService.getAgentListing(id), HttpStatus.OK);
    }

    @PutMapping("/agent/{agentId}/{listingId}")
    public ResponseEntity<Listing> updateListing(
        @PathVariable Long agentId, 
        @PathVariable Long listingId,
        @RequestBody Listing listing) {
        return new ResponseEntity<>(listingService.updateListing(listingId, listing, agentId), HttpStatus.OK);
    }

    @DeleteMapping("/agent/{agentId}/{listingId}")
    public ResponseEntity<HttpStatus> deleteListing(
        @PathVariable Long agentId, 
        @PathVariable Long listingId) {
        listingService.deleteListing( listingId, agentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
}
