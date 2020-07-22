import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class lianjiechi {
    public static DruidDataSource dataSource;

    //1.初始化Druid连接池
    static {
        //第二种方式:使用软编码通过配置文件初始化DBCP
        try {
            Properties properties = new Properties();
            //通过类加载器加载配置文件
            InputStream inputStream = DBCP.class.getClassLoader().getResourceAsStream("druid.properties");      //配置文件名
            properties.load(inputStream);
            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //2.获取连接
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //3.关闭连接
    public static void closeAll(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void DruidTest() throws SQLException {
        //超过最大限制或报"TimeoutException",每打开一个关闭一个就不会发生异常
        for (int i = 0; i < 5; i++) {
            Connection connection = lianjiechi.getConnection();

            PreparedStatement pstmt = null;
            String sql = "select  * from user ";       // from后的“user”为对应的数据库中的表名
            pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String id = rs.getString(1);        //选择输出的列数
                String username = rs.getString(2);
                String pwd = rs.getString(3);
//                       System.out.println(id+"----"+username+"-----"+pwd);
            }

            System.out.println(connection);
//            lianjiechi.closeAll(connection, null, null);      //不关闭，就不能再选择之前的接口了。关闭了，每次都选择相同的数据库接口。
        }
    }
    private static class DBCP {
    }
}
