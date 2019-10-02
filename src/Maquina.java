import java.util.ArrayList;

public class Maquina {
    private String CM = "";                                     //Segmento de Código Máquina
    private ArrayList<String[]> CodeSegment;
    private ArrayList<String> variables = new ArrayList<String>();
    private String [] [] opCodes = {{      null,      null,"11110110",     null},   //IMUL
            {"10000000","10000111",      null,     null},   //SUB
            {"10000010","10000011",      null,     null},   //ADD
            {"11000110","11000111","11000110","10001100"}}; //MOV

    Maquina(String codIntermedio_txt){
        CodeSegment = getInstructions(codIntermedio_txt);       //Se toma lo importante del Código Intermedio
        System.out.println("\t--------------MAQUINA--------------");

        magic();
    }

    private void magic(){
        for (String[] l: CodeSegment) {
            String opCode = "Error",dir = "";
            String Instruccion = l[0];
            if (Instruccion.equals("IMUL")){
                CM+= "IMUL\t\t";
                opCode = opCodes[0][2];
                dir = "11000000 "+toBinarySpace(l[1],8);
            }else{
                if (l[1].equals("DD")){
                    variables.add(Instruccion);
                    continue;
                }

                String r1 = l[1], r2 = l[2];

                int type = dirMode(r1,r2);
                //System.out.println(type+" "+r1+" "+r2);
                dir = getdirString(type,r1,r2);

                if (Instruccion.equals("ADD")){
                    opCode = opCodes[2][type-1];
                }else if (Instruccion.equals("SUB")){
                    opCode = opCodes[1][type-1];
                }else if (Instruccion.equals("MOV")){
                    opCode = opCodes[3][type-1];
                }
                CM+= Instruccion+" "+r1+", "+r2+"\t";
            }
            CM+=opCode+" "+dir+"\n";
        }
        getVariables();
        System.out.println(getCodMaquina());

    }

    public static void main(String[] args){
        String testS = "    TITLE PRAC01\n" +
                "    .MODEL SMALL\n" +
                "    .486\n" +
                "    .STACK\n" +
                "    .DATA\n" +
                "VAR1 DD 0\n" +
                "VAR2 DD 0\n" +
                "VAR3 DD 0\n" +
                "    .CODE\n" +
                "MAIN PROC FAR\n" +
                "    .STARTUP\n" +
                "\n" +
                ";Cálculo de Asignación de VAR1\n" +
                "    MOV VAR1, 3\n" +
                "\n" +
                ";Cálculo de Asignación de VAR2\n" +
                ";Operación de Multiplicación\n" +
                "    MOV EAX, 3\n" +
                "    IMUL 2\n" +
                "    MOV VAR2, EAX\n" +
                ";Operación de Suma\n" +
                "    MOV R2, 6\n" +
                "    ADD R2, R1\n" +
                "    MOV R2, R2\n" +
                "\n" +
                ";Cálculo de Asignación de VAR3\n" +
                ";Operación de Suma\n" +
                "    MOV R3, R2\n" +
                "    ADD R3, 1\n" +
                "    MOV R3, R3\n" +
                "    .EXIT\n" +
                "MAIN EndP\n" +
                "    END";
        Maquina m = new Maquina(testS);
        System.out.println(m.getCodMaquina());
    }

    private String getdirString(int type,String r1,String r2){
        String str = "";
        switch (type){
            case 1: //VAR, #
                String varDir = "";
                if(isInMemory(r1)){
                    varDir = toBinarySpace(""+r1.charAt(1),3);
                }else if (existVariable(r1)){
                    varDir = varToIndex(r1);
                }
                String inmediate = toBinarySpace(r2,8);
                //System.out.println("CASE 1: r1: "+r1+" r2: "+r2+" varDir: "+varDir+" inmediate: "+inmediate);
                str = "00000" + varDir + " "+inmediate;
                break;
            case 2://R#, R# || R#, VAR
                if(isInMemory(r1)){
                    str= "00" + toBinarySpace(""+r1.charAt(1), 3);
                }else if (existVariable(r1)){
                    str= "00" + varToIndex(r1);
                }

                if(isInMemory(r2)){
                    str += toBinarySpace(""+r2.charAt(1), 3);
                }
                if (existVariable(r2)){
                    //System.out.println("CASE 1: r1: "+r1+" r2: "+r2+" varDir: "+varToIndex(r2)+" inmediate: "+toBinarySpace(r2,8));
                    str += varToIndex(r2);
                }
                break;
            case 3://EAX, #
                str= "11000000 "+toBinarySpace(r2,8);
                break;
            case 4://R#, EAX || VAR, EAX
                if (isInMemory(r1)){
                    str= "11"+toBinarySpace(""+r1.charAt(1), 3)+"000";
                }else if (existVariable(r1)){
                    String var = varToIndex(r1);
                    //System.out.println("CASE 4 r1 "+r1+" binary "+var);
                    str= "11" + var + "000";
                }
                break;
            default:
                System.out.println("Algo salió mal?");
        }
        return str;
    }


