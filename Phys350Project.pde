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
void setup() {
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
  objects.add(new Moon(new Vector3D(0,Config.MOON_ORBIT_RADIUS,0), new Vector3D(0,0,1.022), new Vector3D(0,0,0), Config.MASS_MOON, Config.RADIUS_MOON, "moonmap2k.jpg"));

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
  Vector3D temp_sat_ang_mom = new Vector3D(0.0,0.0,0.0);
  
  InertiaTensor inertiaTensor = new InertiaTensor(
    0.114, 0.00000, 0.00000,
    0.00000, 0.00451, 0.00000,
    0.00000, 0.00000, 0.114
  );
  
  //inertiaTensorTest(inertiaTensor);
  satelliteIndex = objects.size();
  objects.add(new Satellite(temp_sat_pos, temp_sat_vel, new Vector3D(0,0,0), temp_sat_rot, temp_sat_ang_mom, new Vector3D(), config.MASS_SATELLITE, inertiaTensor));
    sun = new Sun(new Vector3D(Config.EARTH_ORBIT_RADIUS,0,0), new Vector3D(0,29.78,0), new Vector3D(0,0,0), Config.MASS_SUN, Config.RADIUS_SUN, "sun32.bmp");
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
void draw() {  
  // Get tick rate from the slider
  Config.TICK_RATE = (float)canvas.slider_T.getValue()/(canvas.slider_T.getMaximum()-canvas.slider_T.getMinimum())*10/30.;
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
  
  perspective(PI/3.0, float(width)/float(height), 5, Config.FAR_Z_CLIP);     
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

void update_physics() {
  
  // tick all of the objects
  for(int i = 0; i < objects.size(); i++) {
    Object o = (Object)objects.get(i);
    o.tick(config.TICK_RATE);
  }
  
}  

void render_scene()
{
  renderer = (GLGraphics)g;
   
  gl_object = renderer.beginGL();
  scale(1.,1.,-1.);
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

void keyPressed() {
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

void calculate_data()
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
void printToFile(boolean flsh) {
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
void secondaryDraw(double angle) {
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
  applet2.text("Real-Time Satellite Angle (degrees)", applet2.width/2.0, 20);
  applet2.textFont(text_font_SM);
  applet2.textAlign(RIGHT);
  applet2.text("180", 24, 19);
  applet2.text("0", 24, applet2.height-21);
  applet2.textAlign(LEFT);
  applet2.text("A \nN \nG \nL \nE", 5, 150);  
  applet2.stroke(200,40,40);
  // Note: 1.84 is the scaling factor to get the angle to fit in between the 0 and 180 degree lines
  int degSatAngle = (int)(1.84*angle*180/PI);
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

void stop(){
  // Ensure the output file is closed properly when the program quits
  output.flush();
  output.close();
  super.stop();
}

void inertiaTensorTest(InertiaTensor i) {
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


