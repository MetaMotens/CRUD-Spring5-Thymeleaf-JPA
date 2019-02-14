package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;

//import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>
{
	@Query("select c from Cliente c left join fetch c.facturas f where c.id=?1")
	public Cliente fetchByIdWithFacturas(Long id);
	/*CON EL CRUDREPOSITORY NO HACE FALTA NADA DE ESTO YA QUE LO TIENE TODO, no hace falta hacer NADA... se le puede añadir consultas con @query
	
	public List<Cliente> findAll();//devolver todos
	
	public void save (Cliente cliente);
	
	public Cliente findOne(Long id);//devuelve solo uno
	
	public void delete(Long id);*/
}
