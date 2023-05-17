package io.wisoft.capstonedesign.global.enumeration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static io.wisoft.capstonedesign.global.enumeration.HospitalDept.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class HospitalDeptTest {

    @ParameterizedTest
    @EnumSource(names = {"DENTAL", "OPHTHALMOLOGY", "DERMATOLOGY"})
    public void transformDept(final HospitalDept hospitalDept) throws Exception {

        switch (hospitalDept) {
            case DENTAL -> Assertions.assertEquals(DENTAL, hospitalDept);
            case OPHTHALMOLOGY -> Assertions.assertEquals(OPHTHALMOLOGY, hospitalDept);
            case DERMATOLOGY -> Assertions.assertEquals(DERMATOLOGY, hospitalDept);
            default -> Assertions.fail("Unexpected enum value: " + hospitalDept);
        }
    }
}