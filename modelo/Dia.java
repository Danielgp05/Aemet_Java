package modelo;

public class Dia {

	//Atributos
	private String fecha;
	private String descripcion_estadoCielo;
	private Temperatura temperatura;
	private Viento rachaViento;
	private String probLluvia;
	private String uv;
	
	//Constructor
	public Dia(String fecha, String descripcion_estadoCielo, Temperatura temperatura, Viento rachaViento, String probLluvia, String uv) {
		this.fecha = fecha;
		this.descripcion_estadoCielo = descripcion_estadoCielo;
		this.temperatura = temperatura;
		this.rachaViento = rachaViento;
		this.probLluvia = probLluvia;
		this.uv = uv;
		
	}

	
	public String getUv() {
		return uv;
	}


	public String getProbLluvia() {
		return probLluvia;
	}

	public String getFecha() {
		return fecha;
	}

	public String getDescripcion_estadoCielo() {
		return descripcion_estadoCielo;
	}

	public Temperatura getTemperatura() {
		return temperatura;
	}

	public Viento getRachaViento() {
		return rachaViento;
	}
	
	
	
	
}
