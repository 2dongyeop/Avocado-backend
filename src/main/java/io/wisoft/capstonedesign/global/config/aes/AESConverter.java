package io.wisoft.capstonedesign.global.config.aes;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class AESConverter implements AttributeConverter<String, String> {


    /**
     * Entity를 데이터베이스에 저장 시, 필드 값을 AES128로 암호화
     *
     * @param attribute the entity attribute value to be converted
     * @return : 암호화되어 데이터베이스에 저장된 값
     */
    @Override
    public String convertToDatabaseColumn(final String attribute) {
        return AES.encryptString(attribute);
    }

    /**
     * 데이터베이스에서 Entity를 조회 시, 필드 값을 AES128로 복호화
     *
     * @param dbData the data from the database column to be converted
     * @return : AES128로 복호화된 엔티티의 필드 값
     */
    @Override
    public String convertToEntityAttribute(final String dbData) {
        return AES.decryptString(dbData);
    }
}
