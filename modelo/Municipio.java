package modelo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import excepciones.MunicipioNoExisteException;

public class Municipio {

	//Atributos
	private TreeMap<String, String> mapaMunicipios;
	private String codigoMunicipio;
	private String codigoSeleccionado;
	private ArrayList<Dia> dias;

	//Constructor
	public Municipio() {
		this.mapaMunicipios = new TreeMap<String, String>();
		this.dias = new ArrayList<Dia>();
		guardarDatos();

	}

	public TreeMap<String, String> getMapaMunicipios() {
		return mapaMunicipios;
	}

	public void setCodigoSeleccionado(String codigoSeleccionado) {
		this.codigoSeleccionado = codigoSeleccionado;
	}

	public String getCodigoSeleccionado() {
		return codigoSeleccionado;
	}

	public ArrayList<Dia> getDias() {
		return dias;
	}

	//Metodos
	public void guardarDatos() {

		String linea;
		String[] partesLinea;

		Scanner scanner;
		try {
			scanner = new Scanner(new File("src/ficheros/diccionario24.txt"));

			while (scanner.hasNextLine()) {
				linea = scanner.nextLine();

				//Separar los datos 
				partesLinea = linea.split(";");

				//Llenar la lista todo el codigo por si solo se introduce el nombre
				mapaMunicipios.put(partesLinea[3], partesLinea[0] + partesLinea[1] + partesLinea[2]);
				//System.out.println("Mapa Completo: " + mapaMunicipios);

			}
			scanner.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		}

	}

	public String obtenerCodigo(String nombre) throws MunicipioNoExisteException {

		try {

			if(nombre.equals("")) {
				throw new MunicipioNoExisteException();

			}
			this.codigoMunicipio = mapaMunicipios.get(nombre);

			//Eliminar el codigo de la Comunidad Autonoma para descargar el fichero XML
			this.codigoMunicipio = this.codigoMunicipio.substring(2);

			return this.codigoMunicipio;

		}catch (Exception e) {
			throw new MunicipioNoExisteException();

		}

	}

	public void descargar_crear_XML() {

		try {
			//Iniciar URL donde se encuentro el fichero a leer
			URL url = new URL("https://www.aemet.es/xml/municipios/localidad_" + this.codigoSeleccionado + ".xml");

			//Utilizar el scanner con el fichero especificado en la URL
			Scanner s = new Scanner(url.openStream());

			//Crear fichero local para guardar la información
			String fichero = "src/ficheros/localidad_actual.xml";
			PrintWriter pw = new PrintWriter(new File(fichero));

			String linea;

			//Leer el fichero
			while(s.hasNext()) {

				//Leer linea del fichero de Internet
				linea = s.nextLine();

				//Escribir linea en el fichero local
				pw.println(linea);

			}

			pw.close();
			s.close();

			informacionXML();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void informacionXML() {

		try {

			this.dias.clear();
			Temperatura t = new Temperatura("0", "0");
			Viento v = new Viento("0", "0");

			String fecha = "";
			String descripcion_estadoCielo = "";
			String probLLuvia = "";
			String uv = "";

			File fxmlFile = new File("src/ficheros/localidad_actual.xml");

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

			//Documento a recorrer con los datos del fichero XML
			Document doc = dBuilder.parse(fxmlFile);

			//La forma normal es útil para operaciones que requieren una estructura de árbol de documento particular
			doc.getDocumentElement().normalize();

			NodeList listaDias = doc.getElementsByTagName("dia");

			//Recorrer lista de los elementos dia
			for (int i = 0; i < listaDias.getLength(); i++) {

				Node elementoDia = listaDias.item(i);
				Element eElement = (Element) elementoDia;

				//1.- Dia
				fecha = eElement.getAttribute("fecha");

				//2.- Estado del cielo
				NodeList cieloLista = eElement.getElementsByTagName("estado_cielo");

				//Iterar sobre los elementos de la lista estado_cielo
				for (int j = 0; j < cieloLista.getLength(); j++) {
					Node cieloNode = cieloLista.item(j);
					Element cieloElement = (Element) cieloNode;

					//Si ya ha encontrado estado_cielo, sale
					if(!cieloElement.getAttribute("descripcion").equals("")) {
						descripcion_estadoCielo = cieloElement.getAttribute("descripcion");
						break;

					}

				}

				//3.- Temperatura
				NodeList nTemp = eElement.getElementsByTagName("temperatura");

				for (int j = 0; j < nTemp.getLength(); j++) {
					Node temperatura = nTemp.item(j);
					Element eTemp = (Element) temperatura;

					//4.- Temperatura Máxima
					NodeList temperaturaMaxima = eTemp.getElementsByTagName("maxima");
					Node maxima = temperaturaMaxima.item(0);
					Element valorTempMax = (Element) maxima;

					//5.- Temperatura Mínima
					NodeList temperaturaMinima = eTemp.getElementsByTagName("minima");
					Node minima = temperaturaMinima.item(0);
					Element valorTempMin = (Element) minima;

					//Guardar temperatura en objeto temperatura
					t = new Temperatura(valorTempMax.getTextContent(), valorTempMin.getTextContent());

				}				

				//4.- Probabilidad de lluvia
				NodeList nLluvia = eElement.getElementsByTagName("prob_precipitacion");

				for (int j = 0; j < nLluvia.getLength(); j++) {
					Node probabilidad = nLluvia.item(j);
					Element eProb = (Element) probabilidad;

					//Si ya ha encontrado lluvia, sale
					if(!eProb.getTextContent().equals("")) {
						probLLuvia = eProb.getTextContent();
						break;

					}	

				}				

				//5.- Direccion y velocidad del viento
				NodeList rachaViento = eElement.getElementsByTagName("viento");

				for (int j = 0; j < rachaViento.getLength(); j++) {
					Node nodoViento = rachaViento.item(j);
					Element eViento = (Element) nodoViento;

					if(!eViento.getElementsByTagName("direccion").item(0).getTextContent().equals("")
							|| !eViento.getElementsByTagName("velocidad").item(0).getTextContent().equals("")) {

						//Guardar viento
						v = new Viento(eViento.getElementsByTagName("direccion").item(0).getTextContent(), eViento.getElementsByTagName("velocidad").item(0).getTextContent());

					}

				}

				//6.- Indice ultraVioleta
				NodeList ultraVioleta = eElement.getElementsByTagName("uv_max");
				Node nUltraVioleta = ultraVioleta.item(0);
				Element eUltraVioleta = (Element) nUltraVioleta;

				//Si contiene el elemento
				if(eUltraVioleta != null) {
					uv = eUltraVioleta.getTextContent();

				}else {
					uv = "0";

				}

				Dia d = new Dia(fecha ,descripcion_estadoCielo , t, v, probLLuvia, uv);
				this.dias.add(d);

			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();

		} catch (SAXException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		}


	}

}
