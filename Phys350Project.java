import processing.core.*; 
import processing.xml.*; 

import javax.media.opengl.*; 
import processing.opengl.*; 
import peasy.*; 
import java.util.Date; 
import java.util.Calendar; 
import codeanticode.glgraphics.*; 
import objimp.*; 
import peasy.org.apache.commons.math.geometry.CardanEulerSingularityException; 
import peasy.org.apache.commons.math.geometry.Vector3D; 
import peasy.org.apache.commons.math.geometry.Rotation; 
import peasy.org.apache.commons.math.geometry.RotationOrder; 
import javax.swing.JFrame; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class Phys350Project extends PApplet {














int tick_count = 0; 

// Time stuff
//Time is in UTC in miliseconds since Jan 1 1970
Date start_time = new Date();
long current_time = start_time.getTime();
Date current_date = new Date(current_time); // used to convert to a string representation
Date end_time = new Date();
GregorianCalendar cal = new GregorianCalendar();

// References to global objects
Config config;
TrigApprox trig  = new TrigApprox();
ArrayList objects;
Viewport viewport;
Earth earth; // reference to earth object
Skybox skybox;
Sun sun;
GLGraphics renderer;
GL gl_object;
Canvas canvas;

int current_viewport = 0;

boolean bDataPointsCalculated = false;

// Secondary window (used for graphing)
SecondaryFrame frame2 = new SecondaryFrame();
SecondaryApplet applet2;
int[] graph = new int[600];

// File i/o
PrintWriter output;

// Graphing stuff
int satelliteIndex;
double satAngle;
PFont text_font_LG;
PFont text_font_SM;
float tickSum = 0;

// Laser
boolean enableLaser = false;
public void setup() {
  frame2.hide();
  size(800,600, GLConstants.GLGRAPHICS);
  frameRate(60);
  
  // Create global objects
  config = new Config();
  viewport = new Viewport();
  skybox = new Skybox();
  canvas = new Canvas();
  
  // Create the object list
  objects = new ArrayList();
  earth = new Earth(new Vector3D(0,0,0), new Vector3D(0,0,0), new Vector3D(0,0,0), config.MASS_EARTH, config.RADIUS_EARTH,"world32k.jpg");
  objects.add(earth);
  objects.add(new Moon(new Vector3D(0,Config.MOON_ORBIT_RADIUS,0), new Vector3D(0,0,1.022f), new Vector3D(0,0,0), Config.MASS_MOON, Config.RADIUS_MOON, "moonmap2k.jpg"));

  Vector3D temp_sat_pos = new Vector3D();
  Vector3D temp_sat_vel = new Vector3D();
  JavaSucksReturnTwo a = OrbitalElements.getParameters(temp_sat_pos, temp_sat_vel);
  temp_sat_pos = a.a;
  temp_sat_vel = a.b;

  /*
  // Test case #1-1, initial rotation about x axis (note: +x is right, -x is left)
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(1,0,0), PI/8);
  Vector3D temp_sat_ang_mom = new Vector3D();
  */
  
  /*
  // Test case #1-2, initial rotation about y axis (note: +y is down, -y is up)
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(0,1,0), PI/8);
  Vector3D temp_sat_ang_mom = new Vector3D();
  */
  
  /*
  // Test case #1-3, initial rotation about z axis (note: +z is into the page, -z is out of the page)
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(0,0,1), PI/8);
  Vector3D temp_sat_ang_mom = new Vector3D();
  */
  
  /*
  // Test case #2-1, angular momentum about x axis
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0.004,0,0);
  */
  
  /*
  // Test case #2-2, angular momentum about y axis
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0,0.0004,0);
  */
  
  /*
  // Test case #2-3, angular momentum about z axis
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0.004);
  */
  
  /*
  // Test case #2-4, angular momentum about x axis with initial rotation
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(0,1,0), PI/4);
  Vector3D temp_sat_ang_mom = new Vector3D(0.004, 0, 0);
  */
  
  /*
  // Test case #2-5, angular momentum about y axis with initial rotation
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(0,0,1), PI/4);
  Vector3D temp_sat_ang_mom = new Vector3D(0, 0.0004, 0);
  */
  
  /*
  // Test case #3-1, angular momentum about both x and z axes
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0.004,0,0.004);
  */
  
  /*
  // Test case #3-2, angular momentum about both y and z axes
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0, 0.0004, 0.0004);
  */
  
  /*
  // Test case #4-1, torque x
  // STATUS: working
  // add constant torque in Satellite.pde
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #4-2, torque y
  // STATUS: working
  // add constant torque in Satellite.pde
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #4-3, torque z
  // STATUS: working
  // add constant torque in Satellite.pde
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #4-4, torque x with initial rotation
  // STATUS: working
  // add constant torque in Satellite.pde
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(1,1,1), PI/4);
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #5-1, gravity x
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(1,0,0), PI/4);
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #5-2, gravity y
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(0,1,0), PI/4).times(new Quaternion(new Vector3D(0,0,1), PI/2));
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #5-3, gravity z
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(1,0,0), PI/4);
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #5-3, gravity mike broke everything
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(0,1,0), PI/4);
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #5-4, gravity flat
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion();
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  /*
  // Test case #5-4, gravity x and y
  // STATUS: working
  Quaternion temp_sat_rot = new Quaternion(new Vector3D(1,0,1), PI/4);
  Vector3D temp_sat_ang_mom = new Vector3D(0,0,0);
  */
  
  // Test case #6-1, The Final Product
  // STATUS: working!!!!!
  Quaternion temp_sat_rot = new Quaternion();
  //Vector3D temp_sat_ang_mom = new Vector3D(0.0005,0.0005,0.0005);
  Vector3D temp_sat_ang_mom = new Vector3D(0.0f,0.0f,0.0f);
  
  InertiaTensor inertiaTensor = new InertiaTensor(
    0.114f, 0.00000f, 0.00000f,
    0.00000f, 0.00451f, 0.00000f,
    0.00000f, 0.00000f, 0.114f
  );
  
  //inertiaTensorTest(inertiaTensor);
  satelliteIndex = objects.size();
  objects.add(new Satellite(temp_sat_pos, temp_sat_vel, new Vector3D(0,0,0), temp_sat_rot, temp_sat_ang_mom, new Vector3D(), config.MASS_SATELLITE, inertiaTensor));
    sun = new Sun(new Vector3D(Config.EARTH_ORBIT_RADIUS,0,0), new Vector3D(0,29.78f,0), new Vector3D(0,0,0), Config.MASS_SUN, Config.RADIUS_SUN, "sun32.bmp");
  objects.add(sun);
  
  // Set default view target
  viewport.setOrigin(earth);
  cal.set(2011, 2, 9, 23, 0, 0);
  end_time = cal.getTime(); 
  
  // Create a secondary display
  //frame2.setTitle("Real-time Graphical Output");
  frame2.setResizable(false);
  applet2.background(255);
  
  // Zero the graph (to be fixed)
  for(int i = 0; i<graph.length; i++){ 
    graph[i] = 20;
  }
  
  // Setup the fonts
  text_font_LG = loadFont("ArialUnicodeMS-20.vlw");
  text_font_SM = loadFont("ArialUnicodeMS-14.vlw");
  
  //File i/o stuff
  output = createWriter("angleoutput.txt");
  output.println("#Tick" + "  " + "Sat. Angle (rad)" + "Sat. Angle (deg)" + "Sat. Angle (deg, graph)");
}

//float tickSum = 100;
public void draw() {  
  // Get tick rate from the slider
  Config.TICK_RATE = (float)canvas.slider_T.getValue()/(canvas.slider_T.getMaximum()-canvas.slider_T.getMinimum())*10/30.f;
  current_time += (long)(config.TICK_RATE*1000*30);
  current_date.setTime(current_time);
  if(config.bRealTime) {
    for (int i=0; i<30; i++) {
      update_physics();
    }
   
    render_scene();
  } else {
    if(!bDataPointsCalculated) {
      calculate_data();
    }
    if(tick_count < config.numDataPoints)
    {
      render_scene();
    }
  }
  
  perspective(PI/3.0f, PApplet.parseFloat(width)/PApplet.parseFloat(height), 5, Config.FAR_Z_CLIP);     
  tick_count++;  
  
  tickSum += Config.TICK_RATE;
  if(tick_count%2 == 0){
    // Write to output file
    if(tick_count%100 == 0){
      // print to output and flush to file
      printToFile(true);
    }else{
      // just print to output
      printToFile(false);
    }
  }
}

public void update_physics() {
  
  // tick all of the objects
  for(int i = 0; i < objects.size(); i++) {
    Object o = (Object)objects.get(i);
    o.tick(config.TICK_RATE);
  }
  
}  

public void render_scene()
{
  renderer = (GLGraphics)g;
   
  gl_object = renderer.beginGL();
  scale(1.f,1.f,-1.f);
  background(0);

  noFill();
  stroke(255,200);
  strokeWeight(2);
  
  skybox.render_skybox();
  pointLight(200,200,200,(float)sun.getPosition().getX(), (float)sun.getPosition().getY(), (float)sun.getPosition().getZ());
  ambientLight(150,150,150);
  
  // render all the objects
  for(int i = 0; i < objects.size(); i++) {
    Object o = (Object)objects.get(i);
    if(i == satelliteIndex){
      if(enableLaser == true){
        Satellite sat = (Satellite)objects.get(satelliteIndex);
        Vector3D surf = sat.getPosition().normalize().scalarMultiply(Config.RADIUS_EARTH);
        stroke(255,0,0);
        strokeWeight(4);
        pointLight(255,0,0,(float)sat.getPosition().getX(), (float)sat.getPosition().getY(), (float)sat.getPosition().getZ());
        pointLight(255,0,0,(float)surf.getX(), (float)surf.getY(), (float)surf.getZ());
        
        line((float)sat.getPosition().getX(), (float)sat.getPosition().getY(), (float)sat.getPosition().getZ(), (float)surf.getX(), (float)surf.getY(), (float)surf.getZ());
        strokeWeight(1);
        noStroke();
      }
    }
    o.render();
  }
  
  canvas.render_canvas();  
  viewport.renderViewport();
  renderer.endGL();
}

public void keyPressed() {
    if(key == '1') {
      viewport.setOrigin(((Object)objects.get(0)));
    }
    if(key == '2') {
      viewport.setOrigin(((Object)objects.get(1)));
    }
    if(key == '3') {
      viewport.setOrigin(((Object)objects.get(2)));
    }
    if(key == '4') {
      viewport.setOrigin(((Object)objects.get(3)));
    }
    if(key == 'l') {
      if(enableLaser == true){
        enableLaser = false;
      }else{
        enableLaser = true;
      }
    }
}  

public void calculate_data()
{
  tick_count = 0;
  current_time = 0;
  while(tick_count < config.numDataPoints) {
    update_physics();
    tick_count++;
  }
  tick_count = 0;
  bDataPointsCalculated = true;
}
public void printToFile(boolean flsh) {
  Satellite sat = (Satellite)objects.get(satelliteIndex);
  double satAngle = sat.find_angle();
  
  // Update secondary window (temporarily placed here)
  secondaryDraw(satAngle);
  
  output.println(tickSum + "  " + satAngle + "  " + satAngle*180/PI + "  " + (int)(satAngle*180/PI));
  if(flsh == true){
    output.flush();
  }
}

// Dummy count for testing the secondary window (to be removed)
//double tempAngle = 0;
public void secondaryDraw(double angle) {
  applet2.background(255);
  applet2.noFill();
  applet2.stroke(0);
  
  // Draw axis
  // y axis
  applet2.line(26,20,26,applet2.height-20);
  // x axis
  applet2.line(0, applet2.height - 20, applet2.width, applet2.height - 20);
  // 180 deg line
  applet2.line(0, 20, applet2.width, 20);  
  
  //Graph lines in increments of 20 degrees
  applet2.stroke(210);
  for(int i = 0; i < 8; i++){
   applet2.line(26, i*37 + 20 + 37, applet2.width, i*37 + 20 + 37);
  }
  
  applet2.textFont(text_font_LG); 
  applet2.fill(100);
  applet2.textAlign(CENTER);
  applet2.text("Real-Time Satellite Angle (degrees)", applet2.width/2.0f, 20);
  applet2.textFont(text_font_SM);
  applet2.textAlign(RIGHT);
  applet2.text("180", 24, 19);
  applet2.text("0", 24, applet2.height-21);
  applet2.textAlign(LEFT);
  applet2.text("A \nN \nG \nL \nE", 5, 150);  
  applet2.stroke(200,40,40);
  // Note: 1.84 is the scaling factor to get the angle to fit in between the 0 and 180 degree lines
  int degSatAngle = (int)(1.84f*angle*180/PI);
  graph[graph.length-1] = (applet2.height - 20) - degSatAngle;
  //println("Graph at: " + graph[graph.length-1] + ", Current Angle: " + (int)(angle*180/PI));
    for(int i = 26; i<graph.length; i++){
  //for(int i = graph.length - 1; i >= 0; i--){
    if(i<graph.length-1){
      if(i >= 600-tick_count){
        applet2.line(i,graph[i],i+1,graph[i+1]);
      }
      graph[i] = graph[i+1];        
    }
  }
  
  applet2.textAlign(RIGHT);
  applet2.text((int)(angle*180/PI), 590, graph[graph.length-10] - 10);
  
  applet2.redraw();
}

public void stop(){
  // Ensure the output file is closed properly when the program quits
  output.flush();
  output.close();
  super.stop();
}

public void inertiaTensorTest(InertiaTensor i) {
  // default
  //print("default:   " + makeString(i.matrix) + "\n");
  
  // 90 degrees about axes
  //print("90x:       " + makeString(i.rotatedMatrix(new Quaternion(new Vector3D(1,0,0), PI/2))) + "\n");
  //print("90y:       " + makeString(i.rotatedMatrix(new Quaternion(new Vector3D(0,1,0), PI/2))) + "\n");
  //print("90z:       " + makeString(i.rotatedMatrix(new Quaternion(new Vector3D(0,0,1), PI/2))) + "\n");
  
  // 45 degress about axes
  //print("45x:       " + makeString(i.rotatedMatrix(new Quaternion(new Vector3D(1,0,0), PI/4))) + "\n");
  //print("45y:       " + makeString(i.rotatedMatrix(new Quaternion(new Vector3D(0,1,0), PI/4))) + "\n");
  //print("45z:       " + makeString(i.rotatedMatrix(new Quaternion(new Vector3D(0,0,1), PI/4))) + "\n");
  
  // quaternion generating test
  //print("q->+x 1:   " + makeString(new Quaternion(new Vector3D(0,0,1), -PI/2)) + "\n");
  //print("q->+x 2:   " + makeString(new Quaternion(new Vector3D(0,1,0), new Vector3D(1,0,0))) + "\n");
  //print("q->-x 1:   " + makeString(new Quaternion(new Vector3D(0,0,1), PI/2)) + "\n");
  //print("q->-x 2:   " + makeString(new Quaternion(new Vector3D(0,1,0), new Vector3D(-1,0,0))) + "\n");
  
  //print("q->+z 1:   " + makeString(new Quaternion(new Vector3D(1,0,0), PI/2)) + "\n");
  //print("q->+z 2:   " + makeString(new Quaternion(new Vector3D(0,1,0), new Vector3D(0,0,1))) + "\n");
  //print("q->-z 1:   " + makeString(new Quaternion(new Vector3D(1,0,0), -PI/2)) + "\n");
  //print("q->-z 2:   " + makeString(new Quaternion(new Vector3D(0,1,0), new Vector3D(0,0,-1))) + "\n");
  
  // quaternion generating test, 45 degrees only
  //print("q->+45z 1: " + makeString(new Quaternion(new Vector3D(1,0,0), PI/4)) + "\n");
  //print("q->+45z 2: " + makeString(new Quaternion(new Vector3D(0,1,0), new Vector3D(0,1,1))) + "\n");
  
  // quaternion generating, 90 degrees + 45 degrees
  //print("q->+45z 1: " + 
  
  // rotation matrix test, right handed and left handed
  //print("rR->+90x:  " + makeString(new Quaternion(new Vector3D(1,0,0), PI/2).getRightMatrix()) + "\n");
  //print("rR->+90y:  " + makeString(new Quaternion(new Vector3D(0,1,0), PI/2).getRightMatrix()) + "\n");
  //print("rR->+90z:  " + makeString(new Quaternion(new Vector3D(0,0,1), PI/2).getRightMatrix()) + "\n");
  //print("rL->+90x:  " + makeString(new Quaternion(new Vector3D(1,0,0), PI/2).getLeftMatrix()) + "\n");
  //print("rL->+90y:  " + makeString(new Quaternion(new Vector3D(0,1,0), PI/2).getLeftMatrix()) + "\n");
  //print("rL->+90z:  " + makeString(new Quaternion(new Vector3D(0,0,1), PI/2).getLeftMatrix()) + "\n");
  //print("rR->-45x:  " + makeString(new Quaternion(new Vector3D(1,0,0), -PI/4).getRightMatrix()) + "\n");
  //print("rR->-45y:  " + makeString(new Quaternion(new Vector3D(0,1,0), -PI/4).getRightMatrix()) + "\n");
  //print("rR->-45z:  " + makeString(new Quaternion(new Vector3D(0,0,1), -PI/4).getRightMatrix()) + "\n");
  //print("rL->-45x:  " + makeString(new Quaternion(new Vector3D(1,0,0), -PI/4).getLeftMatrix()) + "\n");
  //print("rL->-45y:  " + makeString(new Quaternion(new Vector3D(0,1,0), -PI/4).getLeftMatrix()) + "\n");
  //print("rL->-45z:  " + makeString(new Quaternion(new Vector3D(0,0,1), -PI/4).getLeftMatrix()) + "\n");

  // rotation matrix mult test, right and left handed
  double[][] rR = new Quaternion(new Vector3D(1,1,1), PI/2).getRightMatrix();
  double[][] rL = new Quaternion(new Vector3D(1,1,1), PI/2).getLeftMatrix();
  //print("rR'*I*rR:   " + makeString(multMatrix(transposeMatrix(rR), i.matrix, rR)) + "\n");
  //print("rL*I*rL':   " + makeString(multMatrix(rL, i.matrix, transposeMatrix(rL))) + "\n");


  Boolean a = true;
  while(a) {};
}


class AbstractSlider
{
  static final int HORIZONTAL = 0;
  static final int VERTICAL = 1;

  static final int LEFT = 0;
  static final int RIGHT = 1;
  static final int CENTER = 2;
  static final int TOP = 3;
  static final int BOTTOM = 4;

  float min, max, value, extent;
  float old_min, old_max, old_value, old_extent;

  int orientation;
  float x, y, width, height;
  int color1, color2;
  int halign, valign;
  boolean enabled = true;

  float left, top;
  boolean enablable, degraded;
  int c1, c2;
  float[] knob_size = new float[2];
  float[] comp = new float[2];
  float[] m_comp = new float[2];
  float offset;
  boolean armed;
  boolean over, over_track, over_knob;
  boolean knob_locked;

  AbstractSlider(float min, float max, float value, float extent)
  {
    // contraints must be satisfied: min <= value <= value+extent <= max

    if (!(max >= min && value >= min && (value + extent) >= value && (value + extent) <= max))
    {
      throw new IllegalArgumentException(getClass() + ": invalid range properties");
    }

    this.min = old_min = min;
    this.max = old_max = max;
    this.value = old_value = value;
    this.extent = old_extent = extent;
  }

  public float getMinimum()
  {
    return min;
  }

  public float getMaximum()
  {
    return max;
  }

  public float getValue()
  {
    return value;
  }

  public float getExtent()
  {
    return extent;
  }

  public void setMinimum(float n)
  {
    max = max(n, max);
    value = max(n, value);
    extent = min(max - value, extent);
    min = n;
    updateRangeProperties();
  }

  public void setMaximum(float n)
  {
    min = min(n, min);
    extent = min(n - min, extent);
    value = min(n - extent, value);
    max = n;
    updateRangeProperties();
  }

  public void setValue(float n)
  {
    value = constrain(n, min, max - extent);
    updateRangeProperties();
  }

  public void setExtent(float n)
  {
    extent = constrain(n, 0.0f, max - value);
    updateRangeProperties();
  }

  public void setColors(int color1, int color2)
  {
    this.color1 = color1;
    this.color2 = color2;
  }

  public void setLocation(float x, float y)
  {
    setBounds(x, y, width, height);
  }

  public void setSize(float width, float height)
  {
    setBounds(x, y, width, height);
  }

  public void setBounds(float x, float y, float width, float height)
  {
    this.left = x - (halign == LEFT ? 0.0f : (halign == RIGHT ? width : width / 2.0f));;
    this.top = y - (valign == TOP ? 0.0f : (valign == BOTTOM ? height : height / 2.0f));
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    recalc();
  }

  public boolean getEnabled()
  {
    return enabled;
  }

  public void setEnabled(boolean b)
  {
    enabled = b;
  }

  public void updateRangeProperties()
  {
    enablable = getIsEnablable();
    rangePropertiesChanged();
    updateComp();
    updateExtent();
  }

  public boolean getIsEnablable()  // always overriden...
  {
    return true;
  }

  public void updateComp() {}

  public void updateExtent() {}

  public void recalc() {}

  public void rangePropertiesChanged()
  {
    // this is the place to implement an event-posting system...
  }
}

public class Button {
  int x, y;
  int sz;
  int baseGray;
  int overGray;
  int pressGray;
  boolean over = false;
  boolean pressed = false;
  boolean already_pressed = false;
  int id;
  ButtonCallback callback;

  Button(int xp, int yp, int s, int b, int o, int p, int ident) {
    x = xp;
    y = yp;
    sz = s;
    id = ident;
    baseGray = b;
    overGray = o;
    pressGray = p;
  }

  public void mouseEvent(MouseEvent event){
    
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

  public void draw() {
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
  
  public void set_callback(ButtonCallback c){
    callback = c;
  }
    
}
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
    b = new Button(225, (int)(height*0.95f), 30, color(100), color(255,0,0), color(0), 0);
    buttons.add(b);
    registerMouseEvent(b);
  
   //slider 
    slider_T = new Slider(20,height*0.95f, 200, 20.0f, Slider.LEFT, Slider.TOP, Slider.HORIZONTAL, 0, 1, 0.1f, color(255, 255, 255), color(0, 0, 0));
    slider_T.setTickSpacing(0.1f);
    slider_T.setPaintTicks(true);
    slider_T.setSnapToTicks(false);
    
    label_font = loadFont("ArialUnicodeMS-24.vlw");
    textFont(label_font);    
    register_callbacks();
  }
  
  public void render_canvas() {

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
        scale(0.5f);
        translate(-width, height*0.88f, 0);
        
        text(current_date.toString(), 20, 0);
        //hint(ENABLE_DEPTH_TEST);
        
        //translate(0, -height*0.5, 0);
      popMatrix();

      pushMatrix();
        // draw framerate
        translate(-width/2.0f, -height/2.0f, 0);
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
  public void register_callbacks() {
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
static class Config {
  static boolean bRealTime; // true if the data points are NOT precalculated
  static int numDataPoints; // number of data points to precalculate
  static boolean bShowOrbitPrediction; // true if you want to show the orbit prediction line. bRealTime MUST be false!
  
  static final float GRAVITY_CONSTANT = 6.67428e-20f;
  static final float MASS_EARTH = 5.9737e24f; //kg
  static final float RADIUS_EARTH = 6371; //km
  static final float RADIUS_MOON = 1737; //km
  static final float MASS_MOON = 7.3477e22f; //kg
  static final float MOON_ORBIT_RADIUS = 363104; //km. Assumes spherical orbit
  static final float RADIUS_SUN = 6.955e5f; //km
  static final float EARTH_ORBIT_RADIUS = 149598261; //km
  static final float MASS_SUN = 1.9891e30f; //kg
  static float TICK_RATE = 1;
  
  static final float MASS_SATELLITE = 1.5f;
  
  static final int FAR_Z_CLIP = 500000000;
  
  Config() {
    setDefaults();
  }
  
  public static void setDefaults() {
    bRealTime = true;
    numDataPoints = 25000;
    bShowOrbitPrediction = true;
  }
}
  
// Class which holds generic calculated data

abstract class DataContainer {
  public abstract Vector3D getPosition();
  public abstract Vector3D getVelocity();
  public abstract Vector3D getAcceleration();
  
  public abstract Quaternion getRotation();
  public abstract Vector3D getAngularMomentum();
  public abstract Vector3D getTorque();
  
  public abstract float getTime();
  
  public abstract void setPosition(Vector3D new_position);
  public abstract void setVelocity(Vector3D new_velocity);
  public abstract void setAcceleration(Vector3D new_acceleration);
  
  public abstract void setRotation(Quaternion new_rotation);
  public abstract void setAngularMomentum(Vector3D new_angularMomentum);
  public abstract void setTorque(Vector3D new_torque);
  
  public abstract void setTime(float new_time);
  
  public abstract void saveValues();
}
class Earth extends Planet {
  
  Earth(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass, float r, String tex_name) {
    
    super(pos_new, vel_new, acc_new, mass, r, tex_name);
    solver = new ODEEulerSolver();
    radius = r;
    bounding_sphere_radius = r;

  }
}
class InertiaTensor {
  double matrix[][];
  double invertedMatrix[][];
  static final double rows = 3;
  static final double cols = 3;

  InertiaTensor(double matrix_[][]) {
    matrix = new double[3][3];
    matrix[0][0] = matrix_[0][0];
    matrix[0][1] = matrix_[0][1];
    matrix[0][2] = matrix_[0][2];
    matrix[1][0] = matrix_[1][0];
    matrix[1][1] = matrix_[1][1];
    matrix[1][2] = matrix_[1][2];
    matrix[2][0] = matrix_[2][0];
    matrix[2][1] = matrix_[2][1];
    matrix[2][2] = matrix_[2][2];
    
    invertedMatrix = invertMatrix(matrix);
  }
  
  InertiaTensor(double xx, double xy, double xz, double yx, double yy, double yz, double zx, double zy, double zz) {
    matrix = new double[3][3];
    matrix[0][0] = xx;
    matrix[0][1] = xy;
    matrix[0][2] = xz;
    matrix[1][0] = yx;
    matrix[1][1] = yy;
    matrix[1][2] = yz;
    matrix[2][0] = zx;
    matrix[2][1] = zy;
    matrix[2][2] = zz;
    
    invertedMatrix = invertMatrix(matrix);
  }
  
  public double xx() {
    return matrix[0][0];
  }
  
  public double xy() {
    return matrix[0][1];
  }
  
  public double xz() {
    return matrix[0][2];
  }
  
  public double yx() {
    return matrix[1][0];
  }
  
  public double yy() {
    return matrix[1][1];
  }
  
  public double yz() {
    return matrix[1][2];
  }
  
  public double zx() {
    return matrix[2][0];
  }
  
  public double zy() {
    return matrix[2][1];
  }
  
  public double zz() {
    return matrix[2][2];
  }
  
  public double[][] rotatedMatrix(Quaternion r) {
    // Apply a rotation to the inertia tensor
    // this is done by I' = Rl*I*trans(Rl) where Rl is the left rotation matrix of the quaternion
    // trans(Rr)*I*Rr would also work where Rr is the right rotation matrix
    double rotMatrix[][] = r.getRightMatrix();
    return multMatrix(rotMatrix, matrix, transposeMatrix(rotMatrix));
  }
  
  public double[][] inverseRotatedMatrix(Quaternion r) {
    // see above
    double rotMatrix[][] = r.getRightMatrix();
    return multMatrix(rotMatrix, invertedMatrix, transposeMatrix(rotMatrix));
  }
  
  public Vector3D mult(Vector3D vec) {
    return multMatrix(matrix, vec);
  }
  
  public Vector3D multInverse(Vector3D vec) {
    return multMatrix(invertedMatrix, vec);
  }
  
  public String toString() {
    return makeString(matrix);
  }
}
// Put any interfaces used to implement callbacks in here

  public interface ButtonCallback {
    public void button_clicked(Button b);
  }
static class JavaSucksReturnTwo {
  public Vector3D a;
  public Vector3D b;
}
public double[][] multMatrix(double[][] m1, double[][] m2) {
  double[][] res = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = 0;
      for (int k=0; k<3; k++) {
        res[i][j] += m1[i][k] * m2[k][j];
      }
    }
  }
  return res;
}

public double[][] addMatrix(double[][] m1, double[][] m2) {
  double[][] res = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = m1[i][j] + m2[i][j];
    }
  }
  return res;
}

