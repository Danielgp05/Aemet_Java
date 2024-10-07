package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeMap;

public class Provincia {

	//Atributos
	private TreeMap<String, String> mapaProvincias;

	//Constructor
	public Provincia() {
		this.mapaProvincias = new TreeMap<String, String>();
		guardarDatos();
		
	}

	public TreeMap<String, String> getMapaProvincias() {
		return mapaProvincias;
		
	}

	public void setMapaProvincias(TreeMap<String, String> mapaProvincias) {
		this.mapaProvincias = mapaProvincias;
	}

	//Metodos
	public void guardarDatos() {

		String linea;
		String[] partesLinea;

		Scanner scanner;
		try {
			scanner = new Scanner(new File("src/ficheros/Provincias.txt"));

			while (scanner.hasNextLine()) {
				linea = scanner.nextLine();

				//Separar los datos 
				partesLinea = linea.split(";");

				//Llenar la lista todo el codigo por si solo se introduce el nombre
				mapaProvincias.put(partesLinea[2], partesLinea[0] + partesLinea[1]);
				//System.out.println("Mapa Completo: " + mapaProvincias);


			}
			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

	}


}
