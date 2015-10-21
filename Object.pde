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
  
  float getMass() {
    return mass;
  }
  
  Vector3D getPosition() {
    return data.getPosition();
  }
  
  Vector3D getVelocity() {
    return data.getVelocity();
  }
  
  Vector3D getAcceleration() {
    return data.getAcceleration();
  }
  
  Quaternion getRotation() {
    return data.getRotation();
  }
  
  Vector3D getAngularMomentum() {
    return data.getAngularMomentum();
  }

  Vector3D getTorque() {
    return data.getTorque();
  }
  
  float getBoundingSphere() {
    return bounding_sphere_radius;
  }
  
  void setPosition(Vector3D new_position) {
    data.setPosition(new_position);
  }
  
  void setVelocity(Vector3D new_velocity) {
    data.setVelocity(new_velocity);
  }
  
  void setAcceleration(Vector3D new_acceleration) {
    data.setAcceleration(new_acceleration);
  }
  
  void setRotation(Quaternion new_rotation) {
    data.setRotation(new_rotation);
  }
  
  void setAngularMomentum(Vector3D new_angularMomentum) {
    data.setAngularMomentum(new_angularMomentum);
  }
  
  void setTorque(Vector3D new_torque) {
    data.setTorque(new_torque);
  }
  
  void setTime(float new_time) {
    data.setTime(new_time);
  }
  
  void saveValues() {
    data.saveValues();
  }
  
  abstract void render();
  
  abstract void tick(float dt);
  
}
  
