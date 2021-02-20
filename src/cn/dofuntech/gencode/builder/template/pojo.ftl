package ${pojoPackage};
import java.sql.Timestamp;
import cn.dofuntech.core.entity.DefaultValue;

/**
 * ${className_d}
 */
public class ${className_d} extends DefaultValue{
	
	 /**
     * 
     */
    private static final long serialVersionUID = 1L;
	
	<#list tableCarrays as tableCarray>
	<#if tableCarray.carrayName_x != 'id'>
	/**
	 * ${tableCarray.carrayName}
	 */
	<#if tableCarray.carrayType == 'String'>
	private ${tableCarray.carrayType} ${tableCarray.carrayName_x} = EMPTY;
	<#elseif tableCarray.carrayType == 'Integer'>
	private ${tableCarray.carrayType} ${tableCarray.carrayName_x} = INT_EMPTY;
	<#elseif tableCarray.carrayType == 'Long'>
	private ${tableCarray.carrayType} ${tableCarray.carrayName_x} = LONG_EMPTY;
	<#elseif tableCarray.carrayType == 'Float'>
	private ${tableCarray.carrayType} ${tableCarray.carrayName_x} = FLOAT_EMPTY;
	<#elseif tableCarray.carrayType == 'Double'>
	private ${tableCarray.carrayType} ${tableCarray.carrayName_x} = DOUBLE_EMPTY;
	<#else>
	private ${tableCarray.carrayType} ${tableCarray.carrayName_x};
	</#if>
	</#if>
	</#list>
	
	public ${className_d}() {
		super();
	}
	
	public ${className_d}(${stringCarrayNames2}) {
		super();
		<#list tableCarrays as tableCarray>
		this.${tableCarray.carrayName_x} = ${tableCarray.carrayName_x}; 
		</#list>
	}
	
	<#list tableCarrays as tableCarray>
	<#if tableCarray.carrayName_x != 'id'>
	public ${tableCarray.carrayType} get${tableCarray.carrayName_d}() {
		return ${tableCarray.carrayName_x};
	}

	public void set${tableCarray.carrayName_d}(${tableCarray.carrayType} ${tableCarray.carrayName_x}) {
		this.${tableCarray.carrayName_x} = ${tableCarray.carrayName_x};
	} 
	</#if>
	</#list>
}
