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
        return new CitizenService().id(1L).code("code1").name("name1").estimatedDuration(1).feesDescription("feesDescription1");
    }

    public static CitizenService getCitizenServiceSample2() {
        return new CitizenService().id(2L).code("code2").name("name2").estimatedDuration(2).feesDescription("feesDescription2");
    }

    public static CitizenService getCitizenServiceRandomSampleGenerator() {
        return new CitizenService()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .estimatedDuration(intCount.incrementAndGet())
            .feesDescription(UUID.randomUUID().toString());
    }
}
