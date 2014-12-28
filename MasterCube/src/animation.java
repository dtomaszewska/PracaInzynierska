import java.awt.*;
import java.applet.*;

public class animation extends Applet implements Runnable
{
  private static final long serialVersionUID = -705555689585339843L;
  public Thread thread;
  Image image;
  Graphics gc;
  int x, y, r;    
  int w, h;   
   
  public void init()
  {
    thread = null;
    this.setBounds(0, 0, 500, 80);
    w=getSize().width;
    h=getSize().height;
    x=0;
    y = h/6;
    r = (int)(h/1.5);
    image=createImage(w,h);
    gc = image.getGraphics();  
    gc.fillRect(0, 0, w, h); 
    
  }
  
  public void start()
  {
    if (thread == null) {
      thread = new Thread(this);
      thread.start();
    }
  }

  public void stop()
  {
    if (thread != null){
      thread = null;
   }
  }

  public void run()
  {
    while(thread!=null)
    {
      gc = image.getGraphics();
      x ++;      
      if(x == w)  x = (int) (-h/1.5);
      
      gc.setColor(Color.white);
      gc.fillRect(0,0,w,h);    

      gc.setColor(Color.black);
      gc.fillOval(x, y, r, r);
      
      repaint();  
      
      try {
        Thread.sleep(5);
      } catch(InterruptedException e){}
    }
   }
   
   synchronized public void paint(Graphics g){
     g.drawImage(image,0,0,this);
   }
   
   synchronized public void update(Graphics g){
     paint(g);
   } 
}