public double[][] multMatrix(double[][] m1, double s) {
  double[][] res = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = m1[i][j]*s;
    }
  }
  return res;
}

public Vector3D multMatrix(double[][] m, Vector3D v) {
  Vector3D res = new Vector3D(
    m[0][0] * v.getX() + m[0][1] * v.getY() + m[0][2] * v.getZ(),
    m[1][0] * v.getX() + m[1][1] * v.getY() + m[1][2] * v.getZ(),
    m[2][0] * v.getX() + m[2][1] * v.getY() + m[2][2] * v.getZ()
  );
  return res;
}

public Vector3D multMatrix(Vector3D v, double[][] m) {
  Vector3D res = new Vector3D(
    v.getX() * m[0][0] + v.getY() * m[1][0] + v.getZ() * m[2][0],
    v.getX() * m[0][1] + v.getY() * m[1][1] + v.getZ() * m[2][1],
    v.getX() * m[0][2] + v.getY() * m[1][2] + v.getZ() * m[2][2]
  );
  /*
  for (int j=0; j<3; j++) {
    res[0][j] = v.getX() * (m[0][j] + m[1][j] + m[2][j]);
    res[1][j] = v.getY() * (m[0][j] + m[1][j] + m[2][j]);
    res[2][j] = v.getZ() * (m[0][j] + m[1][j] + m[2][j]);
  }
  */
  return res;
}

