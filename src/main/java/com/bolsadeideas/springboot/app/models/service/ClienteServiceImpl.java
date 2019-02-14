package com.bolsadeideas.springboot.app.models.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.dao.IFacturaDao;
import com.bolsadeideas.springboot.app.models.dao.IProductoDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.Producto;

@Service//fascade - fachada? interfaz de interfaces? desde aqui accedes directamente a las interfaces sin tener que acceder directamente a ellas.
		//la idea es trabajar con distintos Daos en la misma transaccion
public class ClienteServiceImpl implements IClienteService
{
	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Transactional(readOnly=true)//ya que es solo lectura
	public List<Cliente> findAll() {
		return (List<Cliente>) clienteDao.findAll();
	}

	@Transactional
	public void save(Cliente cliente) {
		clienteDao.save(cliente);
		
	}

	@Transactional(readOnly=true)//ya que es solo lectura
	public Cliente findOne(Long id) {
		return clienteDao.findById(id).orElse(null);
	}
	
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {
		
		return clienteDao.fetchByIdWithFacturas(id);
	}

	@Transactional
	public void delete(Long id) {
		clienteDao.deleteById(id);
	}

	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		
		return productoDao.findByNombreLikeIgnoreCase("%"+term+"%");
	}

	@Transactional
	public void saveFactura(Factura factura) {
		facturaDao.save(factura);
		
	}

	@Transactional
	public Producto findProductoById(Long id) {
		return productoDao.findById(id).orElse(null);//con el orElse evitamos que el producto sea del tipo opcional(evita null pointer)
	}

	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		return facturaDao.findById(id).orElse(null);
	}

	@Transactional
	public void deleteFactura(Long id) {
		facturaDao.deleteById(id);
		
	}

	@Transactional(readOnly = true)
	public Factura fetchFacturaByIdWithClienteWithItemFacturaWithProducto(Long id) {
		
		return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
	}

	
	
}
