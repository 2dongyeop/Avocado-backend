package io.wisoft.capstonedesign.global.enumeration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BusAreaTest {

    @ParameterizedTest
    @EnumSource(BusArea.class)
    public void transform(final BusArea busArea) {

        switch (busArea) {
            case DAEJEON -> Assertions.assertThat("DAEJEON").isEqualTo(busArea.name());
            case SEOUL -> Assertions.assertThat("SEOUL").isEqualTo(busArea.name());
            case BUSAN -> Assertions.assertThat("BUSAN").isEqualTo(busArea.name());
            case DAEGU -> Assertions.assertThat("DAEGU").isEqualTo(busArea.name());
            case JEJU -> Assertions.assertThat("JEJU").isEqualTo(busArea.name());
            default -> org.junit.jupiter.api.Assertions.fail("Unexpected enum value: " + busArea);
        }
    }
}