public class SecondaryFrame extends JFrame {
  //SecondaryApplet s;
  public SecondaryFrame() {
    setBounds(100,100,600,400);
    
    show();
    
    applet2 = new SecondaryApplet();
    add(applet2);
    applet2.init();
  }
}

public class SecondaryApplet extends PApplet {
  public void setup() {
    size(600, 400);
    noLoop();
  }

  public void draw() {
  }
}
