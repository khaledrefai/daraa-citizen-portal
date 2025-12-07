package gov.daraa.citizen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceCategoryTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ServiceCategory getServiceCategorySample1() {
        return new ServiceCategory().id(1L).code("code1").name("name1").description("description1");
    }

    public static ServiceCategory getServiceCategorySample2() {
        return new ServiceCategory().id(2L).code("code2").name("name2").description("description2");
    }

    public static ServiceCategory getServiceCategoryRandomSampleGenerator() {
        return new ServiceCategory()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
