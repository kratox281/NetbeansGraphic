package javaapplication5;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

public class Maquina {
    private Ticket planta1[][] = new Ticket[4][5];//Matriz que representa la primera planta del Parking
    private Ticket planta2[][] = new Ticket[4][5];//Matriz que representa la segunda planta del Parking
    private Ticket planta3[][] = new Ticket[4][5];//Matriz que representa la tercera planta del Parking
    private double pm;//Precio por minuto
    public static double introducido = 0 ;
    private Date hoy = Date.from(Instant.now()); //Establece la fecha de hoy
    private DepositoDinero deposito = new DepositoDinero(); //Crea un deposito de Dinero
    private Ticket vacio= new Ticket("",hoy,0,0,0);//Crea un Ticket que representa las plazas vacias

    //El constructor rellena las matrices con el Ticket vacio
    public Maquina(double prm) {
        this.pm = prm;
        IntroducirDatos.rellenar(planta1,vacio);
        IntroducirDatos.rellenar(planta2,vacio);
        IntroducirDatos.rellenar(planta3,vacio);
    }
    //Este constructor pide por pantalla el precio por minuto, el otro lo establece fijo como valor en el codigo
    public Maquina() {
        
        IntroducirDatos.rellenar(planta1,vacio);
        IntroducirDatos.rellenar(planta2,vacio);
        IntroducirDatos.rellenar(planta3,vacio);
    }

     public  boolean meterCocheP1(String Matricula) {
        //Comprueba la matricula del coche antes de crear el ticket
        //Busca un hueco libre en el que meter el vehiculo, y en caso de encontrarlo crea el ticket.
        for (int i = 0; i < planta1.length; i++) {
            for (int j = 0; j < planta1[0].length; j++) {
                if (planta1[i][j].compareTo(vacio)) {
                    this.planta1[i][j] = new Ticket(Matricula, Date.from(Instant.now()), i, j, 1);
                    System.out.println("El coche está en la primera planta");
                    System.out.println("Su ticket es el siguiente,recuerdelo bien");
                    planta1[i][j].imprimirTicket();
                    
                    return true;
                }
            }
        }
        //En caso de no encontrarlo intenta realizar la busqueda en introducción en la segunda planta
        MeterCocheP2(Matricula);
        return false;
    }

    @SuppressWarnings("empty-statement")
    public  boolean MeterCocheP2(String Matricula) {
        for (int i = 0; i < planta2.length; i++) {
            for (int j = 0; j < planta2[0].length; j++) {
                if (planta2[i][j].compareTo(vacio)) {
                    this.planta2[i][j] = new Ticket(Matricula, hoy, i, j, 2);;
                    System.out.println("El coche está en la segunda planta");
                    System.out.println("Su ticket es el siguiente,recuerdelo bien");
                    planta2[i][j].imprimirTicket();
                    return true;
                }
            }
        }
        //En caso de no encontrarlo intenta realizar la busqueda en introducción en la tercera planta
        MeterCocheP3(Matricula);
        return false;
    }

    public boolean MeterCocheP3(String Matricula) {
        for (int i = 0; i < planta3.length; i++) {
            for (int j = 0; j < planta3[0].length; j++) {
                if (planta3[i][j].compareTo(vacio)) {
                    this.planta3[i][j] = new Ticket(Matricula, hoy, i, j, 3);
                    System.out.println("El coche está en la tercera planta");
                    System.out.println("Su ticket es el siguiente,recuerdelo bien");
                    planta3[i][j].imprimirTicket();
                    return true;
                }
            }
        }
        //En caso de no encontrar un hueco libre en ninguna de las plantas muestra al usuario un mensaje indicando que no hay ningun hueco
        System.out.println("No hay hueco en ninguna de las 3 plantas");
        return false;
    }
       
