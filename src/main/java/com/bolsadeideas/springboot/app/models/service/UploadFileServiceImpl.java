package com.bolsadeideas.springboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadFileServiceImpl implements IUploadFileService {

	private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());//podemos mostrar en la consola y hacer un debug
	
	private final static String UPLOAD_FOLDER = "uploads";
	
	public UrlResource load(String filename) throws MalformedURLException {
		
		Path pathFoto = getPath(filename);
		log.info("PathFoto: " + pathFoto);
		
		UrlResource recurso = null;

			recurso = new UrlResource(pathFoto.toUri());
			if (!recurso.exists() || !recurso.isReadable())
			{
				throw new RuntimeException("Error: no se puede cargar la imagen: " + pathFoto.toString());
			}

		return recurso;
	}

	public String copy(MultipartFile file) throws IOException {
		
		String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();//se encarga de darle un id unico y concatenarlo con el nombre de la imagen.
		Path rootPath = getPath(uniqueFilename);
		//Path rootAbsolutPath = rootPath.toAbsolutePath();//aqui tenemnos la ruta completa
		
		//log.info("rootPath: " + rootPath);//mostramos por consola rootpath y despues el absolutrootpath
		log.info("rootAbsolutPath: " + rootPath);
		

		/*byte[] bytes = foto.getBytes();
		Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
		Files.write(rutaCompleta, bytes);*/
			
		Files.copy(file.getInputStream(), rootPath);//manera simplificada para copiar el archivo.

		
		return uniqueFilename;
	}

	public boolean delete(String filename) {

		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();
		
		//validar que la foto existe y es "readable"
		if (archivo.exists() && archivo.canRead())
		{
			if(archivo.delete())
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Path getPath(String filename)
	{
		return Paths.get(UPLOAD_FOLDER).resolve(filename).toAbsolutePath();
		
	}

	public void deleteAll() {
		
		FileSystemUtils.deleteRecursively(Paths.get(UPLOAD_FOLDER).toFile());//borra todo
	}

	public void init() throws IOException {
		
		Files.createDirectory(Paths.get(UPLOAD_FOLDER));//crea el directorio
	}

}
