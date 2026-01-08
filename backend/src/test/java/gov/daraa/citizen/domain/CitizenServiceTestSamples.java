package gov.daraa.citizen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CitizenServiceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CitizenService getCitizenServiceSample1() {
        return new CitizenService()
            .id(1L)
            .name("name1")
            .estimatedDuration(1)
            .feesDescription("feesDescription1")
            .serviceLink("https://service-link-1.example.com");
    }

    public static CitizenService getCitizenServiceSample2() {
        return new CitizenService()
            .id(2L)
            .name("name2")
            .estimatedDuration(2)
            .feesDescription("feesDescription2")
            .serviceLink("https://service-link-2.example.com");
    }

    public static CitizenService getCitizenServiceRandomSampleGenerator() {
        return new CitizenService()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .estimatedDuration(intCount.incrementAndGet())
            .feesDescription(UUID.randomUUID().toString())
            .serviceLink("https://service-link-" + UUID.randomUUID() + ".example.com");
    }
}
