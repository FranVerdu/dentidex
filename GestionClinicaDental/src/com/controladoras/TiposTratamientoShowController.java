package com.controladoras;

import com.Main;
import com.modelos.hibernate.TipoTratamiento;
import com.util.Informes;
 
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.Label; 
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.GridPane;

public class TiposTratamientoShowController {
	Main main;
	@FXML Button btnNuevo;
	@FXML Button btnModificar;
	@FXML Button btnBorrar;
	@FXML Button btnGuardarCambios;
	@FXML Button btnCancelarCambios;
	@FXML Button btnListar;
	@FXML TextField txtBusqueda;
	@FXML TextField txtNombre; 
	@FXML Label lbId_Registro; 
	@FXML TableView<TipoTratamiento> dtDatos;
	@FXML TableColumn<TipoTratamiento, Integer> col_IdTipoTratamiento;
	@FXML TableColumn<TipoTratamiento, TipoTratamiento> col_Nombre;

    FilteredList<TipoTratamiento> filtro;
    TipoTratamiento tipoTratamientoActual;
	@FXML GridPane tvDatos;
    
	public void setMain(Main main) {
		this.main = main;		
	}


	@FXML
	private void initialize() { 
		//Establecemos el limite de caracteres de los textfields
		com.util.Eventos.setLimiteCaracteres(txtNombre, 30); 
		
		
		
		this.btnModificar.setDisable(true);
		this.btnBorrar.setDisable(true);
		this.btnGuardarCambios.setDisable(true);
		this.btnCancelarCambios.setDisable(true);  
		this.tvDatos.setDisable(true);
		 
		this.cargarListaDatos();	  
		
		  
		// Ininicializamos la tabla
		col_IdTipoTratamiento.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
		col_Nombre.setCellValueFactory(cellData -> new SimpleObjectProperty<TipoTratamiento>( cellData.getValue()));
		  
		 
        //Actualizamos los datos del jugador seleccionado
        dtDatos.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> { 
            if (nv != null) { 
            	this.tipoTratamientoActual = nv;
            	this.tvDatos.setDisable(true);
        		this.btnNuevo.setDisable(false);
        		this.btnBorrar.setDisable(false);
        		this.btnModificar.setDisable(false);
        		this.btnGuardarCambios.setDisable(true);
        		this.btnCancelarCambios.setDisable(true); 
        		
            	this.lbId_Registro.setText(String.valueOf(nv.getId()));
            	this.txtNombre.setText(nv.getNombre()); 
            } else { 
            	this.limpiarDatos();
            }
    	});
         
        //Configuramos el filtro de la tabla
        this.txtBusqueda.textProperty().addListener( (o, ov, nv) -> { 
	        	filtro.setPredicate(obj -> { 
	        		if (obj.getNombre().toLowerCase().contains(nv.toLowerCase()))
	        			return true;
	        		else return false; 
	        	}); 
        });  
	} 
	
	
	
	

	@FXML public void btnNuevo() {
		this.limpiarDatos();
		this.tipoTratamientoActual = new TipoTratamiento();
		this.btnNuevo.setDisable(true);
		this.btnGuardarCambios.setDisable(false);
		this.btnCancelarCambios.setDisable(false); 
		this.tvDatos.setDisable(false);
	}
 

	@FXML public void btnModificar() {
		this.btnNuevo.setDisable(true);
		this.btnModificar.setDisable(true);
		this.btnBorrar.setDisable(true);
		this.btnGuardarCambios.setDisable(false);
		this.btnCancelarCambios.setDisable(false); 
		this.tvDatos.setDisable(false);
	}

	@FXML public void btnGuardarCambios() {
		//Comprobamos que los datos son v�lidos.
		if(!isValido()) return; 
		
		//Mapeamos el objecto y lo guardamos en base de datos
		TipoTratamiento p = this.tipoTratamientoActual;  
		

		//Comprobamos que no haya ning�n medico m�s con el NIF indicado
		//Solo puede haber un �nico medico con el mismo NIF
		if(p.existeConMismoNombre(this.txtNombre.getText().trim())) {
			com.util.Alertas.alertaDatosErroneos(this.main.getStagePrincipal(), "Ya existe un Tipo de Tratamiento con ese Nombre."); 
			limpiarDatos();
			this.cargarListaDatos();
			return;
		}
		
		p.setNombre(this.txtNombre.getText());  
		
		//Guardamos los datos del paciente y recargamos los datos
		if(!p.guardar()) {
			com.util.Alertas.alertaDatosErroneos(this.main.getStagePrincipal(), "Ha ocurrido un error al guardar los cambios."); 
	        return;
		}; 
		limpiarDatos();
		this.cargarListaDatos();
		 
		//Avisamos al usuario del proceso
        if(p.getId() > 0) com.util.Alertas.alertaDatosCorrectos(this.main.getStagePrincipal(), "El tipo de tratamiento se ha modificado exitosamente.");  
        else com.util.Alertas.alertaDatosCorrectos(this.main.getStagePrincipal(), "El tipo de tratamiento se ha sido creado exitosamente.");
	}

	@FXML public void btnBorrar() {
		int indice = dtDatos.getSelectionModel().getSelectedIndex(); 
        if (indice >= 0) { 
            if(!com.util.Alertas.alertaConfirmacionUsuario(this.main.getStagePrincipal(), "Va eliminar el registro seleccionado, �continuar?").equals(ButtonType.OK)) 
            	return;	//Preguntamos al usuario si desea eliminar el registro seleccionado
            
            if(!dtDatos.getSelectionModel().getSelectedItem().esEliminable()) {
            	com.util.Alertas.alertaDatosErroneos(this.main.getStagePrincipal(), "El Tipo de Tratamiento no se puede eliminar, tiene registros asociados");
            	return;
            }
            
            dtDatos.getSelectionModel().getSelectedItem().borrar();
            this.cargarListaDatos();
        } else {
            com.util.Alertas.alertaNadaSeleccionado(this.main.getStagePrincipal(), "tipo de tratamiento"); 
        }
	}

	private void cargarListaDatos() {
		//A�adimos un filtro a la lista
        filtro = new FilteredList<TipoTratamiento>(FXCollections.observableArrayList(new TipoTratamiento().getLista()));
        dtDatos.setItems(filtro);  
	}

	@FXML public void btnCancelarCambios() {this.limpiarDatos();}

	@FXML public void btnListar() { Informes.mostrarListadoTiposDeTratamiento(main);}
	
	private void limpiarDatos() { 
		this.tipoTratamientoActual = null;
		this.dtDatos.getSelectionModel().clearSelection();
		this.btnNuevo.setDisable(false);
		this.btnBorrar.setDisable(true);
		this.btnModificar.setDisable(true);
		this.btnGuardarCambios.setDisable(true);
		this.btnCancelarCambios.setDisable(true); 
		this.tvDatos.setDisable(true);
		
		this.lbId_Registro.setText("000000"); 
		this.txtNombre.setText(null); 
	}

	private boolean isValido() { 
        String errorMessage = "";
  
	    //Datos de la persona
        if (txtNombre.getText() == null || txtNombre.getText().length() <3) 
        	errorMessage += "El campo Nombre debe tener 3 o m�s car�cteres.\n";  
        
        if (errorMessage.length() == 0) return true;
        else {
            // Mostramos el mensaje de error
            com.util.Alertas.alertaDatosInvalidos(this.main.getStagePrincipal(), errorMessage);
            return false;
        } 
	}
}
