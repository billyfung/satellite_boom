// Class which holds generic calculated data

abstract class DataContainer {
  abstract Vector3D getPosition();
  abstract Vector3D getVelocity();
  abstract Vector3D getAcceleration();
  
  abstract Quaternion getRotation();
  abstract Vector3D getAngularMomentum();
  abstract Vector3D getTorque();
  
  abstract float getTime();
  
  abstract void setPosition(Vector3D new_position);
  abstract void setVelocity(Vector3D new_velocity);
  abstract void setAcceleration(Vector3D new_acceleration);
  
  abstract void setRotation(Quaternion new_rotation);
  abstract void setAngularMomentum(Vector3D new_angularMomentum);
  abstract void setTorque(Vector3D new_torque);
  
  abstract void setTime(float new_time);
  
  abstract void saveValues();
}
