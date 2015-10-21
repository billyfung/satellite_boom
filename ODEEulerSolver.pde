// Implements simple Euler method ODE solver

class ODEEulerSolver extends ODESolver {
  Vector3D nextAngularVelocity;
  double nextInvInertiaTensor[][];
  
  ODEEulerSolver() {
    nextAngularVelocity = new Vector3D();
  }
  
  void calculate(float dt) {
    //print("asdfasdfasdf");
    //print(makeString(acceleration));
    velocity = velocity.add(acceleration.scalarMultiply(dt));
    position = position.add(velocity.scalarMultiply(dt));
  }
  
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
}
