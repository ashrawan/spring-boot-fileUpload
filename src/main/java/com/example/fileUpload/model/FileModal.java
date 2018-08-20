package com.example.fileUpload.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class FileModal {
	
	public FileModal() {
		super();
		// TODO Auto-generated constructor stub
	}


	public FileModal(String fileName, long fileSize, String fileType, LocalDateTime createDateTime, int status) {
		super();
		this.fileName = fileName;
		this.fileSize = fileSize;
		this.fileType = fileType;
		this.createDateTime = createDateTime;
		this.status = status;
	}


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String fileName;
	
	private long fileSize;
	
	private String fileType;

	@Column
	@CreationTimestamp
	private LocalDateTime createDateTime;
	
	private int status;

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public long getFileSize() {
		return fileSize;
	}


	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}


	public String getFileType() {
		return fileType;
	}


	public void setFileType(String fileType) {
		this.fileType = fileType;
	}


	public LocalDateTime getCreateDateTime() {
		return createDateTime;
	}


	public void setCreateDateTime(LocalDateTime createDateTime) {
		this.createDateTime = createDateTime;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}

	
	
}
