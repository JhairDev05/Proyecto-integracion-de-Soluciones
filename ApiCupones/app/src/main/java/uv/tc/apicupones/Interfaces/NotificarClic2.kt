package uv.tc.apicupones.Interfaces


import uv.tc.apicupones.pojo.Sucursal

interface NotificarClic2 {
    fun seleccionarItem(posicion: Int, sucursal: Sucursal)
}