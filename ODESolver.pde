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
  void setValue(Vector3D position, Vector3D velocity, Vector3D acceleration) {
    this.position = position;
    this.velocity = velocity;
    this.acceleration = acceleration;
  }
  
  void setAngularValues(Quaternion rotation, Vector3D angularMomentum, Vector3D torque) {
    this.rotation = rotation;
    this.angularMomentum = angularMomentum;
    this.torque = torque;
  }
  
  // Takes values sent to the solver and updates them one time step
  void calculate(float dt){}
  
  void calculateRotation(float dt, InertiaTensor globalTensor){}
  void calcRK4(float dt, Object newO){}
  
  // Use these getter fundtions after calling calculate to obtain new values
  Vector3D getPosition() {
    return position;
  }
  
  Vector3D getVelocity() {
    return velocity;
  }
  
  Vector3D getAcceleration() {
    return acceleration;
  }
  
  Vector3D calcAccel(Vector3D kPos){
    return acceleration;
  }
  
  Quaternion getRotation() {
    return rotation;
  }
  
  Vector3D getAngularMomentum() {
    return angularMomentum;
  }
  
  Vector3D getTorque() {
    return torque;
  }
}
  
  
