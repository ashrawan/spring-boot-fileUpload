package com.example.fileUpload.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.fileUpload.model.FileModal;

@Transactional
@Repository
public class FileStorageDAOImpl implements FileStorageDAO {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public FileModal getFileById(long fileId) {
		return entityManager.find(FileModal.class, fileId);
	}

	@Override
	public FileModal getFileByName(String fileName) {
		FileModal fileModal = entityManager
				.createQuery("SELECT f from FileModal f WHERE f.fileName = :fileName", FileModal.class)
				.setParameter("fileName", fileName).getSingleResult();

		return fileModal;
	}

	@Override
	public List<FileModal> getAllFiles() {
		String HQL = "FROM FileModal as f ORDER BY f.createDateTime DESC";
		return (List<FileModal>) entityManager.createQuery(HQL).getResultList();
	}

	@Override
	public FileModal addFile(FileModal fileModal) {

		entityManager.persist(fileModal);
		return fileModal;

	}

	@Override
	public void deleteFile(long fileId) {

		FileModal fileModal = getFileById(fileId);
		String fileName = fileModal.getFileName();

		entityManager.remove(fileModal);

	}

}
