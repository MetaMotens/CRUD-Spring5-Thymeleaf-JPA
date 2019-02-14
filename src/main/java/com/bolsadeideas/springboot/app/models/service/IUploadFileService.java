package com.bolsadeideas.springboot.app.models.service;

import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService
{
	public UrlResource load(String filename) throws MalformedURLException;
	
	public String copy(MultipartFile file) throws IOException;
	
	public boolean delete(String filename);
	
	public void deleteAll();
	
	public void init() throws IOException;
}
