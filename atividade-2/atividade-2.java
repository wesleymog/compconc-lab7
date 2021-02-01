import java.util.Random;
// Monitor
class CP {
  private int leit, escr, buffer;  
  
  // Construtor
  CP() { 
     this.leit = 0; //consumidores lendo (0 ou mais)
     this.escr = 0; //produtor escrevendo (0 ou 1)
  } 
  
  // Entrada para consumidores
  public synchronized void EntraConsumidor(int id, ThreadSafe ts) {
    try { 

      while (((this.leit > 0) || (this.escr > 0)) || ts.returnfull() == 1) {
      //if (this.escr > 0) {
         System.out.println ("cp.consumidorBloqueado("+id+")");
         wait();  //bloqueia pela condicao logica da aplicacao 
      }
      this.leit++;  //registra que ha mais um consumidor lendo
      System.out.println ("cp.consumidorLendo("+id+")");
    } catch (InterruptedException e) { }
  }
  
  // Saida para consumidores
  public synchronized void SaiConsumidor (int id) {
     this.leit--; //registra que um consumidor saiu
     this.notifyAll();//libera produtor (caso exista produtor bloqueado)
     System.out.println ("cp.consumidorSaindo("+id+")");
  }
  
  // Entrada para produtores
  public synchronized void EntraProdutor (int id, ThreadSafe ts) {
    try { 
      while (((this.leit > 0) || (this.escr > 0)) || ts.returnfull() == 2) {
      //if ((this.leit > 0) || (this.escr > 0)) {
         System.out.println ("cp.produtorBloqueado("+id+")");
         wait();  //bloqueia pela condicao logica da aplicacao 
      }
      this.escr++; //registra que ha um produtor escrevendo
      System.out.println ("cp.produtorEscrevendo("+id+")");
    } catch (InterruptedException e) { }
  }
  
  // Saida para produtores
  public synchronized void SaiProdutor (int id) {
     this.escr--; //registra que o produtor saiu
     notifyAll(); //libera consumidores e produtores (caso existam consumidores ou produtores bloqueados)
     System.out.println ("cp.produtorSaindo("+id+")");
  }
}

class ThreadSafe{
  private int buffer[];
  private int full;
    // Construtor
  ThreadSafe() { 
     this.buffer = new int [100];
     this.full = 0;
  } 

  public void printBuffer(){
    String bufferstr = "";
    bufferstr += "buffer = [ ";
    int i=0;
    while(i<100){
      bufferstr += this.buffer[i]+", ";
      i++;
    }
    bufferstr += "]";
    System.out.println (bufferstr);
  }

  public void insertBuffer(int value){
    if(this.buffer[0] == 0 ){
      this.full = 2;
      for(int i=0; i<100;i++){
        this.buffer[i] = value;
      }
      
      //System.out.println ("ts.insertThreadSafe("+value+",True)");
    }else{
      //System.out.println ("ts.insertThreadSafe("+value+",False)");
    }
  }

  public void removeBuffer(){
    this.full = 1;
    if(this.buffer[0] != 0){
      for(int i=0; i<100;i++){
        this.buffer[i] = 0;
      }
      //System.out.println ("ts.removeThreadSafe(True)");
    }else{
      //System.out.println ("ts.removeThreadSafe(False)");
    }
  }
  public int returnfull(){
    return this.full;
  }


}

//--------------------------------------------------------
// Consumidor
class Consumidor extends Thread {
  int id; //identificador da thread
  int delay; //atraso bobo
  CP monitor;//objeto monitor para coordenar a lógica de execução das threads
  ThreadSafe ts;
  // Construtor
  Consumidor (int id, int delayTime, CP m, ThreadSafe ts) {
    this.id = id;
    this.delay = delayTime;
    this.monitor = m;
    this.ts = ts;
  }

  // Método executado pela thread
  public void run () {
    this.monitor.EntraConsumidor(this.id, this.ts);
    //System.out.println ("eis me aqui");
    //this.ts.printBuffer();
    this.ts.removeBuffer();
    this.monitor.SaiConsumidor(this.id);    
  }
}

//--------------------------------------------------------
// Produtor
class Produtor extends Thread {
  int id; //identificador da thread
  int delay; //atraso bobo...
  CP monitor; //objeto monitor para coordenar a lógica de execução das threads
  ThreadSafe ts;
  // Construtor
  Produtor (int id, int delayTime, CP m, ThreadSafe ts) {
    this.id = id;
    this.delay = delayTime;
    this.monitor = m;
    this.ts = ts;
  }

  // Método executado pela thread
  public void run () {
    Random rand = new Random();
    this.monitor.EntraProdutor(this.id, this.ts); 
    ts.insertBuffer(rand.nextInt(100000));
    this.monitor.SaiProdutor(this.id); 
  }
}

//--------------------------------------------------------
// Classe principal
class ConsumidorProdutor {
  static final int L = 5;
  static final int E = 5;

  public static void main (String[] args) {
    int i;
    CP monitor = new CP();            // Monitor (objeto compartilhado entre consumidores e produtores)
    Consumidor[] l = new Consumidor[L];       // Threads consumidores
    Produtor[] e = new Produtor[E];   // Threads produtores
    ThreadSafe ts = new ThreadSafe();

    //inicia o log de saida
    System.out.println ("import verificaAt2");
    System.out.println ("cp = verificaAt2.CP()");
    
    for (i=0; i<E; i++) {
      e[i] = new Produtor(i+1, (i+1)*500, monitor, ts);
      e[i].start(); 
   }
    for (i=0; i<L; i++) {
       l[i] = new Consumidor(i+1, (i+1)*500, monitor, ts);
       l[i].start(); 
    }
  }
}
