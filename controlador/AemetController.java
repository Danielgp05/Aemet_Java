package controlador;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import org.controlsfx.control.textfield.TextFields;

import modelo.SpriteAnimation;
import excepciones.MunicipioNoExisteException;
import javafx.animation.Animation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import modelo.ComunidadAutonoma;
import modelo.Dia;
import modelo.Municipio;
import modelo.Provincia;


public class AemetController implements Initializable {

	@FXML private DiaController dia_1Controller;
	@FXML private DiaController dia_2Controller;
	@FXML private DiaController dia_3Controller;
	@FXML private DiaController dia_4Controller;
	@FXML private DiaController dia_5Controller;
	@FXML private DiaController dia_6Controller;
	@FXML private DiaController dia_7Controller;

	@FXML private ComboBox<String> comboBox_comunidad;
	@FXML private ComboBox<String> comboBox_provincia;
	@FXML private ComboBox<String> comboBox_municipio;
	@FXML private Button boton_Guardar;
	@FXML private TextField textField_municipio;

	@FXML private AnchorPane dia_1;
	@FXML private AnchorPane dia_2;
	@FXML private AnchorPane dia_3;
	@FXML private AnchorPane dia_4;
	@FXML private AnchorPane dia_5;
	@FXML private AnchorPane dia_6;
	@FXML private AnchorPane dia_7;

	@FXML private ImageView imagen_Sprite;
	@FXML private Slider velocidad;

	private final Image IMAGE = new Image(getClass().getResourceAsStream("/vista/img/mega-man-running-sprite-animation-e4chd0cenxatez5b-e4chd0cenxatez5b.png"));

	private static final int COLUMNS  =   5;
	private static final int COUNT    =  10;
	private static final int OFFSET_X =  -20;
	private static final int OFFSET_Y =  -5;
	private static final int WIDTH    = 390;
	private static final int HEIGHT   = 410;

	private SpriteAnimation animation;

	ComunidadAutonoma ca;
	Provincia p;
	Municipio m;

	@FXML
	void pulsarTecla(KeyEvent event) {
		switch (event.getCode()) {

		case ENTER: case FINAL: m.setCodigoSeleccionado(obtenerCodigo(textField_municipio.getText())); comboBox_comunidad.setDisable(false); 
		comboBox_municipio.setDisable(false); comboBox_provincia.setDisable(false); textField_municipio.setDisable(false); break;

		default: break;
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		ca = new ComunidadAutonoma();
		p = new Provincia();
		m = new Municipio();

		//Activar Sprite
		activarSprite();
		velocidad.valueProperty().addListener((observable, oldValue, newValue) -> asignarVelocidad(newValue.doubleValue()));

		//Ocultar los paneles
		ocultarYVaciaPaneles();

		comboBox_comunidad.setPromptText("Introduce la comunidad");
		comboBox_provincia.setPromptText("Introduce la provincia");
		comboBox_municipio.setPromptText("Introduce el municipio");

		//Llenar los comboBox y filtrar
		comboBox_comunidad.setOnMouseClicked((event) ->{
			textField_municipio.setDisable(true);

			textField_municipio.setText("");
			rellenarComboBox(ca.getMapaComunidades(), comboBox_comunidad);	
			ocultarYVaciaPaneles();

			comboBox_provincia.setPromptText("Introduce la provincia");
			comboBox_municipio.setPromptText("Introduce el municipio");

			comboBox_provincia.getSelectionModel().clearSelection();
			comboBox_municipio.getSelectionModel().clearSelection();

		});

		comboBox_provincia.setOnMouseClicked((event) ->{

			try {
				if(!comboBox_comunidad.getSelectionModel().selectedItemProperty().getValue().equals("Introduce la comunidad")
						|| !comboBox_comunidad.getSelectionModel().selectedItemProperty().getValue().equals("")){

					rellenarComboBox(filtrarDatos(obtenerClaveSeleccionada(comboBox_comunidad, ca.getMapaComunidades()), p.getMapaProvincias()), comboBox_provincia);
					comboBox_municipio.getSelectionModel().clearSelection();
				
				}

			}catch (NullPointerException e) {
				mostrarDialogo(" la provincia");
				textField_municipio.setDisable(false);
				textField_municipio.setText("");

			}

		});

		comboBox_municipio.setOnMouseClicked((event) ->{

			try {
				if(!comboBox_provincia.getSelectionModel().selectedItemProperty().getValue().equals("Introduce la provincia")
						|| !comboBox_provincia.getSelectionModel().selectedItemProperty().getValue().equals("")){

					rellenarComboBox(filtrarDatos(obtenerClaveSeleccionada(comboBox_provincia, p.getMapaProvincias()), m.getMapaMunicipios()), comboBox_municipio);

				}

			}catch (NullPointerException e) {
				mostrarDialogo(" el municipio");
				comboBox_provincia.getSelectionModel().clearSelection();
				textField_municipio.setDisable(false);
				textField_municipio.setText("");

			}

		});		

		//Guardar el codigo seleccionada para descargar el fichero
		boton_Guardar.setOnMouseClicked((event) ->{

			//Guardar codigo y habilitar los combobox
			try {
				m.setCodigoSeleccionado(obtenerCodigo(comboBox_municipio.getValue().toString()));

			}catch (NullPointerException e) {
				m.setCodigoSeleccionado(obtenerCodigo(textField_municipio.getText()));

			}

			//Si el codigo es valido
			if(!m.getCodigoSeleccionado().equals("")) {
				//Descargar XML
				descargar_crear_XML();

				//Mostrar los datos del tiempo al usuario
				refrescarYMostrar(m.getDias());

			}

			comboBox_comunidad.setDisable(false); 
			comboBox_municipio.setDisable(false); 
			comboBox_provincia.setDisable(false);

			textField_municipio.setDisable(false);

		});

		//Rellenar textField
		Set<String> claves = m.getMapaMunicipios().keySet();
		TextFields.bindAutoCompletion(textField_municipio, claves);  	

		//Deshabilitar y reiniciar combobox si vas a escribir
		textField_municipio.setOnMouseClicked((event) ->{
			ocultarYVaciaPaneles();
			textField_municipio.setText("");
			comboBox_comunidad.setDisable(true);
			comboBox_municipio.setDisable(true);
			comboBox_provincia.setDisable(true);

		});

	}

	ComboBox<String> rellenarComboBox(TreeMap<String, String> a, ComboBox<String> b) {

		b.getItems().clear();
		
		Set<String> claves = a.keySet();
		b.getItems().addAll(claves);

		return b;
	}

	public String obtenerClaveSeleccionada(ComboBox<String> b, TreeMap<String, String> a) {

		String claveSeleccionada =  b.getSelectionModel().selectedItemProperty().getValue();
		String clave;
		String valor;
		String valorSeleccionado = "";

		try {

			//Recorrer el mapa
			Set set = a.entrySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry mentry = (Map.Entry)iterator.next();

				clave = mentry.getKey()+"";
				valor = mentry.getValue()+"";

				if(claveSeleccionada.equals(clave)) {
					valorSeleccionado = valor;
					break;

				}

			}


		}catch (NullPointerException e) {
			System.out.println("Seleccione Primero La Otra Casilla");		

		}

		return valorSeleccionado;		

	}

