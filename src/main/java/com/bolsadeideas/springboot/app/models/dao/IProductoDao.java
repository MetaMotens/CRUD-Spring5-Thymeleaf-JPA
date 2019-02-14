package com.bolsadeideas.springboot.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long>{

	//MANERA 1
	@Query("select p from Producto p where p.nombre like %?1%")//consulta
	public List<Producto> findByNombre(String term);
	
	//MANERA 2
	public List<Producto> findByNombreLikeIgnoreCase(String term);//es un metodo de spring que automaticamente ignora las mayusculas
	
}