public double[][] multMatrix(double[][] m1, double[][] m2, double[][] m3) {
  return multMatrix(m1, multMatrix(m2, m3));
}

public double[][] orthogonalizeMatrix(double[][] m) {
  Vector3D v1 = new Vector3D(m[0][0], m[0][1], m[0][2]);
  Vector3D v2 = new Vector3D(m[1][0], m[1][1], m[1][2]);
  Vector3D v3 = Vector3D.crossProduct(v1, v2).normalize();
  v2 = Vector3D.crossProduct(v3, v1).normalize();
  v1 = v1.normalize();
  
  double[][] res = new double[3][3];
  res[0][0] = v1.getX();
  res[0][1] = v1.getY();
  res[0][2] = v1.getZ();
  res[1][0] = v2.getX();
  res[1][1] = v2.getY();
  res[1][2] = v2.getZ();
  res[2][0] = v3.getX();
  res[2][1] = v3.getY();
  res[2][2] = v3.getZ();
  return res;
  
  /*
  Vector3D v1 = new Vector3D(m[0][0], m[1][0], m[2][0]);
  Vector3D v2 = new Vector3D(m[0][1], m[1][1], m[2][1]);
  Vector3D v3 = Vector3D.crossProduct(v1, v2).normalize();
  v2 = Vector3D.crossProduct(v3, v1).normalize();
  v1 = v1.normalize();
  
  double[][] res = new double[3][3];
  res[0][0] = v1.getX();
  res[0][1] = v2.getX();
  res[0][2] = v3.getX();
  res[1][0] = v1.getY();
  res[1][1] = v2.getY();
  res[1][2] = v3.getY();
  res[2][0] = v1.getZ();
  res[2][1] = v2.getZ();
  res[2][2] = v3.getZ();
  return res;
  */
}

/*
// multiply 4x4 matrix by a vector
double[] multMatrix4(double[][] m, double[] v) {
  double[] res = new double[4];
  res[0] = m[0][0] * v[0] + m[0][1] * v[1] + m[0][2] * v[2] + m[0][3] * v[3];
  res[1] = m[1][0] * v[0] + m[1][1] * v[1] + m[1][2] * v[2] + m[1][3] * v[3];
  res[2] = m[2][0] * v[0] + m[2][1] * v[1] + m[2][2] * v[2] + m[2][3] * v[3];
  res[3] = m[3][0] * v[0] + m[3][1] * v[1] + m[3][2] * v[2] + m[3][3] * v[3];
  return res;
}
*/

