package com.topjava.restaurant_voting.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheClearUtil {

    // All cache cleared every 24 hours
    @Caching(
            evict = {
                    @CacheEvict(value = "dateMealCache", allEntries = true),
                    @CacheEvict(value = "menu", allEntries = true),
                    @CacheEvict(value = "restaurantList", allEntries = true),
                    @CacheEvict(value = "restaurant", allEntries = true),
                    @CacheEvict(value = "votesList", allEntries = true),
                    @CacheEvict(value = "stats", allEntries = true)
            }
    )
    @Scheduled(initialDelay = 3620000, fixedDelay = 3600000)
    public void clearAllCache() {
        log.debug("Cleared all cache");
    }

}
