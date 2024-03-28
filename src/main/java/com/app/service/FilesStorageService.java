package com.app.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {

//	  public void init();

	  public void save(MultipartFile file, String fileName , Path folderName);

	  public Resource load(String filename , Path folderName);

	  public void deleteAll( Path folderName);

	  public Stream<Path> loadAll(Path folderName);

	public boolean delete(String filename , Path FolderName);
	
      public void update (MultipartFile file1, String filename , String UpdatedFileName , Path folderName) ;

}