public double[][] transposeMatrix(double[][] m) {
  double res[][] = new double[3][3];
  for (int i=0; i<3; i++) {
    for (int j=0; j<3; j++) {
      res[i][j] = m[j][i];
    }
  }
  return res;
}

public double determinantMatrix(double[][] m) {
  double det = m[0][0] * (m[1][1]*m[2][2] - m[1][2] * m[2][1]);
  det -= m[0][1] * (m[1][0]*m[2][2] - m[1][2] * m[2][0]);
  det += m[0][2] * (m[1][0]*m[2][1] - m[1][1] * m[2][0]);
  return det;
}

public double[][] invertMatrix(double[][] m) {
   double det = determinantMatrix(m);
   double tmp = 1.0f / det;
   
   double res[][] = new double[3][3];
   
   res[0][0] = tmp * (m[1][1] * m[2][2] - m[1][2] * m[2][1]);
   res[1][0] = tmp * (m[1][2] * m[2][0] - m[1][0] * m[2][2]);
   res[2][0] = tmp * (m[1][0] * m[2][1] - m[1][1] * m[2][0]);

   res[0][1] = tmp * (m[0][2] * m[2][1] - m[0][1] * m[2][2]);
   res[1][1] = tmp * (m[0][0] * m[2][2] - m[0][2] * m[2][0]);
   res[2][1] = tmp * (m[0][1] * m[2][0] - m[0][0] * m[2][1]);

   res[0][2] = tmp * (m[0][1] * m[1][2] - m[0][2] * m[1][1]);
   res[1][2] = tmp * (m[0][2] * m[1][0] - m[0][0] * m[1][2]);
   res[2][2] = tmp * (m[0][0] * m[1][1] - m[0][1] * m[1][0]);
   
   return res;
}

public String makeString(double[][] m) {
  return "[[ " + m[0][0] + ", " + m[0][1] + ", " + m[0][2] + "];\n"
       + " [ " + m[1][0] + ", " + m[1][1] + ", " + m[1][2] + "];\n"
       + " [ " + m[2][0] + ", " + m[2][1] + ", " + m[2][2] + "]]";
}

public static String makeString(Vector3D v) {
  return "[" + v.getX() + ", " + v.getY() + ", " + v.getZ() + "]";
}

public static String makeString(Quaternion r) {
  //return "[" + r.x0 + ", " + r.x1 + ", " + r.x2 + ", " + r.x3 + "]";
  return "[" + r.getAngle() + ", [" + r.getAxis().getX() + ", " + r.getAxis().getY() + ", " + r.getAxis().getZ() + "]]";
}
class Moon extends Planet { 
  Moon(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass, float r, String tex_name) {
    super(pos_new, vel_new, acc_new, mass, r, tex_name);
    solver = new ODEEulerSolver();
    radius = r;
    bounding_sphere_radius = r;
  }
}
// Implements simple Euler method ODE solver

class ODEEulerSolver extends ODESolver {
  Vector3D nextAngularVelocity;
  double nextInvInertiaTensor[][];
  
  ODEEulerSolver() {
    nextAngularVelocity = new Vector3D();
  }
  
  public void calculate(float dt) {
    //print("asdfasdfasdf");
    //print(makeString(acceleration));
    velocity = velocity.add(acceleration.scalarMultiply(dt));
    position = position.add(velocity.scalarMultiply(dt));
  }
  
  public void calculateRotation(float dt, InertiaTensor inertiaTensor) {
    // http://www.euclideanspace.com/physics/kinematics/angularvelocity/QuaternionDifferentiation2.pdf
    // calculate derivatives of quaternion components
    double wx = nextAngularVelocity.getX();
    double wy = nextAngularVelocity.getY();
    double wz = nextAngularVelocity.getZ();
    
    Quaternion wquat = new Quaternion(0, wx, wy, wz);
    Quaternion qdot = rotation.times(wquat).times(0.5f);

    // add quaternions
    rotation = rotation.plus(qdot.times(dt)).normalize();
    
    //print(makeString(torque));
  
    angularMomentum = angularMomentum.add(dt, torque);
    
    // calculate next local frame angular velocity
    // from http://www.mare.ee/indrek/varphi/vardyn.pdf
    nextInvInertiaTensor = inertiaTensor.invertedMatrix;
    nextAngularVelocity = multMatrix(nextInvInertiaTensor, rotation.applyTo(angularMomentum));
    
    // damping
    angularMomentum = angularMomentum.scalarMultiply(0.9999f);
  }
}
// Implements the fourth-order Runge-Kutta algorithm

class ODERK4Solver extends ODESolver {
  protected Vector3D k1v;
  protected Vector3D k2v;
  protected Vector3D k3v;
  protected Vector3D k4v;
  protected Vector3D k1x;
  protected Vector3D k2x;
  protected Vector3D k3x;
  protected Vector3D k4x;
  
  Vector3D nextAngularVelocity;
  double nextInvInertiaTensor[][];
  
  protected Object o;
  
  ODERK4Solver() {
    k1v = new Vector3D();
    k2v = new Vector3D();
    k3v = new Vector3D();
    k4v = new Vector3D();
    k1x = new Vector3D();
    k2x = new Vector3D();
    k3x = new Vector3D();
    k4x = new Vector3D();
    
    nextAngularVelocity = new Vector3D();
  }
  
  public void calcRK4(float dt, Object newO) {
    
    o = newO;
    
    k1v = acceleration.scalarMultiply(dt);
    k1x = velocity.scalarMultiply(dt);
    
    k2v = calcAccel(k1x.scalarMultiply(0.5f)).scalarMultiply(dt);
    k2x = velocity.add(k1v.scalarMultiply(0.5f)).scalarMultiply(dt);
    
    k3v = calcAccel(k2x.scalarMultiply(0.5f)).scalarMultiply(dt);
    k3x = velocity.add(k2v.scalarMultiply(0.5f)).scalarMultiply(dt);
    
    k4v = calcAccel(k3x).scalarMultiply(dt);
    k4x = velocity.add(k3v).scalarMultiply(dt);
    
    velocity = velocity.add(k1v.add(k2v.add(k3v).scalarMultiply(2)).add(k4v).scalarMultiply(1/6.0f));
    position = position.add(k1x.add(k2x.add(k3x).scalarMultiply(2)).add(k4x).scalarMultiply(1/6.0f));
  }
  
  public Vector3D calcAccel(Vector3D kPos){
    Vector3D temp = new Vector3D();
    // Calculate the forces between each object
    /*
    for(int i = 0; i < objects.size(); i++) {
      Vector3D x = new Vector3D();      
      Object otherObj = (Object)objects.get(i);
      if(otherObj != o) { // Don't need to calculate force on self
        x = otherObj.getPosition().subtract(position.add(kPos)); // Distance vector between the two objects
        temp = temp.add(x.scalarMultiply(Config.GRAVITY_CONSTANT*otherObj.getMass()/Math.pow(x.getNorm(), 3)));
      }
    }
    */
    
    // only worry about the earth
    Object otherObj = (Object)objects.get(0);
    Vector3D x = new Vector3D();
    x = otherObj.getPosition().subtract(position.add(kPos)); // Distance vector between the two objects
    temp = temp.add(x.scalarMultiply(Config.GRAVITY_CONSTANT*otherObj.getMass()/Math.pow(x.getNorm(), 3)));
      
    return temp;
  }
  
  //void calculateRotation(float dt, InertiaTensor inertiaTensor) {
    /*
    // http://www.euclideanspace.com/physics/kinematics/angularvelocity/QuaternionDifferentiation2.pdf
    // calculate derivatives of quaternion components
    double wx = nextAngularVelocity.getX();
    double wy = nextAngularVelocity.getY();
    double wz = nextAngularVelocity.getZ();
    
    Quaternion wquat = new Quaternion(0, wx, wy, wz);
    Quaternion qdot0 = rotation.times(wquat).times(0.5);

    // add quaternions
    rotation = rotation.plus(qdot.times(dt)).normalize();
  
    angularMomentum = angularMomentum.add(dt, torque);
    
    // calculate next local frame angular velocity
    // from http://www.mare.ee/indrek/varphi/vardyn.pdf
    nextInvInertiaTensor = inertiaTensor.invertedMatrix;
    nextAngularVelocity = multMatrix(nextInvInertiaTensor, rotation.applyTo(angularMomentum));
    
    // damping
    angularMomentum = angularMomentum.scalarMultiply(0.99999);
    */
  //}
  
  public void calculateRotation(float dt, InertiaTensor inertiaTensor) {
    // http://www.euclideanspace.com/physics/kinematics/angularvelocity/QuaternionDifferentiation2.pdf
    // calculate derivatives of quaternion components
    double wx = nextAngularVelocity.getX();
    double wy = nextAngularVelocity.getY();
    double wz = nextAngularVelocity.getZ();
    
    Quaternion wquat = new Quaternion(0, wx, wy, wz);
    Quaternion qdot = rotation.times(wquat).times(0.5f);

    // add quaternions
    rotation = rotation.plus(qdot.times(dt)).normalize();
    
    //print(makeString(torque));
  
    angularMomentum = angularMomentum.add(dt, torque);
    
    // calculate next local frame angular velocity
    // from http://www.mare.ee/indrek/varphi/vardyn.pdf
    nextInvInertiaTensor = inertiaTensor.invertedMatrix;
    nextAngularVelocity = multMatrix(nextInvInertiaTensor, rotation.applyTo(angularMomentum));
    
    // damping
    angularMomentum = angularMomentum.scalarMultiply(0.9999f);
  }
  
  //Quaternion calcQuat2ndDeriv(Quaternion q, Quaternion qdot, double dt, torque
}
// Interface for ODE solver classes

class ODESolver {
  
  protected Vector3D position;
  protected Vector3D velocity;
  protected Vector3D acceleration;
  
  protected Quaternion rotation;
  protected Vector3D angularMomentum;
  protected Vector3D torque;
  
  ODESolver() {
    position = new Vector3D();
    velocity = new Vector3D();
    acceleration = new Vector3D();
  }
  
  // Send values to the ODE Solver
  public void setValue(Vector3D position, Vector3D velocity, Vector3D acceleration) {
    this.position = position;
    this.velocity = velocity;
    this.acceleration = acceleration;
  }
  
  public void setAngularValues(Quaternion rotation, Vector3D angularMomentum, Vector3D torque) {
    this.rotation = rotation;
    this.angularMomentum = angularMomentum;
    this.torque = torque;
  }
  
  // Takes values sent to the solver and updates them one time step
  public void calculate(float dt){}
  
