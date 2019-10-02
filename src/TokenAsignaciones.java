import java.awt.*;
import java.io.PrintStream;
import java.util.Hashtable;
import java.lang.String;
import java.util.ArrayList;

class NotToken{
	public int typ;public String tok;
	NotToken(int t, String ty) {
		typ = t; tok = ty;
	}
}

class TokenAsignaciones
{
	
	  //Tabla que almacenara los tokens declarados
	  public static Hashtable tabla = new Hashtable();

	  //String que almacenara los tokens para imprimir 
	  public static ArrayList<NotToken> ListaTokens = new ArrayList<NotToken>();

	  public static String resultado;
	  
	  //Listas que guardaran los tipos compatibles de las variablesja
	  private static ArrayList<Integer> intComp = new ArrayList();
	  private static ArrayList<Integer> boolComp = new ArrayList();
	  private static ArrayList<Integer> strComp = new ArrayList();
	  //private static Ventana v = new Ventana();
												//variable		//tipoDato
	public static void InsertarSimbolo(Token identificador, int tipo)
	{
		//En este metodo se agrega a la tabla de tokens el identificador que esta siendo declarado junto con su tipo de dato
		tabla.put(identificador.image, tipo);
		
	}
	public static void SetTables()
	{
		/*
		En este metodo se inicializan las tablas, las cuales almacenaran los tipo de datos compatibles con:
		 entero = intComp
		 decimal = decComp
		 cadena = strComp
		 caracter = chrComp
		*/
		intComp.add(25);
		intComp.add(28);

		boolComp.add(26);
		boolComp.add(29);
		boolComp.add(30);

		strComp.add(27);
		strComp.add(31);
	}
 
	public static String checkAsing(Token TokenIzq, Token TokenAsig)
	{
		//variables en las cuales se almacenara el tipo de dato del identificador y de las asignaciones (ejemplo: n1(tipoIdent1) = 2(tipoIdent2) + 3(tipoIdent2))
		int tipoIdent1;
		int tipoIdent2;		
							/* De la tabla obtenemos el tipo de dato del identificador  
								asi como, si el token enviado es diferente a algun tipo que no se declara como los numeros(48), los decimales(50),
								caracteres(52) y cadenas(51)
								entonces tipoIdent1 = tipo_de_dato, ya que TokenAsig es un dato*/
		if(TokenIzq.kind != 28)		
		{
			try 
			{
				//Si el TokenIzq.image existe dentro de la tabla de tokens, entonces tipoIdent1 toma el tipo de dato con el que TokenIzq.image fue declarado
				tipoIdent1 = (Integer)tabla.get(TokenIzq.image);
			}
			catch(Exception e)
			{
				//Si TokenIzq.image no se encuentra en la tabla en la cual se agregan los tokens, el token no ha sido declarado, y se manda un error
                GUILayer.resultadoAnalisis.setForeground(Color.RED);
                GUILayer.resultadoAnalisis.setText("Error: El identificador " + TokenIzq.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine);
                return "Error: El identificador " + TokenIzq.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine;

            }
		}
		else 		
			tipoIdent1 = 0;
			
		//TokenAsig.kind != 48 && TokenAsig.kind != 50 && TokenAsig.kind != 51 && TokenAsig.kind != 52
		if(TokenAsig.kind == 32)	
		{
			/*Si el tipo de dato que se esta asignando, es algun identificador(kind == 49) 
			se obtiene su tipo de la tabla de tokens para poder hacer las comparaciones*/
			try
			{
				tipoIdent2 = (Integer)tabla.get(TokenAsig.image);
			}
			catch(Exception e)
			{
				//si el identificador no existe manda el error
                GUILayer.resultadoAnalisis.setForeground(Color.RED);
                GUILayer.resultadoAnalisis.setText("Error: El identificador " + TokenIzq.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine);
				return "Error: El identificador " + TokenAsig.image + " No ha sido declarado \r\nLinea: " + TokenIzq.beginLine;
			}
		}
				//Si el dato es entero(30) o booleano(31) o cadena(33)
				//tipoIdent2 = tipo_del_dato
		else if(TokenAsig.kind == 28 || TokenAsig.kind == 29 || TokenAsig.kind == 30 || TokenAsig.kind == 31)
			tipoIdent2 = TokenAsig.kind;
		else //Si no, se inicializa en algun valor "sin significado(con respecto a los tokens)", para que la variable este inicializada y no marque error al comparar
			tipoIdent2 = 0; 

			
	  
		
		if(tipoIdent1 == 25) //Int
        {
            //Si la lista de enteros(intComp) contiene el valor de tipoIdent2, entonces es compatible y se puede hacer la asignacion
            if (intComp.contains(tipoIdent2))
                return " ";
            else { //Si el tipo de dato no es compatible manda el error
                GUILayer.resultadoAnalisis.setForeground(Color.RED);
                GUILayer.resultadoAnalisis.setText("Error: No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: " + TokenIzq.beginLine);
                return "Error: No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: " + TokenIzq.beginLine;
            }
        }
		else if(tipoIdent1 == 26) //boolean
		{
			if(boolComp.contains(tipoIdent2))
				return " ";
			else{
                GUILayer.resultadoAnalisis.setForeground(Color.RED);
                GUILayer.resultadoAnalisis.setText("Error: No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: " + TokenIzq.beginLine);
				return "Error: No se puede convertir " + TokenAsig.image + " a boolean \r\nLinea: " + TokenIzq.beginLine;
		    }
		}
		else if(tipoIdent1 == 27) //string
        {
            if (strComp.contains(tipoIdent2))
                return " ";
            else {
                GUILayer.resultadoAnalisis.setForeground(Color.RED);
                GUILayer.resultadoAnalisis.setText("Error: No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: " + TokenIzq.beginLine);
                return "Error: No se puede convertir " + TokenAsig.image + " a Cadena \r\nLinea: " + TokenIzq.beginLine;
            }
        }
		else
		{
            GUILayer.resultadoAnalisis.setForeground(Color.RED);
            GUILayer.resultadoAnalisis.setText("Error: No se puede convertir " + TokenAsig.image + " a Entero \r\nLinea: " + TokenIzq.beginLine);
			return "El Identificador " + TokenIzq.image + " no ha sido declarado" + " Linea: " + TokenIzq.beginLine;
		}
		
	}	  
 }
  
  
  
  
  
  
  