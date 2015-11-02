// Class which holds real time data. Use this type of container whenever speed is required.

class RealTimeDataContainer extends DataContainer {
  private Vector3D position;
  private Vector3D velocity;
  private Vector3D acceleration;
  
  private Quaternion rotation;
  private Vector3D angularMomentum;
  private Vector3D torque;
 
  private float time;
  
  Vector3D getPosition() {
    return position;
  }
  
  Vector3D getVelocity() {
    return velocity;
  }
  
  Vector3D getAcceleration() {
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
  
  float getTime() {
    return time;
  }
  
  void setPosition(Vector3D new_position) {
    position = new_position;
  }
  
  void setVelocity(Vector3D new_velocity) {
    velocity = new_velocity;
  }
  
  void setAcceleration(Vector3D new_acceleration) {
    acceleration = new_acceleration;
  }
  
  void setRotation(Quaternion new_rotation) {
    rotation = new_rotation;
  }
  
  void setAngularMomentum(Vector3D new_angularMomentum) {
    angularMomentum = new_angularMomentum;
  }
  
  void setTorque(Vector3D new_torque) {
    torque = new_torque;
  }
  
  void setTime(float new_time) {
    time = new_time;
  }
  
  void saveValues() {}
  
}
  
  
