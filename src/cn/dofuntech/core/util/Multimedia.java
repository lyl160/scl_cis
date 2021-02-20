package cn.dofuntech.core.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Multimedia
{
  public static int[] getImageSize(File img)
    throws IOException
  {
    BufferedImage bi = ImageIO.read(img);
    return new int[] { bi.getWidth(), bi.getHeight() };
  }

  public static class Size
  {
    public int width;
    public int height;

    public Size(int w, int h)
    {
      this.width = w;
      this.height = h;
    }
  }
}