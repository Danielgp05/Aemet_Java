package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import modelo.Dia;

public class DiaController {

	@FXML private ImageView imagen_Tiempo;
	@FXML private ImageView imagen_direccionViento;
	@FXML private Label label_Fecha;
	@FXML private Label temperatura_Max;
	@FXML private Label temperatura_Min;
	@FXML private Label label_estadoCielo;
	@FXML private Label label_probPrec;
	@FXML private Label label_UV;
	@FXML private Label label_direccionViento;
	@FXML private Label label_velocidadViento;

	//Metdos
	public void refrescarInformacion(Dia dia) {	
		String[] fecha = dia.getFecha().split("-");
		label_Fecha.setText(fecha[2] + "-" + fecha[1] + "-" + fecha[0]);
		
		temperatura_Max.setText("Temperatura MAX: " + dia.getTemperatura().getTempMax() + "ºC");
		temperatura_Min.setText("Temperatura MIN: " + dia.getTemperatura().getTempMin() + "ºC");
		label_estadoCielo.setText(dia.getDescripcion_estadoCielo());
		label_probPrec.setText(dia.getProbLluvia() + "%");
		label_UV.setText(dia.getUv() + "%");
		label_direccionViento.setText("Dirección: " + dia.getRachaViento().getDireccion());
		label_velocidadViento.setText("Velocidad: " + dia.getRachaViento().getVelocidad());

		Image viento = new Image(getClass().getResourceAsStream("/vista/viento/" + dia.getRachaViento().getDireccion() + ".png"));
		imagen_direccionViento.setImage(viento);

		Image estadoCielo = new Image(getClass().getResourceAsStream("/vista/estadosCielo/" + dia.getDescripcion_estadoCielo() + ".png"));
		imagen_Tiempo.setImage(estadoCielo);

	}

}
