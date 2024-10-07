package modelo;

public class Viento {

	//Atributos
	private String direccion;
	private String velocidad;
	
	//Constructor
	public Viento(String direccion, String velocidad) {
		this.direccion = direccion;
		this.velocidad = velocidad;
		
	}
	
	//Metodos

	public String getDireccion() {
		return direccion;
	}

	public String getVelocidad() {
		return velocidad;
	}
	
	
}
