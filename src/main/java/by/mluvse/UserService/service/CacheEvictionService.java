package by.mluvse.UserService.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheEvictionService {

    @CacheEvict(value = "users", allEntries = true)
    public void evictAllUsers() {

    }
}
