package cn.dofuntech.gencode.builder.util;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.NClob;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLXML;
import java.sql.Time;

public class JavaType {

    public static String getType(ResultSetMetaData rsmd, String columnName) throws SQLException {
        int count = rsmd.getColumnCount();
        for (int i = 1 ; i <= count ; i++) {
            String key = rsmd.getColumnName(i);
            if (!key.equals(columnName))
                continue;

            return getType(rsmd, i);
        }
        return "";
    }

    public static String getType(ResultSetMetaData rsmd, int i) throws SQLException {
        int count = rsmd.getColumnCount();
        if (i > count)
            return "";

        int columnType = rsmd.getColumnType(i);
        int len = rsmd.getColumnDisplaySize(i);
        System.out.println("================" + len);
        switch (columnType) {
            case java.sql.Types.ARRAY:
                return Array.class.getSimpleName();
            case java.sql.Types.BIGINT:
                return Long.class.getSimpleName();
            case java.sql.Types.BINARY:
                return "byte[]";
            case java.sql.Types.BIT:
                return Integer.class.getSimpleName();
            case java.sql.Types.BLOB:
                return Blob.class.getName();
            case java.sql.Types.BOOLEAN:
                return Boolean.class.getSimpleName();
            case java.sql.Types.CHAR:
                return String.class.getSimpleName();
            case java.sql.Types.CLOB:
                return Clob.class.getName();
            case java.sql.Types.DATE:
                return java.util.Date.class.getName();
            case java.sql.Types.DECIMAL:
                return BigDecimal.class.getName();
            case java.sql.Types.DISTINCT:
                break;
            case java.sql.Types.DOUBLE:
                return Double.class.getSimpleName();
            case java.sql.Types.FLOAT:
                return Float.class.getSimpleName();
            case java.sql.Types.INTEGER:
                if (len >= 8)
                    return Long.class.getSimpleName();
                return Integer.class.getSimpleName();
            case java.sql.Types.JAVA_OBJECT:
                return Object.class.getSimpleName();
            case java.sql.Types.LONGVARCHAR:
                return String.class.getSimpleName();
            case java.sql.Types.LONGNVARCHAR:
                return String.class.getSimpleName();
            case java.sql.Types.LONGVARBINARY:
                return "byte[]";
            case java.sql.Types.NCHAR:
                return String.class.getName();
            case java.sql.Types.NCLOB:
                return NClob.class.getName();
            case java.sql.Types.NULL:
                break;
            case java.sql.Types.NUMERIC:
                return BigDecimal.class.getName();
            case java.sql.Types.NVARCHAR:
                return String.class.getSimpleName();
            case java.sql.Types.OTHER:
                return Object.class.getSimpleName();
            case java.sql.Types.REAL:
                return Double.class.getSimpleName();
            case java.sql.Types.REF:
                break;
            case java.sql.Types.ROWID:
                return RowId.class.getName();
            case java.sql.Types.SMALLINT:
                return Short.class.getSimpleName();
            case java.sql.Types.SQLXML:
                return SQLXML.class.getName();
            case java.sql.Types.STRUCT:
                break;
            case java.sql.Types.TIME:
                return Time.class.getName();
            case java.sql.Types.TIMESTAMP:
                return "Timestamp";
            case java.sql.Types.TINYINT:
                return Integer.class.getSimpleName();
            case java.sql.Types.VARBINARY:
                return "byte[]";
            case java.sql.Types.VARCHAR:
                return String.class.getSimpleName();
            default:
                break;
        }
        return "";
    }

    public static String getBasicType(String type) {
        if (type.equals("Boolean"))
            type = "Boolean";
        if (type.equals("Byte") || type.equals("java.lang.Byte"))
            type = "byte";
        else if (type.equals("Short") || type.equals("java.lang.Short"))
            type = "Short";
        else if (type.equals("Integer") || type.equals("java.lang.Integer"))
            type = "Integer";
        else if (type.equals("Long") || type.equals("java.lang.Long"))
            type = "Long";
        else if (type.equals("Float") || type.equals("java.lang.Float"))
            type = "Float";
        else if (type.equals("Double") || type.equals("java.lang.Double"))
            type = "Double";

        return type;
    }

}
