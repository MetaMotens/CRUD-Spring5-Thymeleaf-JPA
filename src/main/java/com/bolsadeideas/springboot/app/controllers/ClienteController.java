package com.bolsadeideas.springboot.app.controllers;

//import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.util.Map;
//import java.util.UUID;
//import java.util.logging.Logger;

import javax.validation.Valid;

//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.web.ServerProperties.Tomcat.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@Controller
@SessionAttributes("cliente") // en vez de usar hidden para capturar el id y trabajar con el, creamos una
								// sesion y trabajamos directamente con el objeto cliente. la sesion se
								// eliminara cuando el proceso este completado.
public class ClienteController {
	@Autowired
	// @Qualifier("clienteDaoJPA")//seleccionar el bean en concreto
	// private IClienteDao clienteDao;
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;

	// private final org.slf4j.Logger log =
	// LoggerFactory.getLogger(getClass());//podemos mostrar en la consola y hacer
	// un debug

	// private final static String UPLOAD_FOLDER = "uploads";

	@GetMapping(value = "/uploads/{filename:.+}") // la expresion regular .+ lo que hace es eliminar la extension y asi
													// solo nos quedamos con el nombre
	public ResponseEntity<UrlResource> verFoto(@PathVariable String filename) {
		UrlResource recurso = null;

		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

	@GetMapping(value = "/ver/{id}")
	public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		//Cliente cliente = clienteService.findOne(id);
		Cliente cliente = clienteService.fetchByIdWithFacturas(id);//De esta manera solo hace una consulta en vez de muchas
		if (cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}

		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());

		return "ver";
	}

	@RequestMapping(value = "/listar", method = RequestMethod.GET) // tipo de peticion, por defecto es GET pero bueno...
	public String listar(Model model)// clase model para pasar datos a la vista
	{
		model.addAttribute("titulo", "Listado de clientes");
		model.addAttribute("clientes", clienteService.findAll());
		return "listar";// devolvemos la clase
	}

	@RequestMapping(value = "/form")
	public String crear(Map<String, Object> model)// primera fase mostrar el formulario al usuario
	{
		Cliente cliente = new Cliente();// crea objeto cliente
		model.put("cliente", cliente);
		model.put("titulo", "Formulario de Cliente");
		return "form";
	}

	@RequestMapping(value = "/form/{id}") // editamos a traves del id
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		Cliente cliente = null;

		if (id > 0) {
			cliente = clienteService.findOne(id);
			if (cliente == null)// comprueba si el cliente existe en la base de datos.
			{
				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD!");
				return "redirect:/listar";
			}
		} else {
			flash.addFlashAttribute("success", "El ID del cliente no puede ser cero!");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar Cliente");

		return "form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST) // segunda fase recibe el objeto cliente y se guarda
																	// en clienteDao, despues redireccionamos
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {
		if (result.hasErrors())// si contiene algun error, vuelve al formulario
		{
			model.addAttribute("titulo", "Formulario de Cliente");// añadimos aqui el titulo porque si hay algun fallo,
																	// el titulo desaparece. de esta manera si.
			return "form";
		}

		// comprobaciones de la foto
		if (!foto.isEmpty())// editar
		{
			if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
					&& cliente.getFoto().length() > 0)// comprbar si existe la foto (rizando el rizo para mi gusto)
			{
				uploadFileService.delete(cliente.getFoto());

				/*
				 * Path rootPath =
				 * Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath(); File
				 * archivo = rootPath.toFile();
				 * 
				 * //validar que la foto existe y es "readable" if (archivo.exists() &&
				 * archivo.canRead()) { archivo.delete(); }
				 */
			}

			String uniqueFilename = null;

			try {
				uniqueFilename = uploadFileService.copy(foto);
			} catch (IOException e) {
				e.printStackTrace();
			}

			flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");

			cliente.setFoto(uniqueFilename);

		}

		String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con exito!" : "Cliente creado con exito!";

		clienteService.save(cliente);// guarda el objeto cliente
		status.setComplete();// si se completa, se elimina la sesion.
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listar";// redirige
	}

	@RequestMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
		if (id > 0) {
			Cliente cliente = clienteService.findOne(id);

			clienteService.delete(id);
			flash.addFlashAttribute("success", "Cliente eliminado con exito!");

			/*
			 * Path rootPath =
			 * Paths.get(UPLOAD_FOLDER).resolve(cliente.getFoto()).toAbsolutePath(); File
			 * archivo = rootPath.toFile();
			 * 
			 * //validar que la foto existe y es "readable" if (archivo.exists() &&
			 * archivo.canRead()) {
			 */

			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto: " + cliente.getFoto() + " eliminada con exito");
			}

		}
		return "redirect:/listar";
	}

}