     public boolean retirarCocheP1(int id){
        //Pide el id del ticket para proceder a cobrar y retirar el vehículo.
        Ticket aRetirar = buscarTicket(id);
        try {
            //Establece cuanto hay que pagar
            double precio = coste(aRetirar);
             new IntroducirDinero(precio).setVisible(true);
            try{
                //Cobra al usuario
                pagar(precio,introducido);
                //Procede a buscar y retirar el vehiculo, indicando los datos del ticket asociado
                for (int i = 0; i < planta1.length; i++) {
                    for (int j = 0; j < planta1[0].length; j++) {
                        if (planta1[i][j].getId() == id) {
                            System.out.println("Se retirará el vehiculo con la siguiente informacion");
                            System.out.println(planta1[i][j].toString());
                            planta1[i][j] = vacio;
                            return true;
                        }
                    }
                }
            }catch (Exception ex){
                ex.getMessage();
            }
            retirarCocheP2(id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return false;
    }
    public boolean retirarCocheP2(int id){
        for (int i = 0; i < planta2.length; i++) {
            for (int j = 0; j <planta2[0].length; j++) {
                if (planta2[i][j].getId()==id){
                    System.out.println("Se retirará el vehiculo con la siguiente informacion");
                    System.out.println(planta2[i][j].toString());
                    planta2[i][j] = vacio;
                    return true;
                }
            }
        }
        retirarCocheP3(id);
        return false;
    }
    public boolean retirarCocheP3(int id){
        for (int i = 0; i < planta3.length; i++) {
            for (int j = 0; j <planta3[0].length; j++) {
                if (planta3[i][j].getId()==id){
                    System.out.println("Se retirará el vehiculo con la siguiente informacion");
                    System.out.println(planta3[i][j].toString());
                    planta3[i][j] = vacio;
                    return true;
                }
            }
        }
        System.out.println("El id introducido no es valido");
        return false;
    }
    public double coste(Ticket saliente){
        //Esta es la introduccion manual de los minutos
        //int mins = IntroducirDatos.introducirInts("Introduzca cuantos minutos ha estado");
        //Este es el calculo automatico de tiempo transcurrido
        Date inicio = saliente.entrada;
        Date fin = Date.from(Instant.now());
        //Calcula los minutos que ha estado realizando la resta entre el momento de la retirada del vehiculo y el momento de la creación del ticket
        //Y realiza la conversion de milisegundos a minutos.
        int mins = (int) ((fin.getTime()-inicio.getTime())/60000)+1;
        //Indica al usuario el tiempo que ha estado y cuanto ha de pagar
        System.out.println("Has estado :"+mins+" minutos");
        System.out.println("El importe asciende a: "+mins*this.pm+"€");
        return mins*this.pm;
    }
    public void pagar(double coste,double introducido) throws ParkingException{
        //Comprueba que el dinero introducido no es menor que el coste
        if (coste>introducido){
            new Errores("Importe superior a lo introducido");
            return;
        }else{
            //En caso de ser mayor procede a devolver el dinero sobrante
            
            deposito.darVuelta(coste,introducido);
        }
    }
    public boolean comprobarMatricula(String matricula){
        //Comprueba la longitud de la matricula
        if (matricula.length()!=7){
            return false;
        }
        //Comprueba si los 4 primeros caracteres son numéricos
        for (int i = 0; i <4; i++) {
            if(!Character.isDigit(matricula.charAt(i))){
                return false;
            }
        }
        //Comprueba que los 3 últimos caracteres sean letras
        for (int i = 4; i < 7; i++) {
            if (!Character.isLetter(matricula.charAt(i))){
                return false;
            }
        }
        return true;
    }
    //Imprime cada planta del parking
    public String mostrarP1(){
        String linea = "";
        for (int i = 0; i < planta1.length; i++) {
            for (int j = 0; j <planta1[0].length; j++) {
                if(planta1[i][j].compareTo(vacio)){
                    linea += "║  ║";
                   
                }
                else{
                    linea += "║█║";
                }
                }
            linea+= "\n";
            }
        return linea;
    }
    public String mostrarP2(){
        String linea ="";
        for (int i = 0; i < planta1.length; i++) {
            for (int j = 0; j <planta1[0].length; j++) {
                if(planta2[i][j].compareTo(vacio)){
                      linea += "║  ║";
                  
                }
                else{
                  
                    linea += "║█║";
               
                }
            }
            linea+= "\n";
        }
        return linea;

    }
        public String mostrarP3(){
            String linea = "";
        for (int i = 0; i < planta1.length; i++) {
            for (int j = 0; j <planta1[0].length; j++) {
                if(planta3[i][j].compareTo(vacio)){
                    linea += "║  ║";
                }
                else{
                   linea += "║█║";
                }
            }
            linea+= "\n";
        }
        return linea;
    }

    //Devuelve un ticket empleando el id para buscar
    public Ticket buscarTicket(int id){
        for (int i = 0; i < planta1.length; i++) {
            for (int j = 0; j < planta1[0].length; j++) {
                if (planta1[i][j].getId() == id) {
                    return planta1[i][j];
                }
            }
        }
         for (int i = 0; i < planta2.length; i++) {
            for (int j = 0; j < planta2[0].length; j++) {
                if (planta2[i][j].getId() == id) {
                    return planta2[i][j];
                }
            }
        }
         for (int i = 0; i < planta3.length; i++) {
            for (int j = 0; j < planta3[0].length; j++) {
                if (planta3[i][j].getId() == id) {
                    return planta3[i][j];
                }
            }
        }
         return vacio;

    }
}



