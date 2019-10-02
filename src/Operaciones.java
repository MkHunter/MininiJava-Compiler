import java.util.ArrayList;
import java.util.Scanner;

public class Operaciones {
    public ArrayList<Character> operadores;
    public String pos, varDestino = "";
    public String subSegmentoCodigo = "";
    public int Temporal = 1;
    private ArrayList<Intermedio.TokInt> listaTokens;

    public Operaciones(String varDestino, ArrayList<Intermedio.TokInt> list, int Temporal) {
        this.listaTokens = list;
        this.Temporal = Temporal;
        this.varDestino = varDestino;
        subSegmentoCodigo += "\n;Cálculo de Asignación de " + varDestino + "\n";
        iniciPrioridad();
    }

    private void iniciPrioridad() {
        operadores = new ArrayList<Character>();
        operadores.add('('); //0
        operadores.add(')'); //1
        operadores.add('-'); //2
        operadores.add('+'); //3
        operadores.add('/'); //4
        operadores.add('*'); //5
    }

    public boolean ValOpe(String[] exp) {
        if (EsOpe(exp[0].charAt(0)) || EsOpe(exp[0].charAt(exp[0].length() - 1)))   //Si un operador está al principio o al final de la cadena
            return false;
        for (int i = 1; i < exp.length - 1; i++) {                              //Si hay dos operadores juntos
            char tempchar = exp[i].charAt(0);
            if (EsOpe(tempchar) && EsOpe(exp[i - 1].charAt(0)))
                return false;
            if (EsOpe(tempchar) && EsOpe(exp[i + 1].charAt(0)))
                return false;
        }
        return true;
    }

    private boolean EsOpe(char c) {
        return operadores.contains(c);
    }

    int tempApliicarOp = 0;

    public String EvExpPos(String [] exp){
        pos = ObtExpPos(exp);
        System.out.println("-------------------------------------------");
        String tempOp1,tempOp2;
        boolean op1EsVar,op2EsVar;
        int res=0;
        int ope1 = 0,ope2 = 0;
        PilaGen<String> operandos = new PilaGen<String> (pos.length());

        for(int i=0;i<pos.length();i++) {                       //1*3+(4+3)       (6+4)*8*(7+4)
            tempOp1 = null;
            tempOp2 = null;
            //op1EsVar = false;
            //op2EsVar = false;
            int priopridad = ObtPrio(Character.toString(pos.charAt(i)));
            if (priopridad == 0){                               //Si es un número o una variable empezará con ( : Prioridad 0
                int j = i+1;
                String varOrNumero = "";
                while(ObtPrio(Character.toString(pos.charAt(j))) != 1){ //
                    varOrNumero += pos.charAt(j);
                    j++;
                }
                //op1EsVar = esNumero(varOrNumero);
                /*
                if(!esNumero(varOrNumero)){
                    varOrNumero = getValueOfToken(varOrNumero.toUpperCase()); //Recobra el valor actual de la variable
                }*/
                operandos.Agregar(varOrNumero);

                i+=j-i; //Desplaza el contador a la posición de la variable en la que te quedaste. Si no encontró variable es i+= 1;
            }else{                                              //Si es un operando

                tempOp1 = operandos.VerTope().toUpperCase();
                operandos.Remover();
                tempOp2 = operandos.VerTope().toUpperCase();

                op1EsVar = existeVariable(tempOp1) && !esNumero(tempOp1);
                op2EsVar = existeVariable(tempOp2) && !esNumero(tempOp2);

                if (op1EsVar){
                    ope1 = Integer.parseInt(getValueOfToken(tempOp1)); //Toma el valor de su token
                }else{
                    ope1 = Integer.parseInt(tempOp1);                  //En un número en string. Sólo conviertelo a int.
                }
                if (op2EsVar){
                    ope2 = Integer.parseInt(getValueOfToken(tempOp2)); //Toma el valor de su token
                }else{
                    ope2 = Integer.parseInt(tempOp2); //Es un número en string. Sólo conviertelo a int.
                }
                /*
                ope1 = Integer.parseInt(tempOp1);
                ope2 = Integer.parseInt(tempOp2);
                */
                //System.out.println(ope1+" "+ope2+" "+tempOp1+" "+tempOp2+" "+op1EsVar+" "+op2EsVar);

                res = AplicarOp(pos.charAt(i),ope1,ope2,tempOp1,tempOp2,op1EsVar,op2EsVar);

                operandos.Remover();
                operandos.Agregar(res+"");
                System.out.println("Operación: "+ope1+" "+pos.charAt(i)+" "+ope2+" res: "+res);

                res=0;
            }
        }
        // Otros casos a tratar...
        //If BOOL - true 1 - false - 0
        //If STR - CADENA
        //If INT - T...
        res = Integer.parseInt(operandos.VerTope());
        //Si el token existe, actualiza su valor;
        getTokenValueByName(varDestino,""+res);
        for (Intermedio.TokInt t:listaTokens) {
            if(t.Token.equals(varDestino)){
                t.valor = ""+res;
            }
        }
        //Cuando sea una asignación directa: var1 = var; o var1 = 1;...
        if(exp[1] == null){
            subSegmentoCodigo+= "    MOV "+varDestino+", "+operandos.VerTope()+"\n";
        }else { // Si no...
            subSegmentoCodigo += "    MOV " + varDestino + ", R" + (Temporal-1) + "\n";
        }
        System.out.println("--------------------Vslor resultante------- "+res);
        printTok();
        return subSegmentoCodigo;
    }

