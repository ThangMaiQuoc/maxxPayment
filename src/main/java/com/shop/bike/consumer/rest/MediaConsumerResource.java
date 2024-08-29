package com.shop.bike.consumer.rest;

import com.shop.bike.constant.ApplicationConstant;
import com.shop.bike.consumer.service.UserConsumerService;
import com.shop.bike.web.rest.errors.BadRequestAlertException;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/consumer")
@Slf4j
public class MediaConsumerResource {

	private static final String UPLOADED_FOLDER = "src/main/java/com/shop/bike/consumer/upload/";


	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	@Qualifier(ApplicationConstant.CONSUMER)
	private UserConsumerService userConsumerService;

	@PostMapping("/upload")
	public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
		if (file.isEmpty()) {
			throw new BadRequestAlertException("Please select a file!", "", "");
		}

		Map<String, String> filepath = new HashMap<>();

		try {
			// Kiểm tra loại tệp
			Tika tika = new Tika();
			String mimeType = tika.detect(file.getBytes());
			if (!mimeType.startsWith("image/")) {
				throw new BadRequestAlertException("Invalid file type. Please upload an image.", "", "");
			}

			// Tạo thư mục nếu chưa tồn tại
			String projectDir = System.getProperty("user.dir");
			Path uploadPath = Paths.get(projectDir).resolve(UPLOADED_FOLDER);
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			byte[] bytes = file.getBytes();
			String filePath = Instant.now().toString() + "_" + file.getOriginalFilename();
			Path path = uploadPath.resolve(filePath);
			Files.write(path, bytes);

			filepath.put("path", UPLOADED_FOLDER + filePath);
		} catch (IOException e) {
			log.error("Error writing file", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return ResponseEntity.ok(filepath);
	}

	@GetMapping("/public/logo/{filename}")
	public ResponseEntity<Resource> getImage(@PathVariable String filename, HttpServletRequest request) {
		try {
			// Tạo đường dẫn tới tệp tin
			String projectDir = System.getProperty("user.dir");
			Path filePath = Paths.get(projectDir).resolve(UPLOADED_FOLDER).resolve(filename);

			// Kiểm tra tệp tin có tồn tại không
			Resource resource = new UrlResource(filePath.toUri());
			if (!resource.exists() || !resource.isReadable()) {
				throw new BadRequestAlertException("Image not found", "", "");
			}

			// Xác định kiểu dữ liệu (content type)
			String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
			if (contentType == null) {
				contentType = "application/octet-stream";
			}

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					.body(resource);
		} catch (MalformedURLException e) {
			log.error("Error reading file", e);
			throw new BadRequestAlertException("Error reading file", "", "");
		} catch (IOException e) {
			log.error("Error reading file", e);
			throw new BadRequestAlertException("Error reading file", "", "");
		}
	}

}