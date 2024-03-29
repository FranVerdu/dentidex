package com.modelos.hibernate;
// Generated 5 may. 2021 19:19:57 by Hibernate Tools 5.4.27.Final
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList; 

import com.conexion.ConexionBBDD; 

/**
 * Proveedor generated by hbm2java
 */
@SuppressWarnings("serial")
public class Proveedor implements java.io.Serializable, IOperable, ISujeto {

	private int id; 
	private Sujeto sujeto;
	private String paginaWeb;
	private String personaContacto;

	public Proveedor() {
		this.id = 0; 
		this.sujeto = new Sujeto(); 
	}
	
	public Proveedor(int id) {
		this.id = id;
	}

	public Proveedor(int id, Sujeto sujeto, String paginaWeb, String personaContacto) {
		this.id = id; 
		this.sujeto = sujeto;
		this.paginaWeb = paginaWeb;
		this.personaContacto = personaContacto;
	}
 

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
 

	public Sujeto getSujeto() {
		return this.sujeto;
	}

	public void setSujeto(Sujeto sujeto) {
		this.sujeto = sujeto;
	}
	public String getPaginaWeb() {
		return paginaWeb;
	}

	public void setPaginaWeb(String paginaWeb) {
		this.paginaWeb = paginaWeb;
	}

	public String getPersonaContacto() {
		return personaContacto;
	}

	public void setPersonaContacto(String personaContacto) {
		this.personaContacto = personaContacto;
	}
 
	@Override
	public String toString() {
		return getSujeto().toString();
	}
 
 
 

	@Override
	public boolean guardar() {
		//Guardamos o actualizamos el registro
		//Si el Id de la entidad es 0, significa que hay que guardar un registro nuevo 
		//Si el Id de la entidad no es 0, significa que debemos actualizar los datos del registro
		String sql = ""; 
		Direccion dir = getSujeto().getDireccion();
		if(this.getId() == 0) {
			sql = "Insert Into Direcciones (Direccion, Poblacion, CodigoPostal, Provincia, Pais)"
							+ " VALUES (" + (dir.getDireccion() == null? "null" : "'" + dir.getDireccion() + "'") + "," + 
							(dir.getPoblacion() == null? "null" : "'" + dir.getPoblacion() + "'") + "," + 
							(dir.getCodigoPostal() == null? "null" : "'" + dir.getCodigoPostal() + "'") +  "," + 
							(dir.getProvincia() == null? "null" : "'" + dir.getProvincia() + "'") +  "," + 
							(dir.getPais() == null? "null" : "'" + dir.getPais() + "'") + ");"; 
			sql += "	INSERT INTO Sujetos (Id_Direccion, FechaNacimiento, Nombre, Apellidos, Nif, Email, Telefono, Foto)"
							+ "	VALUES ((select Max(Id) FROM Direcciones), '" + 
											getSujeto().getFechaNacimiento().toString() + "', '" + 
											getSujeto().getNombre() + "', '" + 
											getSujeto().getApellidos() + "', '" +
											getSujeto().getNif() + "', '" + 
											getSujeto().getEmail() + "', '" + 
											getSujeto().getTelefono() + "'," + 
											(getSujeto().getFoto() == null? "null" : "'" + getSujeto().getFoto() + "'") + ");";
					
			sql += " INSERT INTO Proveedores(Id_Sujeto, PaginaWeb, PersonaContacto)"
						+ " VALUES ((Select max(id) From Sujetos), " + 
									(getPaginaWeb() == null? "null" : "'" + getPaginaWeb() + "'") + ", " + 
									(getPersonaContacto() == null? "null" : "'" + getPersonaContacto() + "'") + ");";
			
			  
		}else {
			sql = "  UPDATE Direcciones"
					+ " SET Direccion = " + (dir.getDireccion() == null? "null" : "'" + dir.getDireccion() + "'") + ","
						+ " Poblacion = " +  (dir.getPoblacion() == null? "null" : "'" + dir.getPoblacion() + "'") + ","
						+ " CodigoPostal = " + (dir.getCodigoPostal() == null? "null" : "'" + dir.getCodigoPostal() + "'") + ","
						+ " Provincia = " +  (dir.getProvincia() == null? "null" : "'" + dir.getProvincia() + "'") + ","
						+ "	Pais = " +  (dir.getPais() == null? "null" : "'" + dir.getPais() + "'") 
					+ " WHERE Id = " + getSujeto().getDireccion().getId() + ";"
					 
					+ "UPDATE Sujetos"
					+ "	SET FechaNacimiento = '" + getSujeto().getFechaNacimiento().toString() + "',"
						+ " Nombre = '" + getSujeto().getNombre() + "',"
						+ " Apellidos = '"+ getSujeto().getApellidos() + "',"
						+ " Nif = '" + getSujeto().getNif() + "',"
						+ " Email = '"+ getSujeto().getEmail() + "',"
						+ "	Telefono = '"+ getSujeto().getTelefono() + "',"
						+ "	Foto = " + (getSujeto().getFoto() == null? "null" : "'" + getSujeto().getFoto() + "'") 
					+ "	WHERE Id = " + getSujeto().getId() + ";"
					 
					+ "UPDATE Proveedores"
					+ "	SET PersonaContacto = " + (getPersonaContacto() == null? "null" : "'" + getPersonaContacto() + "'") + ","
					+ "     PaginaWeb = " + (getPaginaWeb() == null? "null" : "'" + getPaginaWeb() + "'")
					+ "	WHERE Id = " + getId() + ";";
					  
		} 
		return ConexionBBDD.setDatos(sql); 
	}

