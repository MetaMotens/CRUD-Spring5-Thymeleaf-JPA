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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name="facturas")//mapear a la tabla facturas
public class Factura implements Serializable{

	@Id//indica que es primarykey
	@GeneratedValue(strategy=GenerationType.IDENTITY)//autoincremental
	private Long id;
	
	@NotEmpty
	private String descripcion;
	private String observacion;
	
	@Temporal(TemporalType.DATE)//fecha
	@Column(name="create_at")
	private Date createAt;
	
	@ManyToOne(fetch=FetchType.LAZY)//indica muchas facturas a un cliente, relacion n.1 ------ el LAZY es carga perezosa, carga el cliente cuando se le invoca
	private Cliente cliente;
	
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)//una factura muchos items. relacion 1.n
	@JoinColumn(name="factura_id")//como no tenemos una relacion en ambos sentidos, tenemos que indicar la foreing key
	private List<ItemFactura> items;
	
	//inicializa items
	public Factura() {
		this.items = new ArrayList<ItemFactura>();	
	}

	//Se encarga de crear la fecha
	@PrePersist
	public void prePersist()
	{
		createAt = new Date();
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public List<ItemFactura> getItems() {
		return items;
	}
	public void setItems(List<ItemFactura> items) {
		this.items = items;
	}
	
	//añade item
	public void addItemFactura(ItemFactura item) {
		this.items.add(item);
	}
	
	public Double getTotal() {
		Double total = 0.0;
		int size = items.size();//cantidad de items
		
		for (int i = 0; i < size; i++) {
			total += items.get(i).calcularImporte();
		}
		
		return total;
	}


	private static final long serialVersionUID = 1L;
	
}
