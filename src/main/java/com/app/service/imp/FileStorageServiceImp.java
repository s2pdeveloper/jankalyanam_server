package com.app.service.imp;

import java.net.MalformedURLException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.FileSystemUtils;

import com.app.exception.ServerError;
import com.app.service.FilesStorageService;
import com.google.protobuf.GeneratedMessageV3.Builder;
import com.google.protobuf.compiler.PluginProtos.CodeGeneratorResponse.File;



@Service
public class FileStorageServiceImp implements FilesStorageService{
	
	
	private static final MultipartFile MultipartFile = null;
	private final Path root = Paths.get("uploads");


	
//	private Path folderName1 ;
//	  @Override
//	  public void init() {
//	    try {
//	      Files.createDirectory(root);
//	    } catch (IOException e) {
//	      throw new RuntimeException("Could not initialize folder for upload!");
//	    }
//	  }
	  
	public void createDirectory(Path folderName) throws IOException {
		if (!Files.isDirectory(root)) {
			Files.createDirectory(root);
			Files.createDirectory(folderName) ;
		}
		else if (Files.isDirectory(root) && !Files.isDirectory(folderName)){
			Files.createDirectory(folderName) ;
		}
		
	}

	  @Override
	  public void save(MultipartFile file,String fileName,Path folderName) {
	    try {
		    createDirectory(folderName) ;	
	      Files.copy(file.getInputStream(), folderName.resolve(fileName));
	    } catch (Exception e) {
	      throw new ServerError("Could not store the file. Error: " + e.getMessage());
	    }
	  }
	  
	  
		@Override
		public void update(MultipartFile file , String updatedFileName , String existingFileName , Path folderName) {
			    delete(existingFileName , folderName);
			     save(file , updatedFileName , folderName) ;
			  
			
		}

	  @Override
	  public Resource load(String filename , Path folderName) {
	    try {
	      Path file = folderName.resolve(filename);
	      Resource resource = new UrlResource(file.toUri());

	      if (resource.exists() || resource.isReadable()) {
	        return resource;
	      } else {
	        throw new RuntimeException("Could not read the file!");
	      }
	    } catch (MalformedURLException e) {
	      throw new RuntimeException("Error: " + e.getMessage());
	    }
	  }

	  @Override
	  public void deleteAll(Path folderName) {
	    FileSystemUtils.deleteRecursively(folderName.toFile());
	  }

	  
	  
	  @Override
	  public boolean delete(String filename , Path folderName) {
	    try {
	      Path file = folderName.resolve(filename);
	      return Files.deleteIfExists(file);
	    } catch (IOException e) {
	      throw new RuntimeException("Could not delete the file. Error: " + e.getMessage());
	    }
	  }
	  
	  
	  

	  
	  @Override
	  public Stream<Path> loadAll(Path folderName) {
	    try {
	      return Files.walk(folderName, 1).filter(path -> !path.equals(folderName)).map(folderName::relativize);
	    } catch (IOException e) {
	      throw new RuntimeException("Could not load the files!");
	    }
	  }



}
