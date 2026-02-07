package com.ecommerce.orders.client;

import com.ecommerce.orders.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public UserDTO getUserById(Long id) {
        UserDTO fallbackUser = new UserDTO();
        fallbackUser.setId(id);
        fallbackUser.setFirstName("Nepoznat");
        fallbackUser.setLastName("Korisnik");
        fallbackUser.setEmail("unavailable@example.com");
        fallbackUser.setAddress("Adresa nije dostupna");
        fallbackUser.setPhoneNumber("N/A");
        return fallbackUser;
    }

    @Override
    public Map<String, Boolean> userExists(Long id) {
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", false);
        return response;
    }
}
