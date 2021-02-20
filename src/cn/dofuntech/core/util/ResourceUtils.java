package cn.dofuntech.core.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;

public class ResourceUtils {

	private static final MyResourceBundleControl ctl = new MyResourceBundleControl();

	  public static String getString(String baseName, String key)
	  {
	    return _getStringForLocale(Locale.getDefault(), baseName, key);
	  }
	  public static String ui(String key) {
	    return getString("ui", key);
	  }

	  private static String _getStringForLocale(Locale locale, String baseName, String key)
	  {
	    try
	    {
	      ResourceBundle rb = ResourceBundle.getBundle(baseName, locale, ResourceUtils.class.getClassLoader(), ctl);
	      return rb != null ? rb.getString(key) : null;
	    } catch (MissingResourceException e) {
	      return null; } catch (NullPointerException e) {
	    }
	    return null;
	  }

	  public static String getString(String baseName, String key, Object[] args)
	  {
	    String text = getString(baseName, key);
	    return text != null ? MessageFormat.format(text, args) : null;
	  }

	  public static String getStringForLocale(Locale locale, String baseName, String key, Object[] args)
	  {
	    String text = _getStringForLocale(locale, baseName, key);
	    return text != null ? MessageFormat.format(text, args) : null;
	  }

	  public static String loadFromResource(String resource) {
	    InputStream in = null;
	    BufferedReader reader = null;
	    try {
	      in = new FileInputStream(resource);
	      reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
	      return IOUtils.toString(reader);
	    } catch (Exception excp) {
	      throw new RuntimeException(excp);
	    } finally {
	      IOUtils.closeQuietly(reader);
	      IOUtils.closeQuietly(in);
	      reader = null;
	    }
	  }

	  private static class MyResourceBundleControl extends ResourceBundle.Control
	  {
	    public long getTimeToLive(String baseName, Locale locale)
	    {
	      return 3600000L;
	    }

	    public boolean needsReload(String baseName, Locale locale, String format, ClassLoader loader, ResourceBundle bundle, long loadTime)
	    {
	      return true;
	    }
	  }
}
