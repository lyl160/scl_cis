package cn.dofuntech.cis.api.util;

import java.util.Map;

import cn.dofuntech.core.page.Paginator;

public class PagesUtils {

	public static int[]  mapTOpages(Map<String, Object> map ){
		  int page  ;
		   int rows ;
		 int i[] = new int[2]  ;
		 if(map.get("page") == null || map.get("page").equals("") ){
			   page = Paginator.DEFAULT_CURRENT_PAGE;
			   i[0] = page ;
		 }else{
			 i[0] = Integer.parseInt(map.get("page").toString()) ;
		 }
		 if(map.get("rows") == null || map.get("rows").equals("") ){
			 rows = Paginator.DEFAULT_PAGE_SIZE;
			  i[1] = rows ;
		 }else{
			 i[1] = Integer.parseInt(map.get("rows").toString()) ;	 
		 }
		return  i  ; 
	}
}
