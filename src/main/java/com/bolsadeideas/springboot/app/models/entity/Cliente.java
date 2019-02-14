package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
//import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name="clientes")//cuando runeamos, crea la tabla clientes gracias al h2 que coge el import.sql y crea la tabla automaticamente, tiene que llamarse import.sql
public class Cliente implements Serializable{
	
	private static final long serialVersionUID = 1L;


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty//obliga a que no sea vacio
	private String nombre;
	
	@NotEmpty
	private String apellido;
	
	@NotEmpty
	@Email//tipo
	private String email;
	
	@NotNull//que no sea nulo
	@Column(name="create_at")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern="dd-MM-yyyy")//le añades un patron al formato, el solo te lo convierte.
	private Date createAt;
	
	@OneToMany(mappedBy="cliente", fetch=FetchType.LAZY, cascade=CascadeType.ALL)//un cliente muchas facturas, 1.n ------ el LAZY es carga perezosa, carga el cliente cuando se le invoca. con MappedBy hacemos que sea bidireccional IMPORTANTISIMO
	private List<Factura> facturas;//un cliente puede tener muchas facturas por tanto se crea un list
	
	//inicializar facturas
	public Cliente() {
		facturas = new ArrayList<Factura>();
	}

	private String foto;
	
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public List<Factura> getFacturas() {
		return facturas;
	}
	
	//aqui guarda una lista de facturas
	public void setFacturas(List<Factura> facturas) {
		this.facturas = facturas;
	}
	
	//aqui se guarda factura una por una
	public void addFactura(Factura factura)
	{
		facturas.add(factura);
	}
	@Override
	public String toString() {
		return nombre + " " + apellido;
	}
	
	
}
