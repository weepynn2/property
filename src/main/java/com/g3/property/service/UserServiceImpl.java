package com.g3.property.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.g3.property.entity.Listing;
import com.g3.property.entity.User;
import com.g3.property.exception.ListingNotFoundException;
import com.g3.property.exception.UserNotFoundException;
import com.g3.property.repository.ListingRepository;
import com.g3.property.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private ListingRepository listingRepository;


    public UserServiceImpl(UserRepository userRepository, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }


    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User updateUser(Long id, User user) {
        User userToUdpate = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userToUdpate.setEmail(user.getEmail());
        userToUdpate.setFirstName(user.getFirstName());
        userToUdpate.setPhoneNumber(user.getPhoneNumber());
        userToUdpate.setLastName(user.getLastName());
        return userRepository.save(userToUdpate);
    }

    @Override
    public void deleteUser(Long id) {
       userRepository.deleteById(id);
    }

    @Override
    public void updateUserFavouriteListing(Long userId, Long listingId) {
        Listing listingToAdd = listingRepository.findById(listingId).orElseThrow(() -> new ListingNotFoundException("listing not found"));
        User selectUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        
        selectUser.addFavourite(listingToAdd);
        userRepository.save(selectUser);
    }

    @Override
    public List<Listing> getUserFavouriteListing(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        return user.getFavouriteListings();
    }
    
}
