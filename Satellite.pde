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
  
  void render() {
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
    
  void render_trajectory() {
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
  
  void tick(float dt) {
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
  
    double find_angle(){
    Vector3D V = new Vector3D(0,1,0);
    Vector3D earth_pos_difference = new Vector3D();
    earth_pos_difference = getPosition().subtract(earth.getPosition());
    
    Vector3D newSatelliteAxisOrientation = globalFromSatelliteQuaternion.applyTo(V);
    
    Vector3D temp = new Vector3D(earth_pos_difference.getX(), earth_pos_difference.getY(), earth_pos_difference.getZ());

    double a = Vector3D.angle(newSatelliteAxisOrientation, temp);

    return a;
  }
}
    
    
