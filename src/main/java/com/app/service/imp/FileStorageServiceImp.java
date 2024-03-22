package com.app.service.imp;

import java.net.MalformedURLException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.FileSystemUtils;

import com.app.service.FilesStorageService;



@Service
public class FileStorageServiceImp implements FilesStorageService{
	
	
	private final Path root = Paths.get("uploads");
//
//	  @Override
//	  public void init() {
//	    try {
//	      Files.createDirectory(root);
//	    } catch (IOException e) {
//	      throw new RuntimeException("Could not initialize folder for upload!");
//	    }
//	  }
	  


	  @Override
	  public void save(MultipartFile file,String fileName) {
	    try {
	    	Files.createDirectory(root);
	    System.out.print("inside the save---------"+root);
	    	System.out.println(file.getInputStream());
	    	System.out.println(file.getOriginalFilename());
	      	System.out.println("Here");
	      Files.copy(file.getInputStream(), this.root.resolve(fileName));
	    } catch (Exception e) {
	      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
	    }
	  }

	  @Override
	  public Resource load(String filename) {
	    try {
	      Path file = root.resolve(filename);
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
	  public void deleteAll() {
	    FileSystemUtils.deleteRecursively(root.toFile());
	  }

	  
	  @Override
	  public Stream<Path> loadAll() {
	    try {
	      return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
	    } catch (IOException e) {
	      throw new RuntimeException("Could not load the files!");
	    }
	  }

}