	@Override
	public boolean esEliminable() {
		//No tiene dependencias. Siempre podr� eliminarse
		return true;
	}

	@Override
	public boolean borrar() {
		String sql = 
				 "DELETE "
				+ "	FROM Sujetos"
				+ "		USING Proveedores"
				+ "	WHERE Proveedores.Id_Sujeto = Sujetos.ID"
				+ "		AND Proveedores.ID = " + this.getId();
		return ConexionBBDD.setDatos(sql); 
	}

	@Override
	public boolean cargar() {
		try {
			ResultSet rs = ConexionBBDD.getDatos("SELECT * FROM ProveedoresVista WHERE Id_Proveedor = " + this.getId());
			if (rs.next() == false) return false;
			else {   
				setPaginaWeb(rs.getString("PaginaWeb"));
				setPersonaContacto(rs.getString("PersonaContacto"));
				 
				Direccion direccion = new Direccion(rs.getInt("Id_Direccion"), rs.getString("Direccion"), rs.getString("Poblacion"), rs.getString("CodigoPostal"), rs.getString("Provincia"), rs.getString("Pais"));
				Sujeto suj = new Sujeto(rs.getInt("Id_Sujeto"), direccion, rs.getString("Nombre"), rs.getString("Apellidos"), rs.getString("Nif"));
				suj.setEmail(rs.getString("Email"));
				suj.setTelefono(rs.getString("Telefono"));
				suj.setFoto(rs.getString("Foto"));
				suj.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
				suj.setFechaNacimiento(rs.getTimestamp("FechaNacimiento"));
				setSujeto(suj);  
			}
		} catch (SQLException e) { 
			e.printStackTrace();
			return false;
		} 
		return true;
	}

	@Override
	public ArrayList<Proveedor> getLista() {
		ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
		try {
			ResultSet rs = ConexionBBDD.getDatos("SELECT * FROM ProveedoresVista");
			while(rs.next()) {
				Proveedor p = new Proveedor();
				p.setId(rs.getInt("Id_Proveedor"));
				p.setPaginaWeb(rs.getString("PaginaWeb"));
				p.setPersonaContacto(rs.getString("PersonaContacto"));
				
				Direccion direccion = new Direccion(rs.getInt("Id_Direccion"), rs.getString("Direccion"), rs.getString("Poblacion"), rs.getString("CodigoPostal"), rs.getString("Provincia"), rs.getString("Pais"));
				Sujeto suj = new Sujeto(rs.getInt("Id_Sujeto"), direccion, rs.getString("Nombre"), rs.getString("Apellidos"), rs.getString("Nif"));
				suj.setEmail(rs.getString("Email"));
				suj.setTelefono(rs.getString("Telefono"));
				suj.setFoto(rs.getString("Foto"));
				suj.setFechaCreacion(rs.getTimestamp("FechaCreacion"));
				suj.setFechaNacimiento(rs.getTimestamp("FechaNacimiento"));
				p.setSujeto(suj);  
				lista.add(p); 
			}
		} catch (SQLException e) { 
			e.printStackTrace();
		} 
		return lista;
	}

	@Override
	public boolean existeSujetoConNif(String nif) {
		if(Integer.parseInt(ConexionBBDD.getDato("SELECT count(*) Existe FROM ProveedoresVista Where Nif = '" + nif.trim() + "' and Id_Proveedor <>" + this.getId()).toString()) > 0) 
			return true;
			 
		return false;
	}

	
 
}
