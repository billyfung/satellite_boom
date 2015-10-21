// The canvas class draws anything which should be part of the GUI.

class Canvas {
  private ArrayList buttons;
  Button b;

  //slider
  Slider slider_T,slider_Y;
  
  private PFont label_font;
 
  Canvas() {
     //button 
    buttons = new ArrayList();
    b = new Button(225, (int)(height*0.95), 30, color(100), color(255,0,0), color(0), 0);
    buttons.add(b);
    registerMouseEvent(b);
  
   //slider 
    slider_T = new Slider(20,height*0.95, 200, 20.0, Slider.LEFT, Slider.TOP, Slider.HORIZONTAL, 0, 1, 0.1, color(255, 255, 255), color(0, 0, 0));
    slider_T.setTickSpacing(0.1);
    slider_T.setPaintTicks(true);
    slider_T.setSnapToTicks(false);
    
    label_font = loadFont("ArialUnicodeMS-24.vlw");
    textFont(label_font);    
    register_callbacks();
  }
  
  void render_canvas() {

    pushMatrix();
      resetMatrix();
      
      // disable depth test to draw ontop of scene
      hint(DISABLE_DEPTH_TEST);  
      // reset the projection matrix
      beginCamera();
      resetMatrix();
      endCamera();
      // set ortho to match window coordinates to draw coordinates
      ortho(-width/2, width/2, -height/2, height/2, -10, 10);

      pushMatrix();
        // draw the current time
        scale(0.5);
        translate(-width, height*0.88, 0);
        
        text(current_date.toString(), 20, 0);
        //hint(ENABLE_DEPTH_TEST);
        
        //translate(0, -height*0.5, 0);
      popMatrix();

      pushMatrix();
        // draw framerate
        translate(-width/2.0, -height/2.0, 0);
        if(frameRate >= 100) {
          text((int)frameRate, width-40, height);
        } else {
          text((int)frameRate, width-25, height);
        }

        slider_T.run();
      
        b.draw();  
        slider_T.draw();
      popMatrix();

      hint(ENABLE_DEPTH_TEST);
   
    popMatrix();
  }
  
  // setup button callbacks here
  void register_callbacks() {
    ButtonCallback test = new ButtonCallback() {
      public void button_clicked(Button b) {
        // Do button stuff
        if(frame2.isShowing()){
          frame2.hide();
        }else{
          frame2.show();
        }
        //println(current_date.toString());
      }
    };
    b.set_callback(test);   
  }
}
