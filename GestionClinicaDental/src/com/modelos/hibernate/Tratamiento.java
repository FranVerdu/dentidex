package com.modelos.hibernate;
// Generated 5 may. 2021 19:19:57 by Hibernate Tools 5.4.27.Final
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 

import com.conexion.ConexionBBDD;

/**
 * Tratamiento generated by hbm2java
 */
@SuppressWarnings("serial")
public class Tratamiento implements java.io.Serializable, IOperable, INombreUnico {

	private int id;
	private TipoTratamiento tipoTratamiento;
	private String nombre;
	private Double precio; 

	public Tratamiento() {
		this.id = 0;
		this.tipoTratamiento = new TipoTratamiento();
	}
	public Tratamiento(int id) {
		this.id = id;
	}

	public Tratamiento(int id, TipoTratamiento tipoTratamiento, String nombre, Double precio) {
		this.id = id;
		this.tipoTratamiento = tipoTratamiento;
		this.nombre = nombre;
		this.precio = precio;
	}
 

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public TipoTratamiento getTipoTratamiento() {
		return this.tipoTratamiento;
	}

	public void setTipoTratamiento(TipoTratamiento tipoTratamiento) {
		this.tipoTratamiento = tipoTratamiento;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Double getPrecio() {
		return this.precio;
	}

	public void setPrecio(Double precio) {
		this.precio = precio;
	}
 

	@Override
	public String toString() {
		return getNombre();
	}

	@Override
	public boolean cargar() {
		try {
			ResultSet rs = ConexionBBDD.getDatos("SELECT * FROM TratamientosVista WHERE Id_Tratamiento = " + this.getId());
			if (rs.next() == false) return false;
			else {   
				this.setNombre(rs.getString("Tratamiento"));
				this.setPrecio(rs.getDouble("Precio"));
				
				if(rs.getObject("Id_TipoTratamiento") != null) setTipoTratamiento(new TipoTratamiento(rs.getInt("Id_TipoTratamiento"), rs.getString("TipoTratamiento")));  
			}
		} catch (SQLException e) { 
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	@Override
	public ArrayList<Tratamiento> getLista() {
		ArrayList<Tratamiento> lista = new ArrayList<Tratamiento>();
		try {
			ResultSet rs = ConexionBBDD.getDatos("SELECT * FROM TratamientosVista");
			while(rs.next()) {
				Tratamiento p = new Tratamiento();
				p.setId(rs.getInt("Id_Tratamiento"));
				p.setNombre(rs.getString("Tratamiento"));
				p.setPrecio(rs.getDouble("Precio")); 
				if(rs.getObject("Id_TipoTratamiento") != null) p.setTipoTratamiento(new TipoTratamiento(rs.getInt("Id_TipoTratamiento"), rs.getString("TipoTratamiento")));
				 
				lista.add(p); 
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		} 
		return lista;
	}

	@Override
	public boolean guardar() {
		String sql = "";
		
		if(this.getId() == 0) {
			sql = "INSERT INTO Tratamientos (Nombre, Precio, Id_TipoTratamiento)"
					+ "	VALUES ('" + this.getNombre().trim() + "'," +
									 this.getPrecio().toString() + "," +
									 (this.getTipoTratamiento().getId() > 0 ? String.valueOf(this.getTipoTratamiento().getId()) : "null") 	
						+ ")"; 
		}else {
			sql = "UPDATE Tratamientos "
					+ " SET Nombre = '" + this.getNombre().trim() + "', "
						+ "	Precio = " + this.getPrecio().toString() + ", "
						+ " Id_TipoTratamiento = " + (this.getTipoTratamiento().getId() > 0 ? String.valueOf(this.getTipoTratamiento().getId()) : "null") 
				+ " WHERE Id = " + this.getId();			
		} 	
		return ConexionBBDD.setDatos(sql); 
	}

	@Override
	public boolean esEliminable() {
		//Comprobamos si se ha realizado algun registro con esta entidad.
		if(Integer.parseInt(ConexionBBDD.getDato(
				"SELECT count(*) Existe " +
				 " FROM Tratamientos T " +  
				 " INNER JOIN Odontogramas O on O.Id_Tratamiento = T.ID" + 
				 " WHERE T.ID = " + this.getId()).toString()) > 0) return false;  
		return true;
	}

	@Override
	public boolean borrar() {
		String sql = 
				 "DELETE "
				+ "	FROM Tratamientos" 
				+ "	WHERE ID = " + this.getId();
		return ConexionBBDD.setDatos(sql); 
	}

	@Override
	public boolean existeConMismoNombre(String nombre) {
		if(Integer.parseInt(ConexionBBDD.getDato("SELECT count(*) Existe FROM TratamientosVista Where Tratamiento = '" + nombre.trim() + "' and Id_Tratamiento <>" + this.getId()).toString()) > 0) 
			return true;
			 
		return false;
	}

}