  public void calculateRotation(float dt, InertiaTensor globalTensor){}
  public void calcRK4(float dt, Object newO){}
  
  // Use these getter fundtions after calling calculate to obtain new values
  public Vector3D getPosition() {
    return position;
  }
  
  public Vector3D getVelocity() {
    return velocity;
  }
  
  public Vector3D getAcceleration() {
    return acceleration;
  }
  
  public Vector3D calcAccel(Vector3D kPos){
    return acceleration;
  }
  
  public Quaternion getRotation() {
    return rotation;
  }
  
  public Vector3D getAngularMomentum() {
    return angularMomentum;
  }
  
  public Vector3D getTorque() {
    return torque;
  }
}
  
  
// An object is something which has a mass and may be under the influence of forces

abstract class Object {
  protected DataContainer data;
  protected float mass;
  protected float bounding_sphere_radius;
  protected ODESolver solver;
  protected GLModel model;
  
  Object(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass) {
    if(config.bRealTime) {
      data = new RealTimeDataContainer();
    } else {
      data = new StoredDataContainer();
    }
    
    data.setPosition(pos_new);
    data.setVelocity(vel_new);
    data.setAcceleration(acc_new);
    data.saveValues();
    
    this.mass = mass;
  }
  
  public float getMass() {
    return mass;
  }
  
  public Vector3D getPosition() {
    return data.getPosition();
  }
  
  public Vector3D getVelocity() {
    return data.getVelocity();
  }
  
  public Vector3D getAcceleration() {
    return data.getAcceleration();
  }
  
  public Quaternion getRotation() {
    return data.getRotation();
  }
  
  public Vector3D getAngularMomentum() {
    return data.getAngularMomentum();
  }

  public Vector3D getTorque() {
    return data.getTorque();
  }
  
  public float getBoundingSphere() {
    return bounding_sphere_radius;
  }
  
  public void setPosition(Vector3D new_position) {
    data.setPosition(new_position);
  }
  
  public void setVelocity(Vector3D new_velocity) {
    data.setVelocity(new_velocity);
  }
  
  public void setAcceleration(Vector3D new_acceleration) {
    data.setAcceleration(new_acceleration);
  }
  
  public void setRotation(Quaternion new_rotation) {
    data.setRotation(new_rotation);
  }
  
  public void setAngularMomentum(Vector3D new_angularMomentum) {
    data.setAngularMomentum(new_angularMomentum);
  }
  
  public void setTorque(Vector3D new_torque) {
    data.setTorque(new_torque);
  }
  
  public void setTime(float new_time) {
    data.setTime(new_time);
  }
  
  public void saveValues() {
    data.saveValues();
  }
  
  public abstract void render();
  
  public abstract void tick(float dt);
  
}
  
// Main Sources:
//   (i)   http://www.bruce-shapiro.net/pair/
//   (ii)  http://www.amsat.org/amsat/keps/kepmodel.html
//   (iii) http://en.wikipedia.org/wiki/Orbital_elements
//   (iv)  http://www.elsevierdirect.com/companions/9780126836301/appendices/Chapter_2_-_Kepler_Orbit_Elements_to_Eci_Cartesian_Coordinates_Conversion.pdf

static class OrbitalElements {

  // Semimajor axis
  static float a = 7078; //km?

  // Eccentricity
  static float e = 0.0f; //range [0,1) (but breaks above ~0.97)
  // Inclination
  static float incl = 1.693f; //radians, range [0,PI]
  // Argument of periapsis
  static float omega = 0; //radians, range [0,2PI]
  // Longitude of the ascending node
  static float OMEGA = 0; //radians, range [0,2PI]
  // Mean anomaly
  static float M = PI/2; //range [0,2PI]

  // Orbital period
  static float T = 6; //days/revolution
  
  public static JavaSucksReturnTwo getParameters(Vector3D pos, Vector3D vel) {
  
    //Vector3D curPoint = new Vector3D();
    //Vector3D prevPoint = new Vector3D();
  
    //float t = i/(numPoints/10.0);
    //M = n*(t - T/(2*PI));
    // Calculate the eccentric anomaly, E
    float E = CalcEccentricAnomaly(M);
    //println("M = " + M + "; E = " + E + ";");
    
    // Calculate the true anomaly, range (0,2PI)
    float nu = 2*atan(sqrt((1+e)/(1-e))*tan(E/2));
    
    // Calculate radius, r
    float r = a*(1-pow(e,2))/(1+e*cos(nu));
    
    // Calculate mu:
    // Note: not sure if this is correct in this context
    float mu = Config.GRAVITY_CONSTANT*(Config.MASS_EARTH + Config.MASS_SATELLITE);
    
    // Compute specific angular momentum, h
    float h = sqrt(mu*a*(1-pow(e,2)));    
    
    // Calculate position and velocity in cartesian co-ords
    //Vector3D pos = new Vector3D();
    //Vector3D vel = new Vector3D();
  
    pos = new Vector3D(
      r*(cos(OMEGA)*cos(omega+nu) - sin(OMEGA)*sin(omega+nu)*cos(incl)),
      r*(sin(OMEGA)*cos(omega+nu) + cos(OMEGA)*sin(omega+nu)*cos(incl)),
      r*(sin(incl)*sin(omega+nu))
    );
    
    vel = new Vector3D(
      pos.getX()*h*e*sin(nu)/(r*a*(1-pow(e,2))) - h*(cos(OMEGA)*sin(omega+nu)+sin(OMEGA)*cos(omega+nu)*cos(incl))/r,
      pos.getY()*h*e*sin(nu)/(r*a*(1-pow(e,2))) - h*(sin(OMEGA)*sin(omega+nu)-cos(OMEGA)*cos(omega+nu)*cos(incl))/r,
      pos.getZ()*h*e*sin(nu)/(r*a*(1-pow(e,2))) + h*(sin(incl)*cos(omega+nu))/r
    );
    
    JavaSucksReturnTwo res = new JavaSucksReturnTwo();
    res.a = pos;
    res.b = vel;
    return res;
  }

  static private float CalcEccentricAnomaly(float M0){
    // Want to calculate the eccentric anomaly given a known mean anomaly 
    
    // Set how accurate the apprimation must be (lower maxDiff -> more accurate)
    float maxDiff = 1e-15f;
    
    float[] E = new float[2];
    E[0] = M0;
    E[1] = E[0] - (E[0] - e*sin(E[0]) - M0)/(1-e*cos(E[0]));
    
    // Track number of iterations of the approximation
    // If it has tried over 100 times, stop trying and take the value found
    int numIter = 0;
    
    while(abs(E[0]-E[1]) > maxDiff && numIter < 100){
      numIter ++;
      E[0] = E[1];
      E[1] = E[0] - (E[0] - e*sin(E[0]) - M0)/(1-e*cos(E[0]));
    }
    //println("Iterations: " + numIter);
    return E[1];  
  }
}
class Planet extends Object  {
  protected GLTexture texmap;
  private int sDetail = 50; // Increase to make the sphere more detailed
  float radius;

  Planet(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass, float r, String tex_name) {
    super(pos_new, vel_new, acc_new, mass);
 
    texmap = new GLTexture(Phys350Project.this, tex_name); 
    solver = new ODERK4Solver();
    radius = r;
    bounding_sphere_radius = r;
    init_model();
  }
  
  // Create the model and store it for future rendering
  public void init_model() {
    float delta = 360.0f/sDetail;
    float[] sphereX, sphereY, sphereZ; // used for drawing
    float[] cx = new float[sDetail];
    float[] cz = new float[sDetail];
    ArrayList vertices;
    ArrayList normals;   
    ArrayList uv_map;
    int v1,v11,v2;
    
    // Calc unit circle in XZ plane
    for (int i = 0; i < sDetail; i++) {
      cx[i] = -trig.ncos((int) (i*delta));
      cz[i] = trig.nsin((int) (i*delta));
    }
    
    // Computing vertexlist vertexlist starts at south pole
    int vertCount = sDetail * (sDetail-1) + 2;
    int currVert = 0;
    
    // Re-init arrays to store vertices
    sphereX = new float[vertCount];
    sphereY = new float[vertCount];
    sphereZ = new float[vertCount];
    float angle_step = (360.0f*0.5f)/sDetail;
    float angle = angle_step;
    
    // Step along Y axis
    for (int i = 1; i < sDetail; i++) {
      float curradius = trig.nsin((int) angle % trig.SINCOS_LENGTH);
      float currY = -trig.ncos((int) angle % trig.SINCOS_LENGTH);
      for (int j = 0; j < sDetail; j++) {
        sphereX[currVert] = cx[j] * curradius;
        sphereY[currVert] = currY;
        sphereZ[currVert++] = cz[j] * curradius;
      }
      angle += angle_step;
    }
    
    vertices = new ArrayList();
    normals = new ArrayList();
    uv_map = new ArrayList();
    
    float iu = (float) (1.0f / (sDetail));
    float iv = (float) (1.0f / (sDetail));

    float u=0,v=iv;
    for (int i = 0; i < sDetail; i++) {
      addVertex(vertices, normals,uv_map,0, -radius, 0,u,0);
      addVertex(vertices, normals,uv_map,sphereX[i]*radius, sphereY[i]*radius, sphereZ[i]*radius, u, v);
      u+=iu;
    }
    addVertex(vertices, normals,uv_map,0, -radius, 0,u,0);
    addVertex(vertices, normals,uv_map,sphereX[0]*radius, sphereY[0]*radius, sphereZ[0]*radius, u, v);
     
    
    // Middle rings
    int voff = 0;
    for(int i = 2; i < sDetail; i++) {
      v1=v11=voff;
      voff += sDetail;
      v2=voff;
      u=0;

      for (int j = 0; j < sDetail; j++) {
        addVertex(vertices, normals,uv_map,sphereX[v1]*radius, sphereY[v1]*radius, sphereZ[v1++]*radius, u, v);
        addVertex(vertices, normals,uv_map,sphereX[v2]*radius, sphereY[v2]*radius, sphereZ[v2++]*radius, u, v+iv);
        u+=iu;
      }
    
      // Close each ring
      v1=v11;
      v2=voff;
      addVertex(vertices, normals,uv_map,sphereX[v1]*radius, sphereY[v1]*radius, sphereZ[v1]*radius, u, v);
      addVertex(vertices, normals,uv_map,sphereX[v2]*radius, sphereY[v2]*radius, sphereZ[v2]*radius, u, v+iv);
     
      v+=iv;
    }
    u=0;
    
    // Add the northern cap

    for (int i = 0; i < sDetail; i++) {
      v2 = voff + i;
      addVertex(vertices, normals,uv_map,sphereX[v2]*radius, sphereY[v2]*radius, sphereZ[v2]*radius, u, v);
      addVertex(vertices, normals,uv_map,0, radius, 0,u,v+iv);    
      u+=iu;
    }
    addVertex(vertices, normals,uv_map,sphereX[voff]*radius, sphereY[voff]*radius, sphereZ[voff]*radius, u, v); 
    
    model = new GLModel(Phys350Project.this, vertices.size(), TRIANGLE_STRIP, GLModel.STATIC);
    // Sets the coordinates.
    model.updateVertices(vertices);    
    
    // Sets the normals.    
    model.initNormals();
    model.updateNormals(normals);    
    
    model.initTextures(1);    
    model.setTexture(0, texmap);

    model.updateTexCoords(0,uv_map);
    model.initColors();
    model.setColors(255);
  }
  
