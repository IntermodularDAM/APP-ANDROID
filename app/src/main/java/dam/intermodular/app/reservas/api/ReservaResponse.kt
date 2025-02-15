package dam.intermodular.app.reservas.api

import dam.intermodular.app.reservas.model.Reservas

class ReservaResponse (
    val status: String,
    val message: String,
    val reservas: List<Reservas>
)