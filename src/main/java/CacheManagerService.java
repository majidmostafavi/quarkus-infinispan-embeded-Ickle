import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.infinispan.manager.EmbeddedCacheManager;

@ApplicationScoped
public class CacheManagerService {
    @Inject
    EmbeddedCacheManager cacheManager;

    public void createCache() {
        var cache = cacheManager.getCache("book");

        cache.put("1", "John");
    }
}
