package gov.daraa.citizen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DirectorateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Directorate getDirectorateSample1() {
        return new Directorate().id(1L).code("code1").name("name1").description("description1");
    }

    public static Directorate getDirectorateSample2() {
        return new Directorate().id(2L).code("code2").name("name2").description("description2");
    }

    public static Directorate getDirectorateRandomSampleGenerator() {
        return new Directorate()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
