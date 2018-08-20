package com.example.fileUpload.dao;

import java.util.List;

import com.example.fileUpload.model.FileModal;

public interface FileStorageDAO {
	
	public abstract List<FileModal> getAllFiles();
	
	public abstract FileModal getFileByName(String fileName);
	
	public abstract FileModal getFileById(long fileId);
	
	public abstract FileModal addFile(FileModal File);
	
	public abstract void deleteFile(long fileId);


}