  private void addVertex(ArrayList vertices, ArrayList normals, ArrayList uv_map, float x, float y, float z, float u, float v)
  {
      PVector vert = new PVector(x, y, z);
      PVector vertNorm = PVector.div(vert, vert.mag()); 
      PVector uv = new PVector(u, v, 0);
      vertices.add(vert);
      normals.add(vertNorm);
      uv_map.add(uv);
  } 
    
  
  public void render()
  {
    pushMatrix();
      noStroke();
      translate((float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ());
      rotateX(-PI/2.0f); // rotate so z axis up goes through north pole
      model.render();
    popMatrix();
  }
  
  public void tick(float dt) {
    Vector3D temp = new Vector3D();
   
    // Calculate the forces between each object
    /*
    for(int i = 0; i < objects.size(); i++) {
      Vector3D x;
      Object o = (Object)objects.get(i);
      if(o != this) { // Don't need to calculate force on self
        x = o.getPosition().subtract(getPosition()); // Distance vector between the two objects
        temp = temp.add(x.scalarMultiply(Config.GRAVITY_CONSTANT*o.getMass()/Math.pow(x.getNorm(), 3)));
      }
    }
    
    // RK4
    setAcceleration(temp);
    solver.setValue(getPosition(), getVelocity(), temp);
    solver.calcRK4(dt, this);
    
    setPosition(solver.getPosition());
    setVelocity(solver.getVelocity());
    setTime(current_time);
    saveValues();
    
    */
  }      
}
// based off of http://introcs.cs.princeton.edu/32class/Quaternion.java.html

public class Quaternion {
    public final double x0, x1, x2, x3; 

    // create a new object with the given components
    public Quaternion(double x0, double x1, double x2, double x3) {
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
    }
    
    // create a new quaternion with the given components with the option to normalize
    public Quaternion(double x0, double x1, double x2, double x3, boolean needsNormalization) {
        if (needsNormalization) {
            double invnorm = 1.f / Math.sqrt(x0*x0 + x1*x1 + x2*x2 + x3*x3);
            x0 *= invnorm;
            x1 *= invnorm;
            x2 *= invnorm;
            x3 *= invnorm;
        }
        this.x0 = x0;
        this.x1 = x1;
        this.x2 = x2;
        this.x3 = x3;
    }
    
    // quaternion from a rotation
    public Quaternion(Vector3D axis, double angle) {
        this.x0 = Math.cos(angle/2);
        axis = axis.normalize();
        double sinhalf = Math.sin(angle/2);
        this.x1 = axis.getX() * sinhalf;
        this.x2 = axis.getY() * sinhalf;
        this.x3 = axis.getZ() * sinhalf;
    }

    // default quaternion (no rotation)
    public Quaternion() {
        this.x0 = 1;
        this.x1 = 0;
        this.x2 = 0;
        this.x3 = 0;
    }
    
    // quaternion from one vector to another (note: this does not uniquely define a quaternion!)
    public Quaternion(Vector3D v1, Vector3D v2) {
        double angle;
        try {
          angle = Vector3D.angle(v1, v2);
        } catch (ArithmeticException e) {
          print("broken @ Quaternion(Vector3D v1, Vector3D v2)\n");
          x0 = 1;
          x1 = 0;
          x2 = 0;
          x3 = 0;
          return;
        }
        Vector3D axis = Vector3D.crossProduct(v1, v2);
       
        //print(makeString(v1) + " : " + makeString(v2) + "\n");
        //print(angle);
       
        if (angle > PI-0.000005f) {
          // rotation by 180 degrees
          this.x0 = -1;
          this.x1 = 0;
          this.x2 = 0;
          this.x3 = 0;
        } else if (angle > 0.000005f) {
          // copied from axis/angle code. Java sucks...
          this.x0 = Math.cos(angle/2);
          axis = axis.normalize();
          double sinhalf = Math.sin(angle/2);
          this.x1 = axis.getX() * sinhalf;
          this.x2 = axis.getY() * sinhalf;
          this.x3 = axis.getZ() * sinhalf;
        } else {
          // no rotation actually occurred
          this.x0 = 1;
          this.x1 = 0;
          this.x2 = 0;
          this.x3 = 0;
        }
    }

    // return a string representation of the invoking object
    public String toString() {
        return x0 + " + " + x1 + "i + " + x2 + "j + " + x3 + "k";
    }

    // return the quaternion norm
    public double norm() {
        return Math.sqrt(x0*x0 + x1*x1 +x2*x2 + x3*x3);
    }
    
    // return a normalized quaternion
    public Quaternion normalize() {
        double d = this.norm();
        return new Quaternion(x0/d, x1/d, x2/d, x3/d);
    }

    // return the quaternion conjugate
    public Quaternion conjugate() {
        return new Quaternion(x0, -x1, -x2, -x3);
    }

    // return a new Quateyrnion whose value is (this + b)
    public Quaternion plus(Quaternion b) {
        Quaternion a = this;
        return new Quaternion(a.x0+b.x0, a.x1+b.x1, a.x2+b.x2, a.x3+b.x3);
    }


    // return a new Quaternion whose value is (this * b)
    public Quaternion times(Quaternion b) {
        Quaternion a = this;
        double y0 = a.x0*b.x0 - a.x1*b.x1 - a.x2*b.x2 - a.x3*b.x3;
        double y1 = a.x0*b.x1 + a.x1*b.x0 + a.x2*b.x3 - a.x3*b.x2;
        double y2 = a.x0*b.x2 - a.x1*b.x3 + a.x2*b.x0 + a.x3*b.x1;
        double y3 = a.x0*b.x3 + a.x1*b.x2 - a.x2*b.x1 + a.x3*b.x0;
        return new Quaternion(y0, y1, y2, y3);
    }
    
    // scalar multiplication
    public Quaternion times(double b) {
        Quaternion a = this;
        double y0 = a.x0*b;
        double y1 = a.x1*b;
        double y2 = a.x2*b;
        double y3 = a.x3*b;
        return new Quaternion(y0, y1, y2, y3);
    }

    // return a new Quaternion whose value is the inverse of this
    public Quaternion inverse() {
        double d = x0*x0 + x1*x1 + x2*x2 + x3*x3;
        return new Quaternion(x0/d, -x1/d, -x2/d, -x3/d);
    }

    // return a / b
    public Quaternion divides(Quaternion b) {
        Quaternion a = this;
        return a.inverse().times(b);
    }


    // return the corresponding left handed rotation matrix
    public double[][] getLeftMatrix() {
        // adapted from http://en.wikipedia.org/wiki/Rotation_matrix
        // verified at http://www.euclideanspace.com/maths/algebra/realNormedAlgebra/quaternions/transforms/index.htm
        // this corresponds to a left rotation
      
        // pointless, but more readable.
        double w = this.x0;
        double x = this.x1;
        double y = this.x2;
        double z = this.x3;
      
        double[][] res = new double[3][3];
        res[0][0] = 2*x*x + 2*w*w - 1;
        res[0][1] = 2*x*y - 2*z*w;
        res[0][2] = 2*x*z + 2*y*w;
        res[1][0] = 2*x*y + 2*z*w;
        res[1][1] = 2*y*y + 2*w*w - 1;
        res[1][2] = 2*y*z - 2*x*w;
        res[2][0] = 2*x*z - 2*y*w;
        res[2][1] = 2*y*z + 2*x*w;
        res[2][2] = 2*z*z + 2*w*w - 1;
        
        return res;
    }

    // return the corresponding right handed rotation matrix
    public double[][] getRightMatrix() {
        // from http://osdir.com/ml/games.devel.algorithms/2002-11/msg00318.html
        // results verified with http://www.cprogramming.com/tutorial/3d/rotationMatrices.html
      
        // pointless, but more readable.
        double w = this.x0;
        double x = this.x1;
        double y = this.x2;
        double z = this.x3;
      
        double[][] res = new double[3][3];
        res[0][0] = 2*x*x + 2*w*w - 1;
        res[0][1] = 2*x*y + 2*z*w;
        res[0][2] = 2*x*z - 2*y*w;
        res[1][0] = 2*x*y - 2*z*w;
        res[1][1] = 2*y*y + 2*w*w - 1;
        res[1][2] = 2*y*z + 2*x*w;
        res[2][0] = 2*x*z + 2*y*w;
        res[2][1] = 2*y*z - 2*x*w;
        res[2][2] = 2*z*z + 2*w*w - 1;
        
        return res;
    }
    
    // return the corresponding axis of rotation
    public Vector3D getAxis() {
        double s = Math.sqrt(1 - x0*x0);
        if (s < 0.00001f) {
            // axis of rotation is arbitrary if no rotation occurs
            return new Vector3D(1,0,0);
        }
        return new Vector3D(x1 / s, x2 / s, x3 / s);
    }
    
    // return the corresponding angle for axis/angle notation
    public double getAngle() {
        return 2*Math.acos(x0);
    }

    // return a vector with the transformation applied to it
    public Vector3D applyTo(Vector3D v) {
        // LEFT quaternion, so q*v*conj(q)
        // this is equivalent to multiplying by the corresponding rotation matrix
        //print("right mat: " + makeString(multMatrix(getRightMatrix(), v)) + "\n");
     /*   print("left mat:  " + makeString(multMatrix(getLeftMatrix(), v)) + "\n");
        Quaternion qvec = new Quaternion(0, v.getX(), v.getY(), v.getZ());
        Quaternion qres = this.times(qvec.times(this.inverse()));
        Vector3D res = new Vector3D(qres.x1, qres.x2, qres.x3);
        print("quat mult: " + makeString(res) + "\n");
        return res;*/
        return multMatrix(getRightMatrix(), v);
    }
}

// Class which holds real time data. Use this type of container whenever speed is required.

class RealTimeDataContainer extends DataContainer {
  private Vector3D position;
  private Vector3D velocity;
  private Vector3D acceleration;
  
  private Quaternion rotation;
  private Vector3D angularMomentum;
  private Vector3D torque;
 
  private float time;
  
  public Vector3D getPosition() {
    return position;
  }
  
  public Vector3D getVelocity() {
    return velocity;
  }
  
  public Vector3D getAcceleration() {
    return acceleration;
  }
  
  public Quaternion getRotation() {
    return rotation;
  }
  
