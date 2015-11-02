public class Button {
  int x, y;
  int sz;
  color baseGray;
  color overGray;
  color pressGray;
  boolean over = false;
  boolean pressed = false;
  boolean already_pressed = false;
  int id;
  ButtonCallback callback;

  Button(int xp, int yp, int s, color b, color o, color p, int ident) {
    x = xp;
    y = yp;
    sz = s;
    id = ident;
    baseGray = b;
    overGray = o;
    pressGray = p;
  }

  void mouseEvent(MouseEvent event){
    
    if ((mouseX >= x) && (mouseX <= x+sz) && (mouseY >= y) && (mouseY <= y+sz)) {  
      if(!already_pressed)
        over = true;
      switch(event.getID()){
      case MouseEvent.MOUSE_PRESSED:
        pressed = true;
        already_pressed = true;
        break;
      case MouseEvent.MOUSE_RELEASED:
        pressed = false;
        callback.button_clicked(this);
        break;
      }
    } 
    else {
      already_pressed = false;
      over = false;
      pressed = false;
    }
  }

  void draw() {
    rectMode(CORNER);
    if (pressed == true) {
      fill(pressGray);
    } 
    else if (over == true) {
      fill(overGray);
    } 
    else {
      fill(baseGray);
    }
    stroke(255);
    rect(x, y, 30, 30);
  }
  
  void set_callback(ButtonCallback c){
    callback = c;
  }
    
}
