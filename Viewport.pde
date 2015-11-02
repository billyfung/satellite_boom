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
        if(mouseY < 0.9*height)
          left_drag.handleDrag(dx, dy);
      }
  };  
  
  Viewport() {
    camera = new PeasyCam(Phys350Project.this, distance);
    camera.setResetOnDoubleClick(false);
    left_drag = camera.getRotateDragHandler();
    camera.setLeftDragHandler(drag);
    camera.setWheelScale(10.0);
  }
  
  void setOrigin(Object origin) {
    this.origin = origin;
    this.distance = origin.getBoundingSphere();
    camera.lookAt(origin.getPosition().getX(), origin.getPosition().getY(), origin.getPosition().getZ(), distance*4, (long)300);
    old_origin_location = origin.getPosition();
  }
  
  void renderViewport() {
    double temp_angles[] = {0,0,0};
    
    // Hack math to get the camera to rotate around the origin
    if(old_origin_location.subtract(origin.getPosition()).getNorm() > 0.01) {
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
  
