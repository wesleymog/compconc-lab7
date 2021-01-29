
// Monitor
class LE {
  private int leit, escr, buffer;  
  
  // Construtor
  LE() { 
     this.leit = 0; //leitores lendo (0 ou mais)
     this.escr = 0; //escritor escrevendo (0 ou 1)
  } 
  
  // Entrada para leitores
  public synchronized void EntraLeitor (int id) {
    try { 
      while (this.escr > 0) {
      //if (this.escr > 0) {
         System.out.println ("le.leitorBloqueado("+id+")");
         wait();  //bloqueia pela condicao logica da aplicacao 
      }
      this.leit++;  //registra que ha mais um leitor lendo
      System.out.println ("le.leitorLendo("+id+")");
    } catch (InterruptedException e) { }
  }
  
  // Saida para leitores
  public synchronized void SaiLeitor (int id) {
     this.leit--; //registra que um leitor saiu
     if (this.leit == 0) 
           this.notify(); //libera escritor (caso exista escritor bloqueado)
     System.out.println ("le.leitorSaindo("+id+")");
  }
  
  // Entrada para escritores
  public synchronized void EntraEscritor (int id) {
    try { 
      while ((this.leit > 0) || (this.escr > 0)) {
      //if ((this.leit > 0) || (this.escr > 0)) {
         System.out.println ("le.escritorBloqueado("+id+")");
         wait();  //bloqueia pela condicao logica da aplicacao 
      }
      this.escr++; //registra que ha um escritor escrevendo
      System.out.println ("le.escritorEscrevendo("+id+")");
    } catch (InterruptedException e) { }
  }
  
  // Saida para escritores
  public synchronized void SaiEscritor (int id) {
     this.escr--; //registra que o escritor saiu
     notifyAll(); //libera leitores e escritores (caso existam leitores ou escritores bloqueados)
     System.out.println ("le.escritorSaindo("+id+")");
  }
}

class ThreadSafe{
  private int buffer[];
    // Construtor
  ThreadSafe() { 
     this.buffer = new int [100000];
  } 

  public void printBuffer(){
    String bufferstr = "";
    bufferstr += "buffer = [ ";
    int i=0;
    while(i<100000){
      bufferstr += this.buffer[i]+", ";
      i++;
    }
    bufferstr += "]";
    System.out.println (bufferstr);
  }

  public void insertBuffer(int i, int value){
    if(i <100000 && this.buffer[i] == 0 ){
      this.buffer[i] = value;
      System.out.println ("ts.insertThreadSafe("+i+","+value+",True)");
    }else{
      System.out.println ("ts.insertThreadSafe("+i+","+value+",False)");
    }
  }

  public void removeBuffer(int i){
    if(i <100000 && this.buffer[i] != 0){
      this.buffer[i] = 0;
      System.out.println ("ts.removeThreadSafe("+i+",True)");
    }else{
      System.out.println ("ts.removeThreadSafe("+i+",False)");
    }
  }



}

//--------------------------------------------------------
// Leitor
class Leitor extends Thread {
  int id; //identificador da thread
  int delay; //atraso bobo
  LE monitor;//objeto monitor para coordenar a lógica de execução das threads
  ThreadSafe ts;
  // Construtor
  Leitor (int id, int delayTime, LE m, ThreadSafe ts) {
    this.id = id;
    this.delay = delayTime;
    this.monitor = m;
    this.ts = ts;
  }

  // Método executado pela thread
  public void run () {
    try {
      for (;;) {
        this.monitor.EntraLeitor(this.id);
        this.ts.insertBuffer
        this.monitor.SaiLeitor(this.id);
        sleep(this.delay); 
      }
    } catch (InterruptedException e) { return; }
  }
}

//--------------------------------------------------------
// Escritor
class Escritor extends Thread {
  int id; //identificador da thread
  int delay; //atraso bobo...
  LE monitor; //objeto monitor para coordenar a lógica de execução das threads

  // Construtor
  Escritor (int id, int delayTime, LE m) {
    this.id = id;
    this.delay = delayTime;
    this.monitor = m;
  }

  // Método executado pela thread
  public void run () {
    double j=777777777.7, i;
    try {
      for (;;) {
        this.monitor.EntraEscritor(this.id); 
        for (i=0; i<100000000; i++) {j=j/2;} //...loop bobo para simbolizar o tempo de escrita
        this.monitor.SaiEscritor(this.id); 
        sleep(this.delay); //atraso bobo...
      }
    } catch (InterruptedException e) { return; }
  }
}

//--------------------------------------------------------
// Classe principal
class LeitorEscritor {
  static final int L = 4;
  static final int E = 3;

  public static void main (String[] args) {
    int i;
    LE monitor = new LE();            // Monitor (objeto compartilhado entre leitores e escritores)
    Leitor[] l = new Leitor[L];       // Threads leitores
    Escritor[] e = new Escritor[E];   // Threads escritores
    ThreadSafe ts = new ThreadSafe();

    //inicia o log de saida
    System.out.println ("import verificaLE");
    System.out.println ("le = verificaLE.LE()");
    
    for (i=0; i<L; i++) {
       l[i] = new Leitor(i+1, (i+1)*500, monitor, ts);
       l[i].start(); 
    }
    for (i=0; i<E; i++) {
       e[i] = new Escritor(i+1, (i+1)*500, monitor, ts);
       e[i].start(); 
    }
  }
}
