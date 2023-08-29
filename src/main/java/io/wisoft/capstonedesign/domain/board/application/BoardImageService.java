package io.wisoft.capstonedesign.domain.board.application;

import io.wisoft.capstonedesign.domain.board.persistence.BoardImage;
import io.wisoft.capstonedesign.domain.board.persistence.BoardImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Transactional
    public List<String> save(final Long boardId, final MultipartFile... multipartFiles) {

        if (ArrayUtils.isEmpty(multipartFiles)) {
            log.info("Failed to store empty file.");
            return List.of();
        }

        final List<String> savedUrlList = new ArrayList<>();

        for (MultipartFile file : multipartFiles) {

            // 실제 파일 이름 IE나 Edge는 전체 경로가 들어옴
            final String originalName = file.getOriginalFilename();
            final String fileName = originalName.substring(originalName.lastIndexOf("\\") + 1);

            log.info("fileName: {}", fileName);

            // 날짜별 폴더 생성
            final String folderPath = makeFolder();

            // 저장 될 이름 생성
            final String saveName = createSaveName(folderPath, fileName);

            // 저장 될 경로 생성
            final Path savePath = Paths.get(saveName);

            try {
                file.transferTo(savePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            savedUrlList.add(savePath.toString());
        }

        savedUrlList.forEach(savedUrl -> boardImageRepository.save(new BoardImage(savedUrl, boardId)));
        return savedUrlList;
    }

    private String createSaveName(final String folderPath, final String fileName) {

        // UUID 생성
        final String uuid = UUID.randomUUID().toString();

        // 파일 저장시 이름 중간에 "_"를 이용해 구분
        return uploadPath + File.separator + folderPath + File.separator + uuid + "_" + fileName;
    }

    /**
     * 파일을 저장할 폴더를 생성하는 메서드 (디렉토리를 년월일에 맞춰 생성)
     *
     * @return : 생성된 폴더 경로
     */
    private String makeFolder() {

        final String str = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

        final String folderPath = str.replace("//", File.separator);

        // make folder
        final File uploadPathFolder = new File(uploadPath, folderPath);

        if (!uploadPathFolder.exists()) {
            uploadPathFolder.mkdirs();
        }

        return folderPath;
    }
}
