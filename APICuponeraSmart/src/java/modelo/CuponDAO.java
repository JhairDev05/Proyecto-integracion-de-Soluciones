package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.pojo.Cupon;
import mybatis.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;

public class CuponDAO {
    
    public static List<Cupon> obtenerCupon(Integer idCupon){
        List<Cupon> cupon = new ArrayList<>();
        SqlSession conexionBD = MyBatisUtil.getSession();
        if(conexionBD != null){
            try{
                cupon = conexionBD.selectList("cupon.obtenerCupones", idCupon);
            }catch (Exception e){
                e.printStackTrace();
            }finally{
                conexionBD.close();
            }
        }
        return cupon;
    }
}
