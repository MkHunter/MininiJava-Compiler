import java.util.ArrayList;
import java.util.Vector;

public class Intermedio {
    public static class TokInt{
        public String Token, Tipo,valor;
        TokInt(String Tipo, String Token){
            this.Token = Token;
            this.Tipo  = Tipo;
            //System.out.println(Tipo+" Tipo / Token "+Token+" Valor: "+valor);
            valor = (Tipo.equals("STR")?"": "0");
        }

        @Override
        public String toString() {
            return "("+"Token: "+ Token+"\tTipo: "+Tipo+"\tValor: "+valor+")";
        }
    }
    private String SS="",DS="",CS=""; //Segmento de Pila, Datos y Código
    private int Temporal = 1;
    private ArrayList<TokInt> listaTokens = new ArrayList<TokInt>();
    private String [][] CodeSegment;
    Intermedio(Vector v){
        CodeSegment = getTokens(v); //Se toma lo importante de la lista de Tokens...
        System.out.println("\t--------------INTERMEDIO--------------");
        for (int i = 0;i < CodeSegment.length; i++) {
            String Tipo = CodeSegment[i][0];
            String Token = CodeSegment[i][1];

            if(Tipo.equals("STR") || Tipo.equals("INT") || Tipo.equals("BOOLEAN") ){ //En caso de ser declaración
                TokInt tempTok= new TokInt(Tipo, CodeSegment[i+1][1].toUpperCase());
                listaTokens.add(tempTok);
                addVar();
                i+=2;
                continue;
            }else if (Tipo.equals("ASIGNACIONES")){ //En caso de ser asignación
                String varDestino = CodeSegment[i-1][1];//Atrás del "=" se ecuentra el nombre de la variable
                int qty = i+1,tempIndex = i+1,tokIdx = 0;
                do{ qty++; }while(!CodeSegment[qty][0].equals("SEMICOLON"));
                String [] toksOperacion = new String[qty - i];
                do{
                    toksOperacion[tokIdx++] = CodeSegment[tempIndex][1];
                    tempIndex++;
                }while(!CodeSegment[tempIndex][0].equals("SEMICOLON"));

                Operaciones opera = new Operaciones(varDestino.toUpperCase(),listaTokens,Temporal);

                CS += opera.EvExpPos(toksOperacion);

                Temporal = opera.Temporal;
            }else if (Tipo.equals("PRINT")){

            }
        }
        printVariables();
    }

    private String[][] getTokens(Vector v){//Vector de Vectores... [[CLASS,class],[IDENTIFICADOR, haha], ... ]
        String[][]mat = new String[v.size()-16][2];
        for (int i = 0;i < v.size() - 2; i++) {
            if(i > 13) {
                Vector temp = (Vector) v.elementAt(i);
                mat[i-14][0] = temp.elementAt(0).toString();
                mat[i-14][1] = temp.elementAt(1).toString();
                //System.out.println(mat[i-14][0]+"\t"+mat[i-14][1]);
            }
        }
        return mat;
    }

    private void printVariables(){//Vector de Vectores... [[CLASS,class],[IDENTIFICADOR, haha], ... ]
        for (int i = 0;i < listaTokens.size(); i++) {
            String t1 = listaTokens.get(i).Token;
            String t2 = listaTokens.get(i).Tipo;
            String t3 = listaTokens.get(i).valor;
            System.out.println("Variable "+i+" | Nombre : "+t1+"\t Tipo :"+t2 +"\t Valor :"+t3 );
        }
    }

    public String getCodIntermedio(){
        return "    TITLE PRAC01\n" +
                "    .MODEL SMALL\n" +
                "    .486\n" +
                "    .STACK\n" +
                SS +
                "    .DATA\n" +
                DS +
                "    .CODE\n" +
                "MAIN PROC FAR\n" +
                "    .STARTUP\n" +
                CS +
                "    .EXIT\n" +
                "MAIN EndP\n" +
                "    END"
                ;
    }

    public void addPrint(String msg){
        CS += ";Mostrar el mensaje\n" +
                "    MOV BX,0001H\n" +
                "    LEA DX,"+msg+"\n" +
                "    MOV AH,09H\n" +
                "    INT 21H\n" +
                "    \n";
    }

    public void addVar(){
        DS = "";
        for (int i = 0;i < listaTokens.size(); i++) {
            TokInt temp = listaTokens.get(i);
            String Tipo = temp.Tipo, Token = temp.Token, Valor = temp.valor;
            if (Tipo.equals("STR"))
                DS += Token+" DB \""+Valor+"$\",0AH,0DH\n";
            if (Tipo.equals("INT"))
                DS += Token+" DD "+Valor+"\n";
            if (Tipo.equals("BOOLEAN"))
                DS += Token+" DB "+Valor+",0AH,0DH\n";
        }
    }
}
