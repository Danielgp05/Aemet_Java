package modelo;

public class Temperatura {

	//Atributos
	private String tempMax;
	private String tempMin;
	
	//Constructor
	public Temperatura(String max, String min) {
		this.tempMax = max;
		this.tempMin = min;
		
	}

	public String getTempMax() {
		return tempMax;
	}

	public String getTempMin() {
		return tempMin;
	}
	
	
	
}
