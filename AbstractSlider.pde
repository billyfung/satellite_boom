class AbstractSlider
{
  static final int HORIZONTAL = 0;
  static final int VERTICAL = 1;

  static final int LEFT = 0;
  static final int RIGHT = 1;
  static final int CENTER = 2;
  static final int TOP = 3;
  static final int BOTTOM = 4;

  float min, max, value, extent;
  float old_min, old_max, old_value, old_extent;

  int orientation;
  float x, y, width, height;
  color color1, color2;
  int halign, valign;
  boolean enabled = true;

  float left, top;
  boolean enablable, degraded;
  int c1, c2;
  float[] knob_size = new float[2];
  float[] comp = new float[2];
  float[] m_comp = new float[2];
  float offset;
  boolean armed;
  boolean over, over_track, over_knob;
  boolean knob_locked;

  AbstractSlider(float min, float max, float value, float extent)
  {
    // contraints must be satisfied: min <= value <= value+extent <= max

    if (!(max >= min && value >= min && (value + extent) >= value && (value + extent) <= max))
    {
      throw new IllegalArgumentException(getClass() + ": invalid range properties");
    }

    this.min = old_min = min;
    this.max = old_max = max;
    this.value = old_value = value;
    this.extent = old_extent = extent;
  }

  float getMinimum()
  {
    return min;
  }

  float getMaximum()
  {
    return max;
  }

  float getValue()
  {
    return value;
  }

  float getExtent()
  {
    return extent;
  }

  void setMinimum(float n)
  {
    max = max(n, max);
    value = max(n, value);
    extent = min(max - value, extent);
    min = n;
    updateRangeProperties();
  }

  void setMaximum(float n)
  {
    min = min(n, min);
    extent = min(n - min, extent);
    value = min(n - extent, value);
    max = n;
    updateRangeProperties();
  }

  void setValue(float n)
  {
    value = constrain(n, min, max - extent);
    updateRangeProperties();
  }

  void setExtent(float n)
  {
    extent = constrain(n, 0.0, max - value);
    updateRangeProperties();
  }

  void setColors(color color1, color color2)
  {
    this.color1 = color1;
    this.color2 = color2;
  }

  void setLocation(float x, float y)
  {
    setBounds(x, y, width, height);
  }

  void setSize(float width, float height)
  {
    setBounds(x, y, width, height);
  }

  void setBounds(float x, float y, float width, float height)
  {
    this.left = x - (halign == LEFT ? 0.0 : (halign == RIGHT ? width : width / 2.0));;
    this.top = y - (valign == TOP ? 0.0 : (valign == BOTTOM ? height : height / 2.0));
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    recalc();
  }

  boolean getEnabled()
  {
    return enabled;
  }

  void setEnabled(boolean b)
  {
    enabled = b;
  }

  void updateRangeProperties()
  {
    enablable = getIsEnablable();
    rangePropertiesChanged();
    updateComp();
    updateExtent();
  }

  boolean getIsEnablable()  // always overriden...
  {
    return true;
  }

  void updateComp() {}

  void updateExtent() {}

  void recalc() {}

  void rangePropertiesChanged()
  {
    // this is the place to implement an event-posting system...
  }
}

