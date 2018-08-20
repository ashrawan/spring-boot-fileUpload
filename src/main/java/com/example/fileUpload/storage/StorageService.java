package com.example.fileUpload.storage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.fileUpload.dao.FileStorageDAO;
import com.example.fileUpload.model.FileModal;

@Service
public class StorageService {

	private Path defaultStorageLocation;
	private String uploadDir = "upload";

	@Autowired
	private FileStorageDAO dao;

	// creating file directory
	public StorageService() { 
//		StringBuilder sb = new StringBuilder();
//		sb.append(System.getProperty("catalina.base")).append("/").append(System.getProperty("webapps")); 
		
		this.defaultStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

		File dir = new File(this.defaultStorageLocation.toString());
		if (!dir.exists()) {
			System.out.println("Creating Directory for file Storage");
			try {
				Files.createDirectories(this.defaultStorageLocation);
			} catch (Exception ex) {
				throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
			}
		}
	}

	public List<FileModal> getAllFiles() {
		return dao.getAllFiles();
	}

	public FileModal getFileById(long id) {
		return dao.getFileById(id);
	}

	public FileModal getFileByName(String fileName) {
		return dao.getFileByName(fileName);
	}

	public FileModal addFile(FileModal File) {
		return dao.addFile(File);
	}

	public FileModal storeFile(MultipartFile file) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		fileName = System.currentTimeMillis() + "-" + fileName;

		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}

			FileModal fileModal = new FileModal(fileName, file.getSize(), file.getContentType(), LocalDateTime.now(),
					1);

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = this.defaultStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return addFile(fileModal);

			// return fileTable;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}

	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.defaultStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			System.out.println("path to file " + filePath);
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

	public void deleteFile(long id) {
		FileModal fileModal = getFileById(id);
		String fileName = fileModal.getFileName();

		Path targetLocation = null;
		targetLocation = this.defaultStorageLocation.resolve(fileName);

		System.out.println("target location " + targetLocation);
		try {
			dao.deleteFile(id);
			Files.delete(targetLocation);

		} catch (NoSuchFileException e) {
			System.out.println("No Such File Exists");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
