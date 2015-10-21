static class Config {
  static boolean bRealTime; // true if the data points are NOT precalculated
  static int numDataPoints; // number of data points to precalculate
  static boolean bShowOrbitPrediction; // true if you want to show the orbit prediction line. bRealTime MUST be false!
  
  static final float GRAVITY_CONSTANT = 6.67428E-20;
  static final float MASS_EARTH = 5.9737E24; //kg
  static final float RADIUS_EARTH = 6371; //km
  static final float RADIUS_MOON = 1737; //km
  static final float MASS_MOON = 7.3477E22; //kg
  static final float MOON_ORBIT_RADIUS = 363104; //km. Assumes spherical orbit
  static final float RADIUS_SUN = 6.955E5; //km
  static final float EARTH_ORBIT_RADIUS = 149598261; //km
  static final float MASS_SUN = 1.9891E30; //kg
  static float TICK_RATE = 1;
  
  static final float MASS_SATELLITE = 1.5;
  
  static final int FAR_Z_CLIP = 500000000;
  
  Config() {
    setDefaults();
  }
  
  static void setDefaults() {
    bRealTime = true;
    numDataPoints = 25000;
    bShowOrbitPrediction = true;
  }
}
  
