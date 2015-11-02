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
  
  void calcRK4(float dt, Object newO) {
    
    o = newO;
    
    k1v = acceleration.scalarMultiply(dt);
    k1x = velocity.scalarMultiply(dt);
    
    k2v = calcAccel(k1x.scalarMultiply(0.5)).scalarMultiply(dt);
    k2x = velocity.add(k1v.scalarMultiply(0.5)).scalarMultiply(dt);
    
    k3v = calcAccel(k2x.scalarMultiply(0.5)).scalarMultiply(dt);
    k3x = velocity.add(k2v.scalarMultiply(0.5)).scalarMultiply(dt);
    
    k4v = calcAccel(k3x).scalarMultiply(dt);
    k4x = velocity.add(k3v).scalarMultiply(dt);
    
    velocity = velocity.add(k1v.add(k2v.add(k3v).scalarMultiply(2)).add(k4v).scalarMultiply(1/6.0));
    position = position.add(k1x.add(k2x.add(k3x).scalarMultiply(2)).add(k4x).scalarMultiply(1/6.0));
  }
  
  Vector3D calcAccel(Vector3D kPos){
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
  
  void calculateRotation(float dt, InertiaTensor inertiaTensor) {
    // http://www.euclideanspace.com/physics/kinematics/angularvelocity/QuaternionDifferentiation2.pdf
    // calculate derivatives of quaternion components
    double wx = nextAngularVelocity.getX();
    double wy = nextAngularVelocity.getY();
    double wz = nextAngularVelocity.getZ();
    
    Quaternion wquat = new Quaternion(0, wx, wy, wz);
    Quaternion qdot = rotation.times(wquat).times(0.5);

    // add quaternions
    rotation = rotation.plus(qdot.times(dt)).normalize();
    
    //print(makeString(torque));
  
    angularMomentum = angularMomentum.add(dt, torque);
    
    // calculate next local frame angular velocity
    // from http://www.mare.ee/indrek/varphi/vardyn.pdf
    nextInvInertiaTensor = inertiaTensor.invertedMatrix;
    nextAngularVelocity = multMatrix(nextInvInertiaTensor, rotation.applyTo(angularMomentum));
    
    // damping
    angularMomentum = angularMomentum.scalarMultiply(0.9999);
  }
  
  //Quaternion calcQuat2ndDeriv(Quaternion q, Quaternion qdot, double dt, torque
}
