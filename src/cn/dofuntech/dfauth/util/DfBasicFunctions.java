package cn.dofuntech.dfauth.util;

import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

public class DfBasicFunctions
{
  public static String GETDATETIME(String args)
  {
	String[] buf1 = { "YYYY", "YY", "MM", "DD", "HH", "MI", "SS" };
    String[] buf2 = { "yyyy", "yy", "MM", "dd", "HH", "mm", "ss" };
    String str = args.trim();
    for (int i = 0; i < buf1.length; i++) {
      str = StringUtils.replace(str, buf1[i], buf2[i]);
    }
    Calendar calendar = Calendar.getInstance();
    return DateFormatUtils.format(calendar.getTime(), str);
  }

  public static String GETDATETIME()
  {
    return GETDATETIME("yyyyMMDDHHmmss");
  }

  public static String GETDATE()
  {
    return GETDATETIME("yyyyMMdd");
  }

  public static long GETSECOND()
  {
    Date dt = new Date();
    long curSec = dt.getTime();
    curSec /= 1000L;
    return curSec;
  }
}