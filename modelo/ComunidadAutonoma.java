package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class ComunidadAutonoma {

	//Atributos
	private TreeMap<String, String> mapaComunidades;
	
	//Constructor
	public ComunidadAutonoma() {
		this.mapaComunidades = new TreeMap<String, String>();
		guardarDatos();
		
	}
	
	public TreeMap<String, String> getMapaComunidades() {
		return mapaComunidades;
	}

	//Metodos
	public void guardarDatos() {
		
		String linea;
		String[] partesLinea;

		Scanner scanner;
		try {
			scanner = new Scanner(new File("src/ficheros/ComunidadAutonoma.txt"));

			while (scanner.hasNextLine()) {
				linea = scanner.nextLine();

				//Separar los datos 
				partesLinea = linea.split(";");

				//Llenar la lista todo el codigo por si solo se introduce el nombre
				mapaComunidades.put(partesLinea[1], partesLinea[0]);
				//System.out.println("Mapa Completo: " + mapaComunidades);


			}
			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}
		
	}

}
