package com.example.fileUpload.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.fileUpload.model.FileModal;
import com.example.fileUpload.storage.StorageService;

@RestController
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);

	@Autowired
	private StorageService StorageService;
	
	@GetMapping("/getAllFiles")
	public ResponseEntity<List<FileModal>> getAllFiles() {
		List<FileModal> fileModal = StorageService.getAllFiles();
		return new ResponseEntity<List<FileModal>>(fileModal, HttpStatus.OK);
	}

	@PostMapping("/uploadFile")
	public ResponseEntity<FileModal> addFile(@RequestParam("file") MultipartFile file) {
		FileModal fileTable = StorageService.storeFile(file);
		return new ResponseEntity<FileModal>(fileTable, HttpStatus.OK);
	}

	@DeleteMapping("/fileDelete/{id}")
	public ResponseEntity<Void> deleteFile(@PathVariable("id") Long id) {
		StorageService.deleteFile(id);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// get uploaded file
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		System.out.println(ServletUriComponentsBuilder.fromCurrentContextPath().toString());
		// Load file as Resource
		Resource resource = StorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}

}