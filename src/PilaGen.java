public class PilaGen<T>{
	private T [] pila;
	private static int tope;
	private static int tam;

	PilaGen(int ta){
		pila = (T[]) new Object[ta];
		tam = ta;
		tope =-1;
	}
	
	public T VerTope(){				//Obtiene el objeto tope actual de la pila
		if(!PilaVacia())
			return (T)pila[tope];
		return null;
	}
	
	public boolean Agregar(T item) {     //PUSH
		if(!PilaLlena()){
			pila[++tope] = (T)item;
			return true;
		}
		return false;
	}
	
	public boolean Remover() {       //POP
		if(!PilaVacia()){
			tope--;
			return true;
		}
		return false;
	}
	
	public static boolean PilaLlena(){ //Empty
		if(tope == tam-1)
			return true;
		return false;
	}
	
	public static boolean PilaVacia(){ //Full
		if(tope==-1)
			return true;
		return false;
	}
	
	public String Mostrar(){     //Muestra la pila
		String cad="";
		if(PilaVacia())
			return "La pila est� vac�a, no hay objeto que mostrar";
		for(int i=tope;i>=0;i--)
			cad+="\n\t| "+pila[i].toString()+" |\n\t-----";
		return cad;
	}
	
	public String toString(){     //Muestra la pila
		String cad="";
		if(PilaVacia())
			return "La pila est� vac�a, no hay objeto que mostrar";
		for(int i=0;i<=tope;i++)
			cad+=pila[i].toString()+" ";
		return cad;
	}
	/*
	public static void main (String[] args) {
		System.out.print("Cadena a introducir a la pila: ");
		String str = Leer.dato();
		
		PilaGen<Character> pila = new PilaGen<Character> (str.length());
		
		for(int i=0;i<str.length();i++)
			pila.Agregar(str.charAt(i));
		
		System.out.println(pila.Mostrar());
		
		if(pila.Remover())
			System.out.println("El elemento \""+pila.VerTope()+"\" ha sido removido con �xito");
		
		System.out.println(pila.Mostrar());
	}*/
}