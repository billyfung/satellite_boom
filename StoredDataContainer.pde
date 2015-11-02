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
  
  
  Vector3D getPosition() {
    return (Vector3D)position.get(tick_count);
  }
  
  Vector3D getVelocity() {
    return (Vector3D)velocity.get(tick_count);
  }
  
  Vector3D getAcceleration() {
    return (Vector3D)acceleration.get(tick_count);
  }
  
  Quaternion getRotation() {
    //TODO
    return new Quaternion();
  }
  
  Vector3D getAngularMomentum() {
    //TODO
    return new Vector3D();
  }

  Vector3D getTorque() {
    //TODO
    return new Vector3D();
  }
  
  float getTime() {
    return (Float)time.get(tick_count);
  }
  
  void setPosition(Vector3D new_position) {
    position_changed = true;
    position.add(new_position);
  }
  
  void setVelocity(Vector3D new_velocity) {
    velocity_changed = true;
    velocity.add(new_velocity);
  }
  
  void setAcceleration(Vector3D new_acceleration) {
    acceleration_changed = true;
    acceleration.add(new_acceleration);
  }
  
  void setRotation(Quaternion new_rotation) {
    //TODO
  }
  
  void setAngularMomentum(Vector3D new_angularMomentum) {
    //TODO
  }
  
  void setTorque(Vector3D new_torque) {
    //TODO
  }
  
  void setTime(float new_time) {
    time_changed = true;
    time.add(new_time);
  }
  
  void saveValues() {
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
  
  