    public String ObtExpPos(String[] exp) {
        String expPos = "";
        PilaGen<String> operadores = new PilaGen<String>(exp.length);
        for (int i = 0; i < exp.length; i++) {
            String caracter = exp[i];
            if (caracter == null) {
                continue;
            }
            int prioridad = ObtPrio(caracter);
            if (prioridad == -1) {                    //Si es un operando: 3123(#) o var(variable)
                expPos += "(" + caracter + ")";
            } else {                                            //SI ES OPERADOR(#)
                if (operadores.PilaVacia())
                    operadores.Agregar(caracter);
                else {
                    int prioridadTope = ObtPrio(operadores.VerTope());
                    if (prioridad > prioridadTope && !((prioridad == 2 && prioridadTope == 3) || (prioridad == 3 && prioridadTope == 2)))
                        operadores.Agregar(caracter);
                    else {
                        do {                                        //Si es un operador * / + -
                            expPos += operadores.VerTope();
                            operadores.Remover();
                        } while (!operadores.PilaVacia() && !(prioridad > prioridadTope));
                        operadores.Agregar(caracter);
                    }
                }
            }
            //System.out.println(caracter+" // "+operadores.toString()+ " // "+expPos);
        }

        while (!operadores.PilaVacia()){                                                       //Vaciar Pila
            expPos += operadores.VerTope();
            operadores.Remover();
        }

        System.out.println("Expresión en posfijo: "+expPos);
        return expPos;
    }

    public int AplicarOp(char c, int ope1, int ope2, String tempOp1, String tempOp2, boolean op1EsVar, boolean op2EsVar) {
        //System.out.println(ope1 + " " + ope2 + " " + tempOp1 + " " + tempOp2 + " " + op1EsVar + " " + op2EsVar);
        int r = 0;
        int opc = ObtPrio(Character.toString(c));
        /*
        if(Temporal == 1){
            subSegmentoCodigo+= "    MOV T"+Temporal+", "+ope1+"\n";
        }*/
        switch (opc) {
            case 5:
                r = ope2 * ope1; // *
                subSegmentoCodigo += ";Operación de Multiplicación\n" +
                        "    MOV EAX, " + ope1 + "\n" +
                        "    IMUL " + (tempApliicarOp == 0?(op1EsVar? tempOp2:ope2): "R"+(Temporal-1)) + "\n" +
                        "    MOV " + varDestino + ", EAX\n";
                break;
            case 4:
                r = ope2 / ope1; // /
                subSegmentoCodigo += ";Operación de División\n" +
                        "    MOV " + varDestino + ", " + ope1 + "\n" +
                        "    IDIV " + varDestino + ", " + ope2 + "\n";
                break;
            case 3:
                r = ope2 + ope1; // +
                subSegmentoCodigo += ";Operación de Suma\n" +
                        "    MOV R" + Temporal + ", " + (op1EsVar ? tempOp1 : ope1) + "\n" +//(tempApliicarOp == 0?(op1EsVar? tempOp1:ope1): "T"+(Temporal-1))
                        "    ADD R" + Temporal + ", " + (tempApliicarOp == 0?(op2EsVar? tempOp2:ope2): "R"+(Temporal-1)) + "\n";
                break;
            case 2:
                r = ope2 - ope1; // -
                subSegmentoCodigo += ";Operación de Resta\n" +
                        "    MOV R" + Temporal + ", " + (tempApliicarOp == 0?(op2EsVar? tempOp2:ope2): "R"+(Temporal-1)) + "\n" +
                        "    SUB R" + Temporal + ", " + (op1EsVar ? tempOp1 : ope1) + "\n";//(tempApliicarOp == 0?(op1EsVar? tempOp1:ope1): "T"+(Temporal-1))
                break;
        }
        tempApliicarOp++;
        Temporal++; //Incremento del Temporal
        return r;
    }



    public int ObtPrio(String exp) {
        int r = -1;
        for (int j = 0; j < operadores.size(); j++) {
            if (operadores.get(j) == exp.charAt(0)) {
                return j;
            }
        }
        return r;
    }

    public boolean esNumero(String s) {
        boolean numeric = true;
        try {
            Double num = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            numeric = false;
        }
        return numeric;
    }

    private void printTok(){
        for (Intermedio.TokInt t:listaTokens) {
                System.out.println(t.Tipo+" Tipo  Token /"+t.Token+" Valor: "+t.valor);
        }
    }

    private String getValueOfToken(String identificadorTok){
        String value = "0";
        for (Intermedio.TokInt t:listaTokens) {
            if(t.Token.equals(identificadorTok)){
                value = t.valor;
            }
        }
        return value;
    }

    private void getTokenValueByName(String identificadorTok,String nuevoValor){
        for (Intermedio.TokInt t:listaTokens) {
            if(t.Token.equals(identificadorTok)){
                t.valor = nuevoValor;
            }
        }
    }

    private boolean existeVariable(String identificadorTok){
        boolean existe = false;
        for (Intermedio.TokInt t:listaTokens) {
            if(t.Token.equals(identificadorTok)){
                existe = true;
            }
        }
        return existe;
    }
}