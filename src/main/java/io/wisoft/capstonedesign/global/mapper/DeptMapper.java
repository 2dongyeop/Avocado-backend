package io.wisoft.capstonedesign.global.mapper;


import io.wisoft.capstonedesign.global.enumeration.HospitalDept;

public class DeptMapper {

    public static HospitalDept numberToDept(final String number) {

        final HospitalDept value =
                switch (number) {
                    case "1" -> {
                        yield HospitalDept.DENTAL;
                    }
                    case "2" -> {
                        yield HospitalDept.OPHTHALMOLOGY;
                    }
                    case "3" -> {
                        yield HospitalDept.DERMATOLOGY;
                    }
                    case "4" -> {
                        yield HospitalDept.PLASTIC_SURGERY;
                    }
                    case "5" -> {
                        yield HospitalDept.OBSTETRICS;
                    }
                    case "6" -> {
                        yield HospitalDept.PSYCHIATRY;
                    }
                    case "7" -> {
                        yield HospitalDept.ORTHOPEDICS;
                    }
                    case "8" -> {
                        yield HospitalDept.NEUROSURGERY;
                    }
                    case "9" -> {
                        yield HospitalDept.SURGICAL;
                    }
                    case "10" -> {
                        yield HospitalDept.NEUROLOGY;
                    }
                    case "11" -> {
                        yield HospitalDept.PEDIATRIC;
                    }
                    case "12" -> {
                        yield HospitalDept.INTERNAL_MEDICINE;
                    }
                    case "13" -> {
                        yield HospitalDept.OTOLARYNGOLOGY;
                    }
                    case "14" -> {
                        yield HospitalDept.UROLOGY;
                    }
                    case "15" -> {
                        yield HospitalDept.ORIENTAL_MEDICAL;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + number);
                };

        return value;
    }
}
