// Implements slider controls

class Slider extends AbstractSlider
{
  float tickSpacing;
  boolean paintTicks, snapToTicks;

  float[] pos = new float[2], size = new float[2];

  Slider(float x, float y, float width, float height, int halign, int valign, int orientation, float min, float max, float value, color color1, color color2)
  {
    super(min, max, value, 0.0);  // extent is not relevant here...

    this.halign = halign;
    this.valign = valign;
    this.orientation = orientation;
    this.color1 = color1;
    this.color2 = color2;

    c1 = orientation;
    c2 = 1 - c1;

    enablable = getIsEnablable();
    setBounds(x, y, width, height);
  }

  void setValue(float n)
  {
    if (snapToTicks)
    {
      n = min + round((n - min) / tickSpacing) * tickSpacing;
    }

    super.setValue(n);
  }

  void setTickSpacing(float spacing)
  {
    tickSpacing = spacing;
  }

  void setPaintTicks(boolean b)
  {
    paintTicks = b;
  }

  void setSnapToTicks(boolean b)
  {
    snapToTicks = b;
  }

  void run()
  {
    knob_locked = knob_locked && enablable && enabled && mousePressed;

    m_comp[0] = mouseX;
    m_comp[1] = mouseY;

    over_track = !degraded && enablable && enabled && !armed && !knob_locked && mousePressed && (m_comp[0] >= pos[0] && m_comp[0] < (pos[0] + size[0]) && m_comp[1] >= pos[1] && m_comp[1] < (pos[1] + size[1]));
    armed = !over_track && mousePressed;

    if (degraded)
    {
      return;
    }

    over_knob = over_track && (m_comp[c1] >= (pos[c1] + comp[c1]) && m_comp[c1] < (pos[c1] + comp[c1] + knob_size[c1]) && m_comp[c2] >= (pos[c2] + comp[c2]) && m_comp[c2] < (pos[c2] + comp[c2] + knob_size[c2]));

    if (over_knob)
    {
      knob_locked = true;
      offset = (m_comp[c1] - pos[c1] - comp[c1]) * (max - min) / (size[c1] - knob_size[c1]);
    }
    else if (over_track)
    {
      setValue(min + (m_comp[c1] - pos[c1] - knob_size[c1] / 2.0) * (max - min) / (size[c1] - knob_size[c1]));
    }

    if (knob_locked)
    {
      setValue((m_comp[c1] - pos[c1]) * (max - min) / (size[c1] - knob_size[c1]) - offset);
    }
  }

  boolean getIsEnablable()
  {
    return max != min;
  }

  void updateComp()
  {
    if (!degraded)
    {
      comp[c1] = !enablable ? 0.0 : (value - min) / (max - min) * (size[c1] - knob_size[c1]);
    }
  }

  void recalc()
  {
    size[c1] = orientation == HORIZONTAL ? width : height;
    size[c2] = orientation == HORIZONTAL ? height : width;
    pos[c1] = orientation == HORIZONTAL ? left : top;
    pos[c2] = orientation == HORIZONTAL ? top : left;

    knob_size[c1] = size[c2];
    knob_size[c2] = size[c2];

    degraded = knob_size[c1] > size[c1];  // gracefull degradation at paranormal sizes

    updateComp();
    comp[c2] = 0.0;
  }

  void draw()
  {
    drawTrack();

    if (!degraded)
    {
      drawKnob();

      if (paintTicks)
      {
        drawTicks();
      }
    }
  }

  void drawTrack()
  {
    noStroke();
    rectMode(CORNER);

    fill(color2);
    rect(pos[0], pos[1], size[0], size[1]);

    fill(color1);
    if (orientation == HORIZONTAL)
    {
      rect(pos[0], pos[1] + size[1] / 2.0, size[0], 1.0);
    }
    else
    {
      rect(pos[0] + size[0] / 2.0, pos[1], 1.0, size[1]);
    }
  }

  void drawTicks()
  {
    float s = tickSpacing / (max - min) * (size[c1] - knob_size[c1]);
    if (s >= 2.0)
    {
      noStroke();
      rectMode(CENTER_DIAMETER);

      float o = pos[c1] + knob_size[c1] / 2.0;
      float i;
      int n_ticks = (int)((max - min) / tickSpacing);
      for (int n = 0; n <= n_ticks; n++)
      {
        i = o + n * s;

        if (knob_locked && i >= pos[c1] + comp[c1] && i < pos[c1] + comp[c1] + knob_size[c1])
        {
          fill(color2);
        }
        else
        {
          fill(color1);
        }

        if (orientation == HORIZONTAL)
        {
          rect(i, pos[1] + size[1] / 2.0, 1.0, size[1] / 3.0);
        }
        else
        {
          rect(pos[0] + size[0] / 2.0, i, size[0] / 3.0, 1.0);
        }
      }
    }
  }

  void drawKnob()
  {
    stroke(color1);
    fill(knob_locked ? color1 : color2);
    rectMode(CORNER);
    rect(pos[0] + comp[0], pos[1] + comp[1], knob_size[0] - 1.0, knob_size[1] - 1.0);
  }
}

