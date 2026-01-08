package gov.daraa.citizen.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ServiceFormTemplateTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ServiceFormTemplate getServiceFormTemplateSample1() {
        return new ServiceFormTemplate().id(1L).name("name1").description("description1").orderIndex(1);
    }

    public static ServiceFormTemplate getServiceFormTemplateSample2() {
        return new ServiceFormTemplate().id(2L).name("name2").description("description2").orderIndex(2);
    }

    public static ServiceFormTemplate getServiceFormTemplateRandomSampleGenerator() {
        return new ServiceFormTemplate()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .orderIndex(intCount.incrementAndGet());
    }
}
