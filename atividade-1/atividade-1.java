

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
// Classe principal
class Atividade1 {

  public static void main (String[] args) {
    int i;   
    ThreadSafe ts = new ThreadSafe();

    //inicia o log de saida
    System.out.println ("import verificaatividade1");
    System.out.println ("ts = verificaatividade1.ThreadSafe()");
    int r= 2;
    for (i=0; i<1000000; i++) {
      if(i%r != 0){
        ts.removeBuffer(i%100000);
      }else{
        ts.insertBuffer(i%100000, i);
      }
      if(i%100000 == 0){
        r--;
        if(r == 1){
          r=3;
        }
      }
    }
    //ts.printBuffer();
    
  }
}