    private int dirMode(String r1, String r2){
        int type = 1;
        if(isInMemory(r1) || existVariable(r1)){
            if(Character.isDigit(r2.charAt(0)))
                type = 1;
            if(isInMemory(r2) || existVariable(r2))
                type = 2;
        }
        if (r1.equals("EAX"))
            type = 3;
        if(r2.equals("EAX"))
            type = 4;
        return type;
    }

    private boolean isInMemory(String r){
        if(r.length() == 2){
            if (r.charAt(0) == 'R' && Character.isDigit(r.charAt(1)))
                return true;
        }
        return false;
    }

    private  boolean existVariable(String s){
        return variables.contains(s);
    }

    private String toBinarySpace(String s, int wantedSize){
        if( wantedSize > 8)
            return "Error fillingWinZero()";

        String auxS = Integer.toBinaryString(Integer.parseInt(s));

        //Agrega bits con 0 en la mayor prioridad
        do {
            auxS = "0"+auxS;
        }while(auxS.length() < wantedSize);

        //Elimina bits de mayor prioridad
        while(auxS.length() > wantedSize){
            auxS = auxS.substring(1,auxS.length());
        }

        return auxS;
    }

    private String varToIndex(String varName){
        int index = 8 - (variables.indexOf(varName) + 1);
        return toBinarySpace(""+index, 3);
    }

    private String getVariables(){//Vector de Vectores... [[CLASS,class],[IDENTIFICADOR, haha], ... ]
        String t = "";
        for (int i = 0;i < variables.size(); i++) {
            t += toBinarySpace(""+i,3)+" "+variables.get(i)+" ";
        }
        return t+"\n";
    }

    public ArrayList<String[]> getInstructions(String s){//Vector de Vectores... [[CLASS,class],[IDENTIFICADOR, haha], ... ]
        String [] lines = s.split("\\n");
        boolean commentOrSpace = false, ignore = false;
        String tempStr = "";
        String [] tempLine;
        ArrayList<String[]> AsmTokens = new ArrayList<String[]>();

        for (int i = 5; i < lines.length-3; i++) {
            for (int j = 0; j < lines[i].length(); j++) {
                char c = lines[i].charAt(j);
                if (c == ' ') {
                    if (j == 0) {
                        do {
                            j++;
                        } while (lines[i].charAt(j + 1) == ' '); //Eliminar sangría
                        continue;
                    }
                }
                commentOrSpace = c == ';' && j == 0; //Si empieza con ; es comentario
                if (commentOrSpace)
                    break;
                if (c == ',')                          //Ignorar las comas
                    continue;
                tempStr += c;
            }

            String[] ignoredToks = {".CODE", "MAIN PROC FAR", ".STARTUP"};
            for (String st : ignoredToks) {
                if (tempStr.equals(st)) {
                    ignore = true;
                    break;
                }
            }

            //Ignorar en caso de ser Cabecilla de segmento, comentario o linea vacía
            ignore = (ignore && !commentOrSpace) || tempStr == "";

            if (!ignore) {
                tempLine = tempStr.split("\\s");
                AsmTokens.add(tempLine);
            }
            ignore = false;
            tempStr = "";
        }
        for (String[] l: AsmTokens) {
            for (int i = 0;i<l.length;i++){
                System.out.print(l[i]+" ");
            }
            System.out.println();
        }
        return AsmTokens;
    }

    public String getCodMaquina(){
        return CM;
    }
}