	public TreeMap<String, String> filtrarDatos(String valor, TreeMap<String, String> siguiente){

		TreeMap<String, String> filtrado = new TreeMap<String, String>();
		int longitud = valor.length();
		String claveMapa;
		String valorMapa;
		String valorActual;

		//Recorrer el mapa
		Set set = siguiente.entrySet();
		Iterator iterator = set.iterator();
		
		while(iterator.hasNext()) {
			Map.Entry mentry = (Map.Entry)iterator.next();

			claveMapa = mentry.getKey()+"";
			valorMapa = mentry.getValue()+"";

			valorActual = valorMapa.substring(0, longitud);

			if(valorActual.equals(valor)) {
				filtrado.put(claveMapa, valorMapa);

			}

		}		

		return filtrado;
	}

	public String obtenerCodigo(String nombre) {
		try {
			return m.obtenerCodigo(nombre);

		} catch (MunicipioNoExisteException e) {
			mostrarDialogo("Este municipio no existe");
			return "";

		}

	}

	public void descargar_crear_XML() {
		m.descargar_crear_XML();

	}

	public void refrescarYMostrar(ArrayList<Dia> dias) {

		dia_1Controller.refrescarInformacion(dias.get(0));
		dia_2Controller.refrescarInformacion(dias.get(1));
		dia_3Controller.refrescarInformacion(dias.get(2));
		dia_4Controller.refrescarInformacion(dias.get(3));
		dia_5Controller.refrescarInformacion(dias.get(4));
		dia_6Controller.refrescarInformacion(dias.get(5));
		dia_7Controller.refrescarInformacion(dias.get(6));

		dia_1.setVisible(true);
		dia_2.setVisible(true);
		dia_3.setVisible(true);
		dia_4.setVisible(true);
		dia_5.setVisible(true);
		dia_6.setVisible(true);
		dia_7.setVisible(true);

	}

	public void ocultarYVaciaPaneles() {
		dia_1.setVisible(false);
		dia_2.setVisible(false);
		dia_3.setVisible(false);
		dia_4.setVisible(false);
		dia_5.setVisible(false);
		dia_6.setVisible(false);
		dia_7.setVisible(false);

	}

	public void mostrarDialogo(String a) {
		Alert alert = new Alert(AlertType.ERROR);

		// Get the Stage.
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();

		// Add a custom icon.
		stage.getIcons().add(new Image(this.getClass().getResource("/vista/img/error.png").toString()));

		alert.setTitle("Error");
		alert.setHeaderText(null);

		switch (a) {
		case " la provincia": alert.setContentText("Introduce antes la comunidad"); break;
		case " el municipio": alert.setContentText("Introduce antes la comunidad y la provincia");break;
		case "Este municipio no existe": alert.setContentText("Este municipio no existe, elija otro"); textField_municipio.setText(""); break;
		default: alert.setContentText("Error desconocido");	break;
		}

		alert.showAndWait();

	}

	private void activarSprite() {

		imagen_Sprite.setImage(IMAGE);
		imagen_Sprite.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));

		animation = new SpriteAnimation(
				imagen_Sprite,
				Duration.millis(1000),
				COUNT, COLUMNS,
				OFFSET_X, OFFSET_Y,
				WIDTH, HEIGHT
				);

		animation.setCycleCount(Animation.INDEFINITE);
		animation.play();
	}

	private void asignarVelocidad(double velocidadActualSlider) {
		double duracion = calcularVelocidad(velocidadActualSlider);		
		animation.setRate(duracion);

	}

	private double calcularVelocidad(double velocidadActualSlider) {
		/*
		 * Un rate de 1.0 significa que la animación se ejecuta a su velocidad normal.
		 * Un rate de 0.5 significa que la animación se ejecuta a la mitad de su velocidad normal.
		 * Un rate de 2.0 significa que la animación se ejecuta al doble de su velocidad normal.
		 */

		// velocidad mas lenta
		double min = 0.5; 

		// velocidad mas rapida
		double max = 2.0; 

		return min + (velocidadActualSlider * (max - min) / 100);
	}

}