  public Vector3D getAngularMomentum() {
    return angularMomentum;
  }
  
  public Vector3D getTorque() {
    return torque;
  }
  
  public float getTime() {
    return time;
  }
  
  public void setPosition(Vector3D new_position) {
    position = new_position;
  }
  
  public void setVelocity(Vector3D new_velocity) {
    velocity = new_velocity;
  }
  
  public void setAcceleration(Vector3D new_acceleration) {
    acceleration = new_acceleration;
  }
  
  public void setRotation(Quaternion new_rotation) {
    rotation = new_rotation;
  }
  
  public void setAngularMomentum(Vector3D new_angularMomentum) {
    angularMomentum = new_angularMomentum;
  }
  
  public void setTorque(Vector3D new_torque) {
    torque = new_torque;
  }
  
  public void setTime(float new_time) {
    time = new_time;
  }
  
  public void saveValues() {}
  
}
  
  
class Satellite extends Object {
  GLModel model;
  ObjImpScene obj_model;
  private PFont label_font;
  InertiaTensor inertiaTensor;
  // Had to move this out here for the function find_angle()
  Quaternion globalFromSatelliteQuaternion = new Quaternion();
  
  int countbla;
  
  Satellite(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, Quaternion rot_new, Vector3D ang_mom_new, Vector3D torque_new, float mass, InertiaTensor inertiaTensor_new) {
    super(pos_new, vel_new, acc_new, mass);

    // load the satellite model
    // Satellite model not used in final simulation
    //obj_model = new ObjImpScene(((GLGraphics)g).gl);
    //obj_model.load(dataPath("sat2small.obj"));

    solver = new ODERK4Solver();
    bounding_sphere_radius = 100;
    inertiaTensor = inertiaTensor_new;
   // calculate_trajectory();
   
    //TODO: move this to the Object constructor if we need rotation for other objects
    setRotation(rot_new);
    setAngularMomentum(ang_mom_new);
    setTorque(torque_new);
    
    countbla = 0;
  }
  
  public void render() {
    if(!Config.bRealTime && Config.bShowOrbitPrediction) {
      render_trajectory();
    }
    pushMatrix();
    ambientLight(75,75,75);
    translate((float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ());
    
    // use left matrix since the original coordinate frame was left handed? I'm not too sure about this...
    double[][] rotMatrix = getRotation().getLeftMatrix();
    applyMatrix(
      (float)rotMatrix[0][0], (float)rotMatrix[0][1], (float)rotMatrix[0][2], 0,
      (float)rotMatrix[1][0], (float)rotMatrix[1][1], (float)rotMatrix[1][2], 0,
      (float)rotMatrix[2][0], (float)rotMatrix[2][1], (float)rotMatrix[2][2], 0,
      0,                      0,                      0,                      1
    );
    
    fill(200);
    
    // draw a box for the satellite (long axis = y axis)
    box(10,1000,100);
    
    noFill();
    //obj_model.draw(gl_object);
    popMatrix();    
  }
    
  public void render_trajectory() {
    int temp_tick_count = tick_count;
    boolean bFirstStep = true;
    
    Vector3D start_pos = getPosition();
    Vector3D earth_start_pos = earth.getPosition();
    Vector3D earth_pos_difference = new Vector3D();

    pushMatrix();
    resetMatrix();
    stroke(250,0,0);
    noFill();
    curveTightness(0);
    beginShape();
    curveVertex((float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ());
    tick_count = tick_count + 1;
    curveVertex((float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ());
    tick_count = tick_count + 50;
    Vector3D prev_pos = getPosition();

    while(tick_count < Config.numDataPoints) {
      if(!bFirstStep && (getPosition().subtract(start_pos)).getNorm() < 300) {    
        curveVertex((float)start_pos.getX(), (float)start_pos.getY(), (float)start_pos.getZ());
        tick_count++;
        curveVertex((float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ()); 
        break;
      }
      bFirstStep = false;
      earth_pos_difference = earth_start_pos.subtract(earth.getPosition());
      curveVertex((float)(prev_pos.getX()+earth_pos_difference.getX()), (float)(prev_pos.getY()+earth_pos_difference.getY()), (float)(prev_pos.getZ()+earth_pos_difference.getZ()));
      prev_pos = getPosition();
      tick_count = tick_count + 50;
    }
    endShape();
    noStroke();
    popMatrix();
    tick_count = temp_tick_count;
  }
  
  public void tick(float dt) {
    calculate_position(dt);
    calculate_rotation(dt);
  }
  
  private void calculate_position(float dt) {
    Vector3D temp = new Vector3D();
    /*
    for(int i = 0; i < objects.size(); i++) {
      Vector3D x;
      Object o = (Object)objects.get(i);
      
      if(o != this) {
        x = o.getPosition().subtract(getPosition());
        temp = temp.add(x.scalarMultiply(Config.GRAVITY_CONSTANT*o.getMass()/Math.pow(x.getNorm(), 3)));
      }
    }
    */
    
    Vector3D x;
    Object o = (Object)objects.get(0);
    x = o.getPosition().subtract(getPosition());
    temp = temp.add(x.scalarMultiply(Config.GRAVITY_CONSTANT*o.getMass()/Math.pow(x.getNorm(), 3)));
        
    setAcceleration(temp);
    solver.setValue(getPosition(), getVelocity(), temp);
    solver.calculate(dt);
      
      
    solver.calcRK4(dt, this);

    //print(makeString(getPosition()));
    //print(makeString(solver.getPosition()));
    //print("\n");

    setPosition(solver.getPosition());
    setVelocity(solver.getVelocity());
    setTime(current_time);
    saveValues();
  }
  
  private void calculate_rotation(float dt) {
    Vector3D newTorque = new Vector3D(0,0,0);
    
    for (int i=0; i<objects.size(); i++) {
      Object o = (Object)objects.get(i);
    
      if (o != this && !(o instanceof Satellite)) {  // don't bother calculating for small objects
        // get the vector from the object to the satellite
        Vector3D r = getPosition().subtract(o.getPosition());

        // get all of the different rotations that we will be using
        Quaternion gravityFromGlobalQuaternion = new Quaternion(new Vector3D(0, 1, 0), r);
        Quaternion globalFromGravityQuaternion = gravityFromGlobalQuaternion.inverse();
        Quaternion satelliteFromGlobalQuaternion = getRotation();
        globalFromSatelliteQuaternion = satelliteFromGlobalQuaternion.inverse();
        Quaternion gravityFromSatelliteQuaternion = globalFromSatelliteQuaternion.times(gravityFromGlobalQuaternion);
        Quaternion satelliteFromGravityQuaternion = gravityFromSatelliteQuaternion.inverse();
        
        // calculate the inertia tensor in the gravity reference frame
        double[][] gravityFrameInertiaTensorMatrix = inertiaTensor.rotatedMatrix(gravityFromSatelliteQuaternion);
        
        // calculate torque in the gravity reference frame
        float torqueConstants = 2*Config.GRAVITY_CONSTANT*o.getMass()/pow((float)r.getNorm(), 3);
        
        Vector3D gravityFrameTorque = new Vector3D((torqueConstants*gravityFrameInertiaTensorMatrix[1][2]), 0, -(torqueConstants*gravityFrameInertiaTensorMatrix[0][1]));
        
        // calculate torque in the global reference frame
        Vector3D torque = globalFromGravityQuaternion.applyTo(gravityFrameTorque);
        newTorque = newTorque.add(torque);
      }
    }
    
    solver.setAngularValues(getRotation(), getAngularMomentum(), newTorque);
    
    solver.calculateRotation(dt, inertiaTensor);
    
    setRotation(solver.getRotation());
    setAngularMomentum(solver.getAngularMomentum());
  }
  
    public double find_angle(){
    Vector3D V = new Vector3D(0,1,0);
    Vector3D earth_pos_difference = new Vector3D();
    earth_pos_difference = getPosition().subtract(earth.getPosition());
    
    Vector3D newSatelliteAxisOrientation = globalFromSatelliteQuaternion.applyTo(V);
    
    Vector3D temp = new Vector3D(earth_pos_difference.getX(), earth_pos_difference.getY(), earth_pos_difference.getZ());

    double a = Vector3D.angle(newSatelliteAxisOrientation, temp);

    return a;
  }
}
    
    
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
// Renders the space background

class Skybox {
  
  private GLTexture sky_tex;
  
  Skybox() {
    sky_tex = new GLTexture(Phys350Project.this,"spaceskybox.jpg");
  }
  
  public void render_skybox() {
    pushMatrix();
      resetMatrix();
  
      hint(DISABLE_DEPTH_TEST);
      noStroke();
      scale(Config.FAR_Z_CLIP, Config.FAR_Z_CLIP, Config.FAR_Z_CLIP);
      textureMode(NORMALIZED);
      // Face intersects +x axis
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, 1, 0, 0);
        vertex(1, -1, 1, 0, 1);
        vertex(1, -1, -1, 1, 1);
        vertex(1, 1, -1, 1, 0);
  
      endShape();
      // Face intercepts -x
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(-1, 1, 1, 0, 0);
        vertex(-1, -1, 1, 0, 1);
        vertex(-1, -1, -1, 1, 1);
        vertex(-1, 1, -1, 1, 0);
  
      endShape();
      // Face intercepts +y
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, 1, 0, 0);
        vertex(-1, 1, 1, 0, 1);
        vertex(-1, 1, -1, 1, 1);
        vertex(1, 1, -1, 1, 0);
  
      endShape();   
      // Face intercepts -y
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, -1, 1, 0, 0);
        vertex(-1, -1, 1, 0, 1);
        vertex(-1, -1, -1, 1, 1);
        vertex(1, -1, -1, 1, 0);
  
      endShape();     
      // Face intercepts +z
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, 1, 0, 0);
        vertex(-1, 1, 1, 0, 1);
        vertex(-1, -1, 1, 1, 1);
        vertex(1, -1, 1, 1, 0);
  
      endShape();   
      // Face intercepts -z
      beginShape(QUADS);
        texture(sky_tex);
        
        vertex(1, 1, -1, 0, 0);
        vertex(-1, 1, -1, 0, 1);
        vertex(-1, -1, -1, 1, 1);
        vertex(1, -1, -1, 1, 0);
  
      endShape();
      hint(ENABLE_DEPTH_TEST);
    popMatrix();
  }
}
// Implements slider controls

class Slider extends AbstractSlider
{
  float tickSpacing;
  boolean paintTicks, snapToTicks;

  float[] pos = new float[2], size = new float[2];

  Slider(float x, float y, float width, float height, int halign, int valign, int orientation, float min, float max, float value, int color1, int color2)
  {
    super(min, max, value, 0.0f);  // extent is not relevant here...

    this.halign = halign;
    this.valign = valign;
    this.orientation = orientation;
    this.color1 = color1;
    this.color2 = color2;

    c1 = orientation;
    c2 = 1 - c1;

    enablable = getIsEnablable();
    setBounds(x, y, width, height);
  }

