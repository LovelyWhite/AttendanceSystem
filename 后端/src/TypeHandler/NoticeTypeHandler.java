package TypeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;

public class NoticeTypeHandler extends BaseTypeHandler<String> {
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
        Blob blob = resultSet.getBlob(s);
        return new String(blob.getBytes(1, (int)blob.length()));
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
        Blob blob = resultSet.getBlob(i);
        return new String(blob.getBytes(1, (int)blob.length()));
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
            Blob blob = callableStatement.getBlob(i);
        return new String(blob.getBytes(1, (int)blob.length()));
    }
}
