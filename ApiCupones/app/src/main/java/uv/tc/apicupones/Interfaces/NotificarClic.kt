package uv.tc.apicupones.Interfaces

import uv.tc.apicupones.pojo.Promocion

interface NotificarClic {
    fun seleccionarItem(posicion: Int, promocion: Promocion)
}