  public void setValue(float n)
  {
    if (snapToTicks)
    {
      n = min + round((n - min) / tickSpacing) * tickSpacing;
    }

    super.setValue(n);
  }

  public void setTickSpacing(float spacing)
  {
    tickSpacing = spacing;
  }

  public void setPaintTicks(boolean b)
  {
    paintTicks = b;
  }

  public void setSnapToTicks(boolean b)
  {
    snapToTicks = b;
  }

  public void run()
  {
    knob_locked = knob_locked && enablable && enabled && mousePressed;

    m_comp[0] = mouseX;
    m_comp[1] = mouseY;

    over_track = !degraded && enablable && enabled && !armed && !knob_locked && mousePressed && (m_comp[0] >= pos[0] && m_comp[0] < (pos[0] + size[0]) && m_comp[1] >= pos[1] && m_comp[1] < (pos[1] + size[1]));
    armed = !over_track && mousePressed;

    if (degraded)
    {
      return;
    }

    over_knob = over_track && (m_comp[c1] >= (pos[c1] + comp[c1]) && m_comp[c1] < (pos[c1] + comp[c1] + knob_size[c1]) && m_comp[c2] >= (pos[c2] + comp[c2]) && m_comp[c2] < (pos[c2] + comp[c2] + knob_size[c2]));

    if (over_knob)
    {
      knob_locked = true;
      offset = (m_comp[c1] - pos[c1] - comp[c1]) * (max - min) / (size[c1] - knob_size[c1]);
    }
    else if (over_track)
    {
      setValue(min + (m_comp[c1] - pos[c1] - knob_size[c1] / 2.0f) * (max - min) / (size[c1] - knob_size[c1]));
    }

    if (knob_locked)
    {
      setValue((m_comp[c1] - pos[c1]) * (max - min) / (size[c1] - knob_size[c1]) - offset);
    }
  }

  public boolean getIsEnablable()
  {
    return max != min;
  }

  public void updateComp()
  {
    if (!degraded)
    {
      comp[c1] = !enablable ? 0.0f : (value - min) / (max - min) * (size[c1] - knob_size[c1]);
    }
  }

  public void recalc()
  {
    size[c1] = orientation == HORIZONTAL ? width : height;
    size[c2] = orientation == HORIZONTAL ? height : width;
    pos[c1] = orientation == HORIZONTAL ? left : top;
    pos[c2] = orientation == HORIZONTAL ? top : left;

    knob_size[c1] = size[c2];
    knob_size[c2] = size[c2];

    degraded = knob_size[c1] > size[c1];  // gracefull degradation at paranormal sizes

    updateComp();
    comp[c2] = 0.0f;
  }

  public void draw()
  {
    drawTrack();

    if (!degraded)
    {
      drawKnob();

      if (paintTicks)
      {
        drawTicks();
      }
    }
  }

  public void drawTrack()
  {
    noStroke();
    rectMode(CORNER);

    fill(color2);
    rect(pos[0], pos[1], size[0], size[1]);

    fill(color1);
    if (orientation == HORIZONTAL)
    {
      rect(pos[0], pos[1] + size[1] / 2.0f, size[0], 1.0f);
    }
    else
    {
      rect(pos[0] + size[0] / 2.0f, pos[1], 1.0f, size[1]);
    }
  }

  public void drawTicks()
  {
    float s = tickSpacing / (max - min) * (size[c1] - knob_size[c1]);
    if (s >= 2.0f)
    {
      noStroke();
      rectMode(CENTER_DIAMETER);

      float o = pos[c1] + knob_size[c1] / 2.0f;
      float i;
      int n_ticks = (int)((max - min) / tickSpacing);
      for (int n = 0; n <= n_ticks; n++)
      {
        i = o + n * s;

        if (knob_locked && i >= pos[c1] + comp[c1] && i < pos[c1] + comp[c1] + knob_size[c1])
        {
          fill(color2);
        }
        else
        {
          fill(color1);
        }

        if (orientation == HORIZONTAL)
        {
          rect(i, pos[1] + size[1] / 2.0f, 1.0f, size[1] / 3.0f);
        }
        else
        {
          rect(pos[0] + size[0] / 2.0f, i, size[0] / 3.0f, 1.0f);
        }
      }
    }
  }

  public void drawKnob()
  {
    stroke(color1);
    fill(knob_locked ? color1 : color2);
    rectMode(CORNER);
    rect(pos[0] + comp[0], pos[1] + comp[1], knob_size[0] - 1.0f, knob_size[1] - 1.0f);
  }
}

class StoredDataContainer extends DataContainer {
  private ArrayList position;
  private boolean position_changed;
  private ArrayList velocity;
  private boolean velocity_changed;
  private ArrayList acceleration;
  private boolean acceleration_changed;
  private ArrayList time;
  private boolean time_changed;
  
  StoredDataContainer() {
    position = new ArrayList();
    velocity = new ArrayList();
    acceleration = new ArrayList();
    time = new ArrayList();

    position_changed = false;
    velocity_changed = false;
    acceleration_changed = false;
    time_changed = false;
  }
  
  
  public Vector3D getPosition() {
    return (Vector3D)position.get(tick_count);
  }
  
  public Vector3D getVelocity() {
    return (Vector3D)velocity.get(tick_count);
  }
  
  public Vector3D getAcceleration() {
    return (Vector3D)acceleration.get(tick_count);
  }
  
  public Quaternion getRotation() {
    //TODO
    return new Quaternion();
  }
  
  public Vector3D getAngularMomentum() {
    //TODO
    return new Vector3D();
  }

  public Vector3D getTorque() {
    //TODO
    return new Vector3D();
  }
  
  public float getTime() {
    return (Float)time.get(tick_count);
  }
  
  public void setPosition(Vector3D new_position) {
    position_changed = true;
    position.add(new_position);
  }
  
  public void setVelocity(Vector3D new_velocity) {
    velocity_changed = true;
    velocity.add(new_velocity);
  }
  
  public void setAcceleration(Vector3D new_acceleration) {
    acceleration_changed = true;
    acceleration.add(new_acceleration);
  }
  
  public void setRotation(Quaternion new_rotation) {
    //TODO
  }
  
  public void setAngularMomentum(Vector3D new_angularMomentum) {
    //TODO
  }
  
  public void setTorque(Vector3D new_torque) {
    //TODO
  }
  
  public void setTime(float new_time) {
    time_changed = true;
    time.add(new_time);
  }
  
  public void saveValues() {
    if(tick_count > 0) {
      if(!position_changed)
        position.add(position.get(tick_count-1));
      if(!velocity_changed)
        velocity.add(velocity.get(tick_count - 1));
      if(!acceleration_changed)
        acceleration.add(acceleration.get(tick_count - 1));
      if(!time_changed)
        time.add(time.get(tick_count - 1));
    }
        
    position_changed = false;
    velocity_changed = false;
    acceleration_changed = false;
    time_changed = false;
  }
  
}
  
  
class Sun extends Planet {
  
  Sun(Vector3D pos_new, Vector3D vel_new, Vector3D acc_new, float mass, float r, String tex_name) {
    super(pos_new, vel_new, acc_new, mass, r, tex_name); 
    solver = new ODEEulerSolver();
    radius = r;
    bounding_sphere_radius = r;

  }

  public void render() {
    //lightFalloff(0.0,0.0000005,0.0);
    ambientLight(255,255,255,(float)getPosition().getX(), (float)getPosition().getY(), (float)getPosition().getZ());
    
    super.render();
  }
}
// Class which contains trig approximations. 

class TrigApprox {
  float sinLUT[];
  float cosLUT[];
  float SINCOS_PRECISION = 0.1f;
  int SINCOS_LENGTH = PApplet.parseInt(360.0f / SINCOS_PRECISION);
  
  TrigApprox() {
      sinLUT = new float[SINCOS_LENGTH];
      cosLUT = new float[SINCOS_LENGTH];
    
      for (int i = 0; i < SINCOS_LENGTH; i++) {
        sinLUT[i] = (float) Math.sin(i * DEG_TO_RAD * SINCOS_PRECISION);
        cosLUT[i] = (float) Math.cos(i * DEG_TO_RAD * SINCOS_PRECISION);
      }
  }
  
  // Return approximate cosine. Angle is in degrees
  public float ncos(float deg) {
    return cosLUT[(int)(deg / SINCOS_PRECISION)];
  }
  
  // Returns approximate sin. Angle is in degrees
  public float nsin(float deg) {
    return sinLUT[(int)(deg / SINCOS_PRECISION)];
  }
}
// The viewport class manages the camera and the final display of the scene

class Viewport {
  Object origin; // camera rotates around this object
  float distance = 7000; // distance of camera from origin
  Vector3D old_origin_location;
  Vector3D old_camera_location;
  
  PeasyCam camera;
  PeasyDragHandler left_drag;
  
  // Hack to disable camera moving in the lower 10% of the screen
  private final PeasyDragHandler drag = new PeasyDragHandler() {
    public void handleDrag(final double dx, final double dy) {
        if(mouseY < 0.9f*height)
          left_drag.handleDrag(dx, dy);
      }
  };  
  
  Viewport() {
    camera = new PeasyCam(Phys350Project.this, distance);
    camera.setResetOnDoubleClick(false);
    left_drag = camera.getRotateDragHandler();
    camera.setLeftDragHandler(drag);
    camera.setWheelScale(10.0f);
  }
  
  public void setOrigin(Object origin) {
    this.origin = origin;
    this.distance = origin.getBoundingSphere();
    camera.lookAt(origin.getPosition().getX(), origin.getPosition().getY(), origin.getPosition().getZ(), distance*4, (long)300);
    old_origin_location = origin.getPosition();
  }
  
  public void renderViewport() {
    double temp_angles[] = {0,0,0};
    
    // Hack math to get the camera to rotate around the origin
    if(old_origin_location.subtract(origin.getPosition()).getNorm() > 0.01f) {
      Rotation temp_rot = new Rotation(old_origin_location, origin.getPosition());

      try{
        temp_angles = temp_rot.getAngles(RotationOrder.XYZ);
      } catch(CardanEulerSingularityException e) {
      }
    }
   
    camera.lookAt(origin.getPosition().getX(), origin.getPosition().getY(), origin.getPosition().getZ(), (long)0);
    camera.rotateX(temp_angles[0]);
    camera.rotateY(temp_angles[1]);
    camera.rotateZ(temp_angles[2]);
    old_origin_location = new Vector3D(origin.getPosition().getX(), origin.getPosition().getY(), origin.getPosition().getZ());
  }
}
  
  static public void main(String args[]) {
    PApplet.main(new String[] { "--bgcolor=#FFFFFF", "Phys350Project" });
  }